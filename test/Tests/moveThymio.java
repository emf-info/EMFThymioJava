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
public class moveThymio{

    private static ServiceThymioOrders thymio = new ServiceThymioOrders();

    public moveThymio() {

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
            thymio.moveThymio(0, 0);
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
            Logger.getLogger(moveThymio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // PAS  IN                                                       OUT 
    // =====================================================================================================
    // 1    -2147483648, -2147483648                                 Thymio does not move / false 
    // 2    -2147483647, -2147483647                                 Thymio does not move / false
    // 3    -1, 0                                                    Thymio turns left / true
    // 4    0, 1                                                     Thymio turns left / true
    // 5    2147483646, 2147483646                                   Thymio does not move / false
    // 6    2147483647, 2147483647                                   Thymio does not move / false
    // 7    100, 100                                                 Thymio move forwards / true
    // 8    0, 100                                                   Thymio turns left / true
    // 9    100, 0                                                   Thymio turns right / true
    // 10   1000, 1000                                               Thymio does not move / false
    // 11   -100, -100                                               Thymio move backwards / true
    // 12   0, -100                                                  Thymio turns right / true
    // 13   -100, 0                                                  Thymio turns left / true
    // 14   -1000, -1000                                             Thymio does not move / false
    // 15   100, -100                                                Thymio turns on itself / true
    // 16   100, 200                                                 Thymio moves slowly to the left / true
    // 17   200, 100                                                 Thymio moves slowly to the right / true
    // 18   0, 0                                                     Thymio stops itself / true
    @Test
    public void PAS_1_moveThymio_MIN_MIN() {
        boolean result = thymio.moveThymio(Integer.MIN_VALUE, Integer.MIN_VALUE);
        assertFalse(result);
        System.out.println("Expected: false, Actual: " + result);
    }

    @Test
    public void PAS_2_moveThymio_MIN1_MIN1() {
        boolean result = thymio.moveThymio(Integer.MIN_VALUE + 1, Integer.MIN_VALUE + 1);
        assertFalse(result);
        System.out.println("Expected: false, Actual: " + result);
    }

    @Test
    public void PAS_3_moveThymio_Negative1_0() {
        boolean result = thymio.moveThymio(-1, 0);
        assertTrue(result);
        System.out.println("Expected: true, Actual: " + result);
    }

    @Test
    public void PAS_4_moveThymio_0_1() {
        boolean result = thymio.moveThymio(0, 1);
        assertTrue(result);
        System.out.println("Expected: true, Actual: " + result);
    }

    @Test
    public void PAS_5_moveThymio_MAX1_MAX1() {
        boolean result = thymio.moveThymio(Integer.MAX_VALUE - 1, Integer.MAX_VALUE - 1);
        assertFalse(result);
        System.out.println("Expected: false, Actual: " + result);
    }

    @Test
    public void PAS_6_moveThymio_MAX_MAX() {
        boolean result = thymio.moveThymio(Integer.MAX_VALUE, Integer.MAX_VALUE);
        assertFalse(result);
        System.out.println("Expected: false, Actual: " + result);
    }

    @Test
    public void PAS_7_moveThymio_100_100() {
        boolean result = thymio.moveThymio(100, 100);
        assertTrue(result);
        System.out.println("Expected: true, Actual: " + result);
    }

    @Test
    public void PAS_8_moveThymio_0_100() {
        boolean result = thymio.moveThymio(0, 100);
        assertTrue(result);
        System.out.println("Expected: true, Actual: " + result);
    }

    @Test
    public void PAS_9_moveThymio_100_0() {
        boolean result = thymio.moveThymio(100, 0);
        assertTrue(result);
        System.out.println("Expected: true, Actual: " + result);
    }

    @Test
    public void PAS_10_moveThymio_1000_1000() {
        boolean result = thymio.moveThymio(1000, 1000);
        assertFalse(result);
        System.out.println("Expected: false, Actual: " + result);
    }

    @Test
    public void PAS_11_moveThymio_NEG100_NEG100() {
        boolean result = thymio.moveThymio(-100, -100);
        assertTrue(result);
        System.out.println("Expected: true, Actual: " + result);
    }

    @Test
    public void PAS_12_moveThymio_0_NEG100() {
        boolean result = thymio.moveThymio(0, -100);
        assertTrue(result);
        System.out.println("Expected: true, Actual: " + result);
    }

    @Test
    public void PAS_13_moveThymio_NEG100_0() {
        boolean result = thymio.moveThymio(-100, 0);
        assertTrue(result);
        System.out.println("Expected: true, Actual: " + result);
    }

    @Test
    public void PAS_14_moveThymio_NEG1000_NEG1000() {
        boolean result = thymio.moveThymio(-1000, -1000);
        assertFalse(result);
        System.out.println("Expected: false, Actual: " + result);
    }

    @Test
    public void PAS_15_moveThymio_100_NEG100() {
        boolean result = thymio.moveThymio(100, -100);
        assertTrue(result);
        System.out.println("Expected: true, Actual: " + result);
    }

    @Test
    public void PAS_16_moveThymio_100_200() {
        boolean result = thymio.moveThymio(100, 200);
        assertTrue(result);
        System.out.println("Expected: true, Actual: " + result);
    }

    @Test
    public void PAS_17_moveThymio_200_100() {
        boolean result = thymio.moveThymio(200, 100);
        assertTrue(result);
        System.out.println("Expected: true, Actual: " + result);
    }

    @Test
    public void PAS_18_moveThymio_0_0() {
        boolean result = thymio.moveThymio(0, 0);
        assertTrue(result);
        System.out.println("Expected: true, Actual: " + result);
    }
}
