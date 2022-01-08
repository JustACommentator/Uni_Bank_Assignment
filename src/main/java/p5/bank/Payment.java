package p5.bank;

/**
 * class for payments made from a bank account
 * @version 25.10.2021
 * @author Tarik
 */
public class Payment extends Transaction{

    private double incomingInterest;
    private double outgoingInterest;

    /**
     * constructor
     * @param _date Date of the payment
     * @param _amount Payment amount
     * @param _description Description of the payment
     */
    public Payment(String _date, double _amount, String _description){
        super(_date, _amount, _description);
    }

    /**
     * constructor
     * @param _date Date of the payment
     * @param _amount Payment amount
     * @param _description Description of the payment
     * @param interestIn Incoming interest
     * @param interestOut Outgoing interest
     */
    public Payment(String _date, double _amount, String _description, double interestIn, double interestOut){ // full constructor
        super(_date,_amount,_description);
        this.setIncomingInterest(interestIn);
        this.setOutgoingInterest(interestOut);
    }

    /**
     * Copy-constructor
     * @param orig existing Payment-instance
     */
    public Payment(Payment orig){ // copy constructor
        this(orig.getDate(), orig.getAmount(), orig.getDescription(), orig.getIncomingInterest(), orig.getOutgoingInterest());
    }

    public double getIncomingInterest() {return this.incomingInterest;}
    void setIncomingInterest(double interestIn) {this.incomingInterest = interestIn;}
    public double getOutgoingInterest() {return this.outgoingInterest;}
    void setOutgoingInterest(double interestOut) {this.outgoingInterest = interestOut;}

    /**
     * method to return class attributes as string
     * @return class attributes as string
     */
    public String toString(){
        return super.toString() + "Eingehender Zinssatz: " + this.incomingInterest + "\n" + "Ausgehender Zinssatz: " + this.outgoingInterest;
    }

    /**
     * calculates the payment amount after applying the interest
     * @return payment amount after applying interest
     */
    @Override
    public double calculate() {
        if(this.amount > 0){
            return this.amount - this.amount*this.incomingInterest;
        }
        else{
            return this.amount + this.amount*this.outgoingInterest;
        }
    }

    /**
     * Overrides the object.equals() method
     * @param obj handed over object to compare
     * @return wether or not the handed over object is equal to this
     */
    public boolean equals(Object obj){
        if(!(obj instanceof Payment))
            return false;

        if(!super.equals(obj))
            return false;

        Payment p = (Payment) obj;

        if(this.incomingInterest != p.getIncomingInterest())
            return false;
        if(this.outgoingInterest != p.getOutgoingInterest())
            return false;

        return true;
    }
}
