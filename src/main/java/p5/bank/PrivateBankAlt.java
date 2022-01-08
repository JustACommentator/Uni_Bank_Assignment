package p5.bank;



import p5.bank.exceptions.AccountAlreadyExistsException;
import p5.bank.exceptions.AccountDoesNotExistException;
import p5.bank.exceptions.TransactionAlreadyExistException;
import p5.bank.exceptions.TransactionDoesNotExistException;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Class for simulating a bank
 * @version 16.11.2021
 * @author Tarik
 */
public class PrivateBankAlt implements Bank{
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


    public String getName() {return this.name;}
    private void setName(String _name) {this.name = _name;}
    public double getIncomingInterest() {return this.incomingInterest;}
    private void setIncomingInterest(double _incomingInterest) {this.incomingInterest = _incomingInterest;}
    public double getOutgoingInterest() {return this.outgoingInterest;}
    private void setOutgoingInterest(double _outgoingInterest) {this.outgoingInterest = _outgoingInterest;}

    public PrivateBankAlt(String _name, double _incomingInterest, double _outgoingInterest){
        this.setName(_name);
        this.setIncomingInterest(_incomingInterest);
        this.setOutgoingInterest(_outgoingInterest);
    }

    public PrivateBankAlt(PrivateBankAlt orig){
        this(orig.getName(), orig.getIncomingInterest(), orig.getOutgoingInterest());
        this.accountsToTransactions = orig.accountsToTransactions;
    }

    /**
     * method to return class attributes as a string
     * @return class attributes as string
     */
    public String toString() { return this.getName() + "; " + this.getIncomingInterest() + "; " + this.getOutgoingInterest(); }

    /**
     * Overrides the object.equals() method
     * @param obj handed over object to compare
     * @return wether or not the handed over object is equal to this
     */
    public boolean equals(Object obj) {
        if(!(obj instanceof PrivateBankAlt))
            return false;

        PrivateBankAlt p = (PrivateBankAlt) obj;

        if(!this.name.equals(p.getName()))
            return false;
        if(this.incomingInterest != p.getIncomingInterest())
            return false;
        if(this.outgoingInterest != p.getOutgoingInterest())
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
    public void createAccount(String account) throws AccountAlreadyExistsException {
        if(accountsToTransactions.containsKey(account)){
            throw new AccountAlreadyExistsException();
        }
        accountsToTransactions.put(account, new LinkedList<>());
    }

    /**
     * Adds an account (with all specified transactions) to the bank. If the account already exists,
     * an exception is thrown.
     *
     * @param account the account to be added
     * @throws AccountAlreadyExistsException if the account already exists
     */
    @Override
    public void createAccount(String account, List<Transaction> transactions) throws AccountAlreadyExistsException {
        if(accountsToTransactions.containsKey(account)){
            throw new AccountAlreadyExistsException();
        }
        accountsToTransactions.put(account, transactions);
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
    public void addTransaction(String account, Transaction transaction) throws TransactionAlreadyExistException, AccountDoesNotExistException {
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
    public void removeTransaction(String account, Transaction transaction) throws TransactionDoesNotExistException {
        if(!accountsToTransactions.get(account).contains(transaction)){
            throw new TransactionDoesNotExistException();
        }
        accountsToTransactions.get(account).remove(transaction);
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
            boolean is_transfer = t instanceof Transfer;
            if(is_transfer) {
                double mult = 1;
                if (((Transfer) t).getSender() == account)
                    mult = -1;
                sum += t.getAmount() * mult;
            }
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
    }

    @Override
    public void deleteAccount(String account) throws AccountDoesNotExistException, IOException {

    }

    @Override
    public List<String> getAllAccounts() {
        return null;
    }
}
