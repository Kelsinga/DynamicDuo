package dynamicduo;

import dynamicduo.ClassAssignmentAvans;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;


public class ClassAssignmentAvansTest {

    public ClassAssignmentAvans classAssignmentAvans = null;

    @BeforeEach
    void init(){
        classAssignmentAvans = new ClassAssignmentAvans();
    }

    @AfterEach
    void endTest(){
        classAssignmentAvans = null;
    }

    @ParameterizedTest(name = "{index} calculateShippingCosts: {0} , typeOfShippingCosts: {1} , totalPrice: {2} , expectedResult: {3}")
    @CsvSource({
            "false,   test,   100, 0",
            "true,    Ground,   2000, 0",
            "true,  Ground, 100, 100",
            "true,  InStore, 100, 50",
            "true,  NextDayAir, 100, 250",
            "true,  SecondDayAir, 100, 125",
            "true,  test, 101, 0"
    })
     void testCase(Boolean calculateShippingCosts, String typeOfShippingCosts, double totalPrice, double expectedResult){
        double result = classAssignmentAvans.ShippingCosts(calculateShippingCosts, typeOfShippingCosts, totalPrice );
        assertEquals(expectedResult, result, "calculateShippingCosts " + calculateShippingCosts + " typeOfShippingCosts " + typeOfShippingCosts + " totalPrice " + totalPrice);
    }
}
