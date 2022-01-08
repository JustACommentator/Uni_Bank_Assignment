package p5.bank.exceptions;

public class AccountDoesNotExistException extends RuntimeException{
    public AccountDoesNotExistException(){
        super("Account does not exist");
    }
}
