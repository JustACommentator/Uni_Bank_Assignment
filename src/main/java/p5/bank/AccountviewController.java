package p5.bank;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AccountviewController {

    private PrivateBank bank;

    private String accountName;

    private MainviewController mvc;

    private List<Transaction> transactionList;

    @FXML
    private TabPane tabPane;

    @FXML
    private Label accountNameLabel;

    @FXML
    private Label kontostandLabel;

    @FXML
    private ListView<Transaction> transactionView;

    @FXML
    private TextField dateLabel;

    @FXML
    private TextField amountLabel;

    @FXML
    private TextArea descriptionLabel;

    @FXML
    private TextField senderLabel;

    @FXML
    private TextField recipientLabel;

    @FXML
    private Tab transferTab;

    @FXML
    private Tab paymentTab;

    public void setMVC(MainviewController _mvc) {
        mvc = _mvc;
    }

    public void setBank(PrivateBank _bank) {
        bank = _bank;
    }

    public void setAccountName(String _accountName) {
        accountName = _accountName;
    }

    public void displayInfo() {
        accountNameLabel.setText(accountName);
        transactionList = bank.getTransactions(accountName);

        updateBalance();
        populateList();
    }

    private void updateBalance() {
        double kontostand = bank.getAccountBalance(accountName);
        DecimalFormat df = new DecimalFormat("#.00");

        kontostandLabel.setText((kontostand == 0 ? "0" : "") + df.format(kontostand) + "€");
    }

    private void populateList() {
        transactionView.getItems().clear();
        for (Transaction t : transactionList) {
            transactionView.getItems().add(t);
        }
    }

    @FXML
    private void deleteTransaction(ActionEvent actionEvent) throws IOException {
        Transaction transaction = transactionView.getSelectionModel().getSelectedItem();


        Alert alert = new Alert(Alert.AlertType.NONE, "Möchten Sie diese Transaktion wirklich unwiederruflich löschen?", ButtonType.OK, ButtonType.CANCEL);
        alert.setTitle("Transaktion entfernen");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            bank.removeTransaction(accountName, transaction);
            updateBalance();
            populateList();
        }
    }

    @FXML
    private void addTransaction() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("TransactionDialogView.fxml"));
        DialogPane transactionDialog = loader.load();
        transactionDialog.getStylesheets().add(getClass().getResource("tab.css").toString());
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(transactionDialog);
        dialog.setTitle("Neue Transaktion");

        Optional<ButtonType> clickedButton = dialog.showAndWait();
        if (clickedButton.get() == ButtonType.OK) {
            if (tabPane.getSelectionModel().getSelectedItem() == transferTab) {
                System.out.printf("transfer"); //Test, the rest will be added once it's working
            } else {
                System.out.println("payment"); //Test, the rest will be added once it's working
            }
        }

    }

    @FXML
    private void goBack() {
        mvc.resetAccountInfo();
    }

    @FXML
    private void sortAsc() {
        transactionList = bank.getTransactionsSorted(accountName, true);
        populateList();
    }

    @FXML
    private void sortDesc() {
        transactionList = bank.getTransactionsSorted(accountName, false);
        populateList();
    }

    @FXML
    private void sortPos() {
        transactionList = bank.getTransactionsByType(accountName, true);
        populateList();
    }

    @FXML
    private void sortNeg() {
        transactionList = bank.getTransactionsByType(accountName, false);
        populateList();
    }
}
