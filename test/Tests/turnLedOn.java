/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Tests;

import java.util.logging.Level;
import java.util.logging.Logger;
import ch.emf.Thymio_Java_Connnect.services.ServiceThymioOrders;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author YerlyT04
 */
public class turnLedOn {

    private static ServiceThymioOrders thymio = new ServiceThymioOrders();

    public turnLedOn() {
    }

    @BeforeClass
    public static void setUpClass() {
        try {
            thymio.connect("Thymio EMF1");
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Logger.getLogger(moveThymio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @AfterClass
    public static void tearDownClass() {
        try {
            thymio.disconnect();
        } catch (InterruptedException ex) {
            Logger.getLogger(moveThymio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(playSound.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // PAS  IN                                                OUT 
    // =====================================================================================================
    // 1    -2147483648, 0, 0, "top"                         Thymio does not light up the top LED / false 
    // 2    0, -2147483648, 0, "bottom.left"                  Thymio does not light up the bottom left LED / false
    // 3    0, 0, -2147483648, "bottom.right"                 Thymio does not light up the bottom right LED / false
    // 4    2147483647, 0, 0, "top"                           Thymio does not light up the top LED / false
    // 5    0, 2147483647, 0, "bottom.left"                   Thymio does not light up the bottom left LED / false
    // 6    0, 0, 2147483647, "bottom.right"                  Thymio does not light up the bottom right LED / false
    // 7    32, 0, 0, "top"                                   Thymio lights up the top LED in red / true
    // 8    0, 32, 0, "bottom.left"                           Thymio lights up the bottom left LED in green / true
    // 9    0, 0, 32, "bottom.right"                          Thymio lights up the bottom right LED in blue / true
    // 10   32, 0, 0, "test"                                  Thymio does not light up any LED / false
    // 11   -12, 0, 0, "top"                                  Thymio does not light up the top LED / false
    // 12   0, -12, 0, "bottom.left"                          Thymio does not light up the bottom left LED / false
    // 13   0, 0, -12, "bottom.right"                         Thymio does not light up the bottom right LED / false
    // 14   33, 0, 0, "top"                                   Thymio does not light up the top LED / false
    // 15   0, 33, 0, "top"                                   Thymio does not light up the top LED / false
    // 16   0, 0, 33, "top"                                   Thymio does not light up the top LED / false
    // 17   0, 0, 0, "top"                                    The top LED does not light up / false
    // 18   32, 0, 0, null                                    No LED lights up / false
    @Test
    public void PAS_1_turnLedOn_MIN_0_0_top() {
        boolean result = thymio.turnLedOn(Integer.MIN_VALUE, 0, 0, "top");
        assertFalse(result);
        System.out.println("Expected: false, Actual: " + result);
    }

    @Test
    public void PAS_2_turnLedOn_0_MIN_0_bottom_left() {
        boolean result = thymio.turnLedOn(0, Integer.MIN_VALUE, 0, "bottom.left");
        assertFalse(result);
        System.out.println("Expected: false, Actual: " + result);
    }

    @Test
    public void PAS_3_turnLedOn_0_0_MIN_bottom_right() {
        boolean result = thymio.turnLedOn(0, 0, Integer.MIN_VALUE, "bottom.right");
        assertFalse(result);
        System.out.println("Expected: false, Actual: " + result);
    }

    @Test
    public void PAS_4_turnLedOn_MAX_0_0_top() {
        boolean result = thymio.turnLedOn(Integer.MAX_VALUE, 0, 0, "top");
        assertFalse(result);
        System.out.println("Expected: false, Actual: " + result);
    }

    @Test
    public void PAS_5_turnLedOn_0_MAX_0_bottom_left() {
        boolean result = thymio.turnLedOn(0, Integer.MAX_VALUE, 0, "bottom.left");
        assertFalse(result);
        System.out.println("Expected: false, Actual: " + result);
    }

    @Test
    public void PAS_6_turnLedOn_0_0_MAX_bottom_right() {
        boolean result = thymio.turnLedOn(0, 0, Integer.MAX_VALUE, "bottom.right");
        assertFalse(result);
        System.out.println("Expected: false, Actual: " + result);
    }

    @Test
    public void PAS_7_turnLedOn_32_0_0_top() {
        boolean result = thymio.turnLedOn(32, 0, 0, "top");
        assertTrue(result);
        System.out.println("Expected: true, Actual: " + result);
    }

    @Test
    public void PAS_8_turnLedOn_0_32_0_bottom_left() {
        boolean result = thymio.turnLedOn(0, 32, 0, "bottom.left");
        assertTrue(result);
        System.out.println("Expected: true, Actual: " + result);
    }

    @Test
    public void PAS_9_turnLedOn_0_0_32_bottom_right() {
        boolean result = thymio.turnLedOn(0, 0, 32, "bottom.right");
        assertTrue(result);
        System.out.println("Expected: true, Actual: " + result);
    }

    @Test
    public void PAS_10_turnLedOn_32_0_0_test() {
        boolean result = thymio.turnLedOn(32, 0, 0, "test");
        assertFalse(result);
        System.out.println("Expected: false, Actual: " + result);
    }

    @Test
    public void PAS_11_turnLedOn_NEG12_0_0_top() {
        boolean result = thymio.turnLedOn(-12, 0, 0, "top");
        assertFalse(result);
        System.out.println("Expected: false, Actual: " + result);
    }

    @Test
    public void PAS_12_turnLedOn_0_NEG12_0_bottom_left() {
        boolean result = thymio.turnLedOn(0, -12, 0, "bottom.left");
        assertFalse(result);
        System.out.println("Expected: false, Actual: " + result);
    }

    @Test
    public void PAS_13_turnLedOn_0_0_NEG12_bottom_right() {
        boolean result = thymio.turnLedOn(0, 0, -12, "bottom.right");
        assertFalse(result);
        System.out.println("Expected: false, Actual: " + result);
    }

    @Test
    public void PAS_14_turnLedOn_33_0_0_top() {
        boolean result = thymio.turnLedOn(33, 0, 0, "top");
        assertFalse(result);
        System.out.println("Expected: false, Actual: " + result);
    }

    @Test
    public void PAS_15_turnLedOn_0_33_0_top() {
        boolean result = thymio.turnLedOn(0, 33, 0, "top");
        assertFalse(result);
        System.out.println("Expected: false, Actual: " + result);
    }

    @Test
    public void PAS_16_turnLedOn_0_0_33_top() {
        boolean result = thymio.turnLedOn(0, 0, 33, "top");
        assertFalse(result);
        System.out.println("Expected: false, Actual: " + result);
    }

    @Test
    public void PAS_17_turnLedOn_0_0_0_top() {
        boolean result = thymio.turnLedOn(0, 0, 0, "top");
        assertFalse(result);
        System.out.println("Expected: false, Actual: " + result);
    }

    @Test
    public void PAS_18_turnLedOn_32_0_0_null() {
        boolean result = thymio.turnLedOn(32, 0, 0, null);
        assertFalse(result);
        System.out.println("Expected: false, Actual: " + result);
    }

}
