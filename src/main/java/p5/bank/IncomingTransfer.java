package p5.bank;

public class IncomingTransfer extends Transfer {
    public IncomingTransfer(String _date, double _amount, String _description, String _sender, String _recipient) {
        super(_date, _amount, _description, _sender, _recipient);
    }

    @Override
    public double calculate() {
        return this.getAmount();
    }
}
