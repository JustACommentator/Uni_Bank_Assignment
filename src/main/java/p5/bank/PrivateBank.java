package p5.bank;

import javafx.scene.control.Label;
import p5.bank.exceptions.AccountAlreadyExistsException;
import p5.bank.exceptions.AccountDoesNotExistException;
import p5.bank.exceptions.TransactionAlreadyExistException;
import p5.bank.exceptions.TransactionDoesNotExistException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Class for simulating a bank
 * @version 16.11.2021
 * @author Tarik
 */
public class PrivateBank implements Bank, Serializable {
    /**
     * name of bank
     */
    private String name;

    /**
     * Interest as positive value between 0 and 1. Identical to incomingInterest in Payment
     */
    private double incomingInterest;

    /**
     * Interest as positive value between 0 and 1. Identical to outgoingInterest in Payment
     */
    private double outgoingInterest;

    /**
     * Links accounts to transactions, so that every saved account can be linked to 0-n Transactions
     *
     * Beispiel: „Konto 1“ -> [Transaktion 1, Transaktion2]
     *           „Konto Adam“ -> [Transaktion 3]
     *           „Konto Eva“ -> []
     */
    private HashMap<String, List<Transaction>> accountsToTransactions = new HashMap<String, List<Transaction>>();

    /**
     * directory where serialized accounts are stored
     */
    private String directory;

    public String getDirectory() {return this.directory;}
    public String getName() {return this.name;}
    private void setName(String _name) {this.name = _name;}
    public double getIncomingInterest() {return this.incomingInterest;}
    private void setIncomingInterest(double _incomingInterest) {this.incomingInterest = _incomingInterest;}
    public double getOutgoingInterest() {return this.outgoingInterest;}
    private void setOutgoingInterest(double _outgoingInterest) {this.outgoingInterest = _outgoingInterest;}

    public PrivateBank(String _name, double _incomingInterest, double _outgoingInterest, String _directory) throws IOException {
        this.setName(_name);
        this.setIncomingInterest(_incomingInterest);
        this.setOutgoingInterest(_outgoingInterest);
        this.directory = _directory;
        this.readAccounts();
    }

    public PrivateBank(PrivateBank orig) throws IOException {
        this(orig.getName(), orig.getIncomingInterest(), orig.getOutgoingInterest(), orig.getDirectory());
        this.accountsToTransactions = orig.accountsToTransactions;
    }

    /**
     * method to return class attributes as a string
     * @return class attributes as string
     */
    public String toString() { return this.getName() + "; " + this.getIncomingInterest() + "; " + this.getOutgoingInterest() + "; " + this.getDirectory(); }

    /**
     * Overrides the object.equals() method
     * @param obj handed over object to compare
     * @return wether or not the handed over object is equal to this
     */
    public boolean equals(Object obj) {
        if(!(obj instanceof PrivateBank))
            return false;

        PrivateBank p = (PrivateBank) obj;

        if(!this.name.equals(p.getName()))
            return false;
        if(this.incomingInterest != p.getIncomingInterest())
            return false;
        if(this.outgoingInterest != p.getOutgoingInterest())
            return false;
        if(this.directory != p.getDirectory())
            return false;
        if(this.accountsToTransactions != p.accountsToTransactions)
            return false;

        return true;
    }

    /**
     * Adds an account to the bank. If the account already exists, an exception is thrown.
     *
     * @param account the account to be added
     * @throws AccountAlreadyExistsException if the account already exists
     */
    @Override
    public void createAccount(String account) throws AccountAlreadyExistsException, IOException {
        if(accountsToTransactions.containsKey(account)){
            throw new AccountAlreadyExistsException();
        }
        accountsToTransactions.put(account, new LinkedList<>());

        writeAccount(account);
    }

    /**
     * Adds an account (with all specified transactions) to the bank. If the account already exists,
     * an exception is thrown.
     *
     * @param account the account to be added
     * @throws AccountAlreadyExistsException if the account already exists
     */
    @Override
    public void createAccount(String account, List<Transaction> transactions) throws AccountAlreadyExistsException, IOException {
        if(accountsToTransactions.containsKey(account)){
            throw new AccountAlreadyExistsException();
        }
        for(Transaction t : transactions) {
            if(t instanceof Payment){
                ((Payment) t).setIncomingInterest(this.incomingInterest);
                ((Payment) t).setOutgoingInterest(this.outgoingInterest);
            }
        }
        accountsToTransactions.put(account, transactions);

        writeAccount(account);
    }

    /**
     * Adds a transaction to an account. If the specified account does not exist, an exception is
     * thrown. If the transaction already exists, an exception is thrown.
     *
     * @param account     the account to which the transaction is added
     * @param transaction the transaction which is added to the account
     * @throws TransactionAlreadyExistException if the transaction already exists
     */
    @Override
    public void addTransaction(String account, Transaction transaction) throws TransactionAlreadyExistException, AccountDoesNotExistException, IOException {
        if(!accountsToTransactions.containsKey(account)){
            throw new AccountDoesNotExistException();
        }
        if(accountsToTransactions.get(account).contains(transaction)){
            throw new TransactionAlreadyExistException();
        }
        if(transaction instanceof Payment){
            ((Payment) transaction).setIncomingInterest(this.incomingInterest);
            ((Payment) transaction).setOutgoingInterest(this.outgoingInterest);
        }
        accountsToTransactions.get(account).add(transaction);

        this.writeAccount(account);
    }

