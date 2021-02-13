package transaction_http_server.exception;

/**
 * @author Orlov Diga
 */
public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(String id, long currentAmountMoney, long deductibleAmountMoney) {
        super("The account with id " + id +
              " does not have enough funds to withdraw " + deductibleAmountMoney +
              " The current amount of money in the balance " + currentAmountMoney);
    }
}
