package p5.bank;

import java.text.DecimalFormat;

/**
 * Abstract superclass for payments and transactions from a bank account
 * @version 25.10.2021
 * @author Tarik
 */
public abstract class Transaction implements CalculateBill{

    protected String date;
    protected double amount;
    protected String description;

    /**
     * abstract superconstructor for {@link Payment} and {@link Transfer}
     * @param _date Date of the interaction
     * @param _amount Interacted amount
     * @param _description Description of the interaction
     */
    public Transaction(String _date, double _amount, String _description) {
        this.setDate(_date);
        this.setAmount(_amount);
        this.setDescription(_description);
    }

    public String getDate() {return this.date;}
    protected void setDate(String _date) {this.date = _date;}
    public double getAmount() {return amount;}
    protected void setAmount(double _amount) {this.amount = _amount;}
    public String getDescription() {return this.description;}
    protected void setDescription(String _description) {this.description = _description;}

    /**
     * method to return class attributes as a string
     * @return class attributes as string
     */
    public String toString(){
        double afterCalculate = this.calculate();
        DecimalFormat df = new DecimalFormat("#.00");;
        return "Datum: " + this.date + "\n" + "Menge: " + df.format(afterCalculate) + "€\n" + "Beschreibung: " + this.description + "\n";
    }

    /**
     * Overrides the object.equals() method
     * @param obj handed over object to compare
     * @return wether or not the handed over object is equal to this
     */
    public boolean equals(Object obj) {
        if(!(obj instanceof Transaction))
            return false;

        Transaction t = (Transaction) obj;

        if(!this.date.equals(t.getDate()))
            return false;
        if(this.amount != t.getAmount())
            return false;
        if(!this.description.equals(t.getDescription()))
            return false;

        return true;
    }
}
