package p5.bank;

/**
 * @version 25.10.2021
 * @author Tarik
 */
public interface CalculateBill {

    /**
     * used by {@link Payment} and {@link Transfer} to determine the final amount after interest
     * @return final amount after interest
     */
    double calculate();
}
