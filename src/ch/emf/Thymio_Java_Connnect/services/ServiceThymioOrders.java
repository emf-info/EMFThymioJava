/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ch.emf.Thymio_Java_Connnect.services;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This Class provides methods to control a Thymio robot, including connecting
 * to it, disconnecting from it, moving it, playing sound, and turning on LED
 * lights.
 *
 * @author Tom Yerly
 * @version 1.0
 */
public class ServiceThymioOrders {

    /**
     * Indicates whether the connection to the Thymio was successful.
     */
    public static boolean isConnected = false;
    /**
     * Indicates whether the Thymio is Ready to execute orders.
     */
    public static boolean isReady = false;

    /**
     * Thread used to send message to the Thymio
     */
    private ServiceThymioSender thymioSender;

    /**
     * Name of the Thymio
     */
    private String thymioName;

    /**
     * Connects to the specified Thymio robot.
     *
     * @param thymioName The name of the Thymio robot to connect to.
     */
    public void connect(String thymioName) {
        this.thymioName = thymioName;
        thymioSender = new ServiceThymioSender(thymioName);
        thymioSender.start();
    }

    /**
     * Disconnects from the Thymio robot.
     *
     * @throws InterruptedException If the current thread is interrupted while
     * waiting for the ServiceThymioSender thread to terminate.
     */
    public void disconnect() throws InterruptedException {
        thymioSender.setRunning(false);
        isConnected = false;
        try {
            thymioSender.join(5000);
            if (thymioSender.isAlive()) {
                throw new InterruptedException("Disconnection failed");
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(ServiceThymioOrders.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
        System.gc();

    }

    /**
     * Moves the Thymio robot by setting the target speeds of its left and right
     * motors.
     *
     * @param motorLeftSpeed The target speed for the left motor (-500 to 500).
     * @param motorRightSpeed The target speed for the right motor (-500 to
     * 500).
     * @return true if the movement command was successfully sent; false
     * otherwise.
     */
    public boolean moveThymio(int motorLeftSpeed, int motorRightSpeed) {
        boolean ok = false;
        if (isConnected != false && isReady != false) {
            if ((motorLeftSpeed <= 500 && motorRightSpeed <= 500) && (motorLeftSpeed >= -500 && motorRightSpeed >= -500)) {
                ok = thymioSender.sendProgram("motor.left.target =" + motorLeftSpeed + "\nmotor.right.target =" + motorRightSpeed);
            }
        }
        return ok;
    }

    /**
     * Plays a sound on the Thymio robot.
     *
     * @param frequence The frequency of the sound to play (16 to 20000 Hz).
     * @return true if the sound command was successfully sent; false otherwise.
     */
    public boolean playSound(int frequence) {
        boolean ok = false;
        if (isConnected != false && isReady != false) {
            if (frequence >= 16 && frequence <= 20000) {
                ok = thymioSender.sendProgram("call sound.freq(" + frequence + ", 50)");
            }
        }
        return ok;

    }

    /**
     * Turns on the LED lights on the Thymio robot.
     *
     * @param red The intensity of the red LED (0 to 32).
     * @param green The intensity of the green LED (0 to 32).
     * @param blue The intensity of the blue LED (0 to 32).
     * @param led The location of the LED to turn on ("top", "bottom.left", or
     * "bottom.right").
     * @return true if the LED command was successfully sent; false otherwise.
     */
    public boolean turnLedOn(int red, int green, int blue, String led) {
        boolean ok = false;
        if (isConnected != false && isReady != false) {
            if (led != null) {
                if (led.equals("top") || led.equals("bottom.left") || led.equals("bottom.right")) {
                    if (red >= 0 && red <= 32 && green >= 0 && green <= 32 && blue >= 0 && blue <= 32 && red + green + blue > 0) {
                        ok = thymioSender.sendProgram("call leds." + led + "(" + red + "," + green + "," + blue + ")");
                    }
                } else {
                    thymioSender.sendProgram("call leds.top (0,0,0)");
                    thymioSender.sendProgram("call leds.bottom.left (0,0,0)");
                    thymioSender.sendProgram("call leds.bottom.right (0,0,0)");
                    ok = false;
                }
            }
        }
        return ok;
    }

}
