import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TaxEstimatorTest {
    private TaxEstimator estimator;
    
    @BeforeEach
    public void setup() {
        estimator = new TaxEstimator();
    }
    
    @Test
    public void testNegativeIncome() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            estimator.estimateTax(-1);
        });
    }
    
    @Test
    public void testIncomeUnder1000() {
        int tax = estimator.estimateTax(999);
        Assertions.assertEquals(0, tax);
    }
    
    @Test
    public void testIncomeBetween1000And3000() {
        int tax = estimator.estimateTax(1001);
        Assertions.assertEquals(101, tax);
    }
    
    @Test
    public void testEstimateTaxWithIncomeOver3000() {
        int tax = estimator.estimateTax(4000);
        Assertions.assertEquals(3600, tax);
    }
}