    /**
     * Removes a transaction from an account. If the transaction does not exist, an exception is
     * thrown.
     *
     * @param account     the account from which the transaction is removed
     * @param transaction the transaction which is added to the account
     * @throws TransactionDoesNotExistException if the transaction cannot be found
     */
    @Override
    public void removeTransaction(String account, Transaction transaction) throws TransactionDoesNotExistException, IOException {
        if(!accountsToTransactions.get(account).contains(transaction)){
            throw new TransactionDoesNotExistException();
        }
        accountsToTransactions.get(account).remove(transaction);

        this.writeAccount(account);
    }

    /**
     * Checks whether the specified transaction for a given account exists.
     *
     * @param account     the account from which the transaction is checked
     * @param transaction the transaction which is added to the account
     */
    @Override
    public boolean containsTransaction(String account, Transaction transaction) { return accountsToTransactions.get(account).contains(transaction); }

    /**
     * Calculates and returns the current account balance.
     *
     * @param account the selected account
     * @return the current account balance
     */
    @Override
    public double getAccountBalance(String account) {
        double sum = 0;
        for(Transaction t : getTransactions(account)){
            sum += t.calculate();
        }
        return sum;
    }

    /**
     * Returns a list of transactions for an account.
     *
     * @param account the selected account
     * @return the list of transactions
     */
    @Override
    public List<Transaction> getTransactions(String account) {
        return accountsToTransactions.get(account);
    }

    /**
     * Returns a sorted list (-> calculated amounts) of transactions for a specific account . Sorts the list either in ascending or descending order
     * (or empty).
     *
     * @param account the selected account
     * @param asc     selects if the transaction list is sorted ascending or descending
     * @return the list of transactions
     */
    @Override
    public List<Transaction> getTransactionsSorted(String account, boolean asc) {
        List<Transaction> newList = this.accountsToTransactions.get(account);
        newList.sort((Transaction a, Transaction b) -> (asc ? Double.compare(a.calculate(), b.calculate()) : Double.compare(b.calculate(), a.calculate())));
        return newList;
    }

    /**
     * Returns a list of either positive or negative transactions (-> calculated amounts).
     *
     * @param account  the selected account
     * @param positive selects if positive  or negative transactions are listed
     * @return the list of transactions
     */
    @Override
    public List<Transaction> getTransactionsByType(String account, boolean positive) {
        List<Transaction> newList = new LinkedList<Transaction>();
        for (Transaction t : this.accountsToTransactions.get(account)) {
            if (positive) {
                if (t.calculate() >= 0) {
                    newList.add(t);
                }
            } else {
                if (t.calculate() < 0) {
                    newList.add(t);
                }
            }
        }
        return newList;
        /*
        return accountsToTransactions.get(account).stream().filter(x -> x.calculate() >= 0).collect(Collectors.toList());
        */
    }

    /**
     * Deletes an account
     * @param account account to be deleted
     * @throws AccountDoesNotExistException
     * @throws IOException
     */
    @Override
    public void deleteAccount(String account) throws AccountDoesNotExistException, IOException {
        accountsToTransactions.remove(account);

        File file = new File((directory + "/" + account + ".json"));
        if (file.exists()){
            file.delete();
        }
    }

    /**
     * @return List containing names of all accounts
     */
    @Override
    public List<String> getAllAccounts() {
        List<String> accountList = new LinkedList<String>();
        File folder = new File(directory);
        if(folder.listFiles() != null)
            for (File file : folder.listFiles())
                accountList.add(file.getName());
        return accountList;
    }

    /**
     * reads content of all files in {@link PrivateBank#directory} and adds them as accounts and transactions
     * @throws IOException
     */
    private void readAccounts() throws IOException {
        GsonBuilder gson = new GsonBuilder();

        gson.registerTypeHierarchyAdapter(Transaction.class, new CustomSerializer()); //registers custom serializer, Gson will use said serializer when dealing with Transaction objects from now on
        Type type = new TypeToken<List<Transaction>>(){}.getType(); // Token to tell Gson what to translate JSON files into | TypeToken instead of List<Transaction>, because that's apparently not possible


        File folder = new File(directory);
        if(folder.listFiles() != null) {
            for (File file : folder.listFiles()) {
                String json = new String(Files.readAllBytes(Paths.get(directory+"/"+file.getName())));

                List<Transaction> myList = gson.create().fromJson(json, type);

                createAccount(file.getName().replaceAll(".json", ""), myList);
            }
        }
    }

    /**
     * creates/rewrites .json file under {@link PrivateBank#directory} containing all transactions linked to {@link PrivateBank#writeAccount(String) account}
     * @param account account to write
     * @throws IOException
     */
    private void writeAccount(String account) throws IOException {
        CustomSerializer ser = new CustomSerializer();
        List<JsonElement> jsonArr = new LinkedList<JsonElement>();
        for(Transaction t : accountsToTransactions.get(account)) {
            jsonArr.add(ser.serialize(t, null, null));
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        Writer writer = new FileWriter(directory + "/" + account + ".json");
        gson.toJson(jsonArr, writer);
        writer.close();
    }

}