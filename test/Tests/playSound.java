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
public class playSound {

    private static ServiceThymioOrders thymio = new ServiceThymioOrders();

    public playSound() {
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

    // PAS  IN                  OUT 
    // ===================================================================================
    // 1    -2147483648         Thymio does not play a sound / false
    // 2    -2147483647         Thymio does not play a sound / false
    // 3    -1                  Thymio does not play a sound / false
    // 4    0                   Thymio does not play a sound / false
    // 5    1                   Thymio does not play a sound / false
    // 6    2147483646          Thymio does not play a sound / false
    // 7    2147483647          Thymio does not play a sound / false
    // 8    424                 Thymio plays a sound at a frequence of 424hz / true
    // 9    16                  Thymio plays a sound at the maximal frequence  / true
    // 10   20000               Thymio plays a sound at the minimal frequence / true
    // 11   10                  Thymio does not play a sound / false
    // 12   -103                Thymio does not play a sound / false
    // 13   21531               Thymio does not play a sound / false
    @Test
    public void PAS_1_playSound_MIN() {
        boolean result = thymio.playSound(Integer.MIN_VALUE);
        assertFalse(result);
        System.out.println("Expected: false, Actual: " + result);
    }

    @Test
    public void PAS_2_playSound_MIN1() {
        boolean result = thymio.playSound(Integer.MIN_VALUE + 1);
        assertFalse(result);
        System.out.println("Expected: false, Actual: " + result);
    }

    @Test
    public void PAS_3_playSound_Negative1() {
        boolean result = thymio.playSound(-1);
        assertFalse(result);
        System.out.println("Expected: false, Actual: " + result);
    }

    @Test
    public void PAS_4_playSound_0() {
        boolean result = thymio.playSound(0);
        assertFalse(result);
        System.out.println("Expected: false, Actual: " + result);
    }

    @Test
    public void PAS_5_playSound_1() {
        boolean result = thymio.playSound(1);
        assertFalse(result);
        System.out.println("Expected: false, Actual: " + result);
    }

    @Test
    public void PAS_6_playSound_MAX1() {
        boolean result = thymio.playSound(Integer.MAX_VALUE - 1);
        assertFalse(result);
        System.out.println("Expected: false, Actual: " + result);
    }

    @Test
    public void PAS_7_playSound_MAX() {
        boolean result = thymio.playSound(Integer.MAX_VALUE);
        assertFalse(result);
        System.out.println("Expected: false, Actual: " + result);
    }

    @Test
    public void PAS_8_playSound_424() {
        boolean result = thymio.playSound(424);
        assertTrue(result);
        System.out.println("Expected: true, Actual: " + result);
    }

    @Test
    public void PAS_9_playSound_16() {
        boolean result = thymio.playSound(16);
        assertTrue(result);
        System.out.println("Expected: true, Actual: " + result);
    }

    @Test
    public void PAS_10_playSound_20000() {
        boolean result = thymio.playSound(20000);
        assertTrue(result);
        System.out.println("Expected: true, Actual: " + result);
    }

    @Test
    public void PAS_11_playSound_10() {
        boolean result = thymio.playSound(10);
        assertFalse(result);
        System.out.println("Expected: false, Actual: " + result);
    }

    @Test
    public void PAS_12_playSound_NEG103() {
        boolean result = thymio.playSound(-103);
        assertFalse(result);
        System.out.println("Expected: false, Actual: " + result);
    }

    @Test
    public void PAS_13_playSound_21531() {
        boolean result = thymio.playSound(21531);
        assertFalse(result);
        System.out.println("Expected: false, Actual: " + result);
    }

}
