package p5.bank;

public class OutgoingTransfer extends Transfer {
    public OutgoingTransfer(String _date, double _amount, String _description, String _sender, String _recipient) {
        super(_date, _amount, _description, _sender, _recipient);
    }

    @Override
    public double calculate() {
        return -this.getAmount();
    }
}