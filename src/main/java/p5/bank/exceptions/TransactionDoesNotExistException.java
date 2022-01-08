package p5.bank.exceptions;

public class TransactionDoesNotExistException extends RuntimeException{
    public TransactionDoesNotExistException() {
        super("Transaction does not exist");
    }
}
