package p5.bank;

/**
 * class for transfers made from a bank account
 * @version 25.10.2021
 * @author Tarik
 */
public class Transfer extends Transaction{

    private String sender;
    private String recipient;

    /**
     * constructor
     * @param _date Date of the transfer
     * @param _amount Transferred amount
     * @param _description Description of the transfer
     */
    public Transfer(String _date, double _amount, String _description){ // constructor
        super(_date, _amount, _description);
    }

    /**
     * constructor
     * @param _date Date of the transfer
     * @param _amount Transferred amount
     * @param _description Description of the transfer
     * @param _sender Sender
     * @param _recipient Recipient
     */
    public Transfer(String _date, double _amount, String _description, String _sender, String _recipient){ // full constructor
        super(_date, _amount, _description);
        this.setSender(_sender);
        this.setRecipient(_recipient);
    }

    /**
     * copy-constructor
     * @param orig existing transfer-instance
     */
    public Transfer(Transfer orig){ // copy constructor
        this(orig.getDate(), orig.getAmount(), orig.getDescription(), orig.getSender(), orig.getRecipient());
    }

    public String getSender() {return this.sender;}
    private void setSender(String _sender) {this.sender = _sender;}
    public String getRecipient() {return this.recipient;}
    private void setRecipient(String _recipient) {this.recipient = _recipient;}

    /**
     * method to return class attributes as string
     * @return class attributes as string
     */
    public String toString(){
        return super.toString() + "Sender: " + this.sender + "\n" + "Empfänger: " + this. recipient;
    }

    /**
     * method to return transferred amount. Subject to change in the future
     * @return transferred amount
     */
    @Override
    public double calculate() {
        return this.getAmount();
    }

    /**
     * Overrides the object.equals() method
     * @param obj handed over object to compare
     * @return wether or not the handed over object is equal to this
     */
    public boolean equals(Object obj){
        if(!(obj instanceof Transfer))
            return false;

        if(!super.equals(obj))
            return false;

        Transfer t = (Transfer) obj;

        if(!this.sender.equals((t.getSender())))
            return false;
        if(!this.recipient.equals((t.getRecipient())))
            return false;

        return true;
    }
}
