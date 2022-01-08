package p5.bank;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainviewController implements Initializable {

    private PrivateBank bank;

    public void setBank(PrivateBank _bank) {
        bank = _bank;
    }

    @FXML
    private ListView<Label> accountList;

    @FXML
    private void populateList() {

        accountList.getItems().clear();

        for (String s : bank.getAllAccounts()) {
            s = s.replaceAll(".json", "");
            accountList.getItems().add(new Label(s));
        }
    }

    @FXML
    private AnchorPane accountInfo;

    public void resetAccountInfo() {
        accountInfo.getChildren().clear();
    }

    @FXML
    private void loadAccountView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Accountview.fxml"));

        Parent root = loader.load();

        AccountviewController accountviewController = loader.getController();
        accountviewController.setAccountName(
                StringHelper.trimAccountName(
                        accountList.getSelectionModel().getSelectedItem()));
        accountviewController.setBank(bank);
        accountviewController.setMVC(this);

        loader.setController(accountviewController);
        accountviewController.displayInfo();


        accountInfo.getChildren().clear();
        accountInfo.getChildren().add(root);


    }

    @FXML
    private void deleteAccount() throws IOException {
        String accountName = StringHelper.trimAccountName(accountList.getSelectionModel().getSelectedItem());

        Alert alert = new Alert(Alert.AlertType.NONE, "Account \"" + accountName + "\" wirklich unwiederruflich löschen?", ButtonType.OK, ButtonType.CANCEL);
        alert.setTitle("Account löschen");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            bank.deleteAccount(accountName);
            populateList();
        }
    }

    @FXML
    public void addAccount() throws IOException {
        TextInputDialog textDialog = new TextInputDialog();
        textDialog.setTitle("Account anlegen");
        textDialog.setHeaderText("");
        textDialog.setContentText("Accountname: ");
        textDialog.setGraphic(null);
        Optional<String> accountName = textDialog.showAndWait(); //anpassen

        if (accountName.isPresent())
            if(accountName.get() == "") {
                Alert alert = new Alert(Alert.AlertType.NONE, "Der Account muss einen Namen besitzen", ButtonType.OK);
                alert.setTitle("Fehler");
                alert.showAndWait();
            }
            else if (StringHelper.checkValid(accountName.get())) {
                bank.createAccount(accountName.get().trim());
                populateList();
            } else {
                Alert alert = new Alert(Alert.AlertType.NONE, "Fehlgeschlagen, bitte überprüfen Sie Ihre angaben", ButtonType.OK);
                alert.setTitle("Fehler");
                alert.showAndWait();
            }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            bank = new PrivateBank("SehrGuteBank", 0.025, 0.05, "./src/main/java/bank/accounts/bank1");
        } catch (IOException e) {
            e.printStackTrace();
        }
        populateList();
    }
}
