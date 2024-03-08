import org.example.BankAccount;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class BankAccountTest {
    private BankAccount bAccount;

    @BeforeEach
    public void startup() {
        bAccount = new BankAccount(1000);
    }

    @Test
    @DisplayName("Test withdraw with enough balance")
    public void Withdraw_WithEnoughBalance_SubtractsFromAccount() {
        Assertions.assertTrue(bAccount.withdraw(500));
        Assertions.assertEquals(500, bAccount.getBalance());
    }

    @Test
    @DisplayName("Test withdraw with not enough balance")
    public void Withdraw_WithoutEnoughBalance_ReturnsFalse() {
        Assertions.assertFalse(bAccount.withdraw(5000));
        Assertions.assertEquals(1000, bAccount.getBalance());
    }

    @Test
    @DisplayName("Test withdraw with negative value")
    public void Withdraw_NegativeValue_ThrowsError() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> bAccount.withdraw(-500));
    }

    @Test
    @DisplayName("Test deposit with positive value")
    public void Deposit_PositiveValue_AddsToAccount() {
        Assertions.assertEquals(1500, bAccount.deposit(500));

    }

    @Test
    @DisplayName("Test deposit with negative value")
    public void Deposit_NegativeValue_ThrowsError() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> bAccount.deposit(-500));
    }

    @Test
    @DisplayName("Test payment with valid arguments")
    public void Payment_ValidArguments_ReturnsValue() {
        Assertions.assertEquals(10000*(0.001*Math.pow((1+0.001), 12)/(Math.pow((1+0.001), 12)-1)), bAccount.payment(10000, 0.001, 12));
    }

    @ParameterizedTest
    @DisplayName("Test payment with invalid arguments")
    //I will use a CsvSource to test different invalid arguments, these arguments should be arrays of 3 elements, the first element is the total amount, the second element is the interest and the third element is the number of payments
    @CsvSource({"-10000, 0.001, 12", "10000, -0.001, 12", "10000, 0.001, -12"})
    public void Payment_InvalidArguments_ThrowsError(double total_amount, double interest, int npayments) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> bAccount.payment(total_amount, interest, npayments));
    }

    @Test
    @DisplayName("Test pending with valid arguments and month bigger than zero")
    public void Pending_ValidArgumentsAndMoreThanZeroMonths_ReturnsValue() {
        double ant = bAccount.pending(10000, 0.001, 12, 1);
        Assertions.assertEquals(ant - (bAccount.payment(10000,0.001,12) - 0.001*ant), bAccount.pending(10000, 0.001, 12, 2));
    }

    @Test
    @DisplayName("Test pending with valid arguments and month equals to zero")
    public void Pending_ValidArgumentsAndZeroMonths_ReturnsSameAmount() {
        Assertions.assertEquals(10000, bAccount.pending(10000, 0.001, 12, 0));
    }

    @ParameterizedTest
    @DisplayName("Test pending with invalid arguments")
    @CsvSource({"-10000, 0.001, 12, 1", "10000, -0.001, 12, 1", "10000, 0.001, -12, 1", "10000, 0.001, 12, -1"})
    public void Pending_InvalidArguments_ThrowsError(double amount, double inte, int npayments, int month) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> bAccount.pending(amount, inte, npayments, month));
    }
}
