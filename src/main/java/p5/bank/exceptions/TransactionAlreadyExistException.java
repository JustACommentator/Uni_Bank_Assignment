package p5.bank.exceptions;

public class TransactionAlreadyExistException extends RuntimeException{
    public TransactionAlreadyExistException(){
        super("Transaction already exists");
    }
}
