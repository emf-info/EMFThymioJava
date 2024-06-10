/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ch.emf.Application_Demonstration.services;

import ch.emf.Application_Demonstration.ctrl.Controller;
import ch.emf.Application_Demonstration.ctrl.IControllerForServiceThymio;
import ch.emf.Thymio_Java_Connnect.services.ServiceThymioOrders;

/**
 * The ServiceThymio class manages the connection and interaction with a Thymio
 * robot. It implements the IServiceThymioForController interface. This class
 * handles the connection, disconnection, and various control operations for the
 * Thymio robot.
 *
 * @author YerlyT04
 * @version 1.0
 */
public class ServiceThymio implements IServiceThymioForController {

    /**
     * Attributes of the class
     */
    private IControllerForServiceThymio ctrl;
    private volatile ServiceThymioOrders thymio;
    private volatile boolean running;

    /**
     * Constructs a ServiceThymio instance and initializes the
     * ServiceThymioOrders object.
     */
    public ServiceThymio() {
        thymio = new ServiceThymioOrders();
    }

    /**
     * Connects to the Thymio robot. It sets the reference to the main
     * controller, checks the connection status, and updates the running state.
     *
     * @param ThymioName the name of the Thymio robot to connect to
     * @return true if the connection was successful, false otherwise
     * @throws Exception if an error occurs during the connection process
     */
    @Override
    public boolean connect(String ThymioName) throws Exception {
        thymio.connect(ThymioName);
        setRefController(Controller.getInstance());
        try {
            //wait the result of the connection
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            throw ex;
        }
        //if the isConnected or isReady equals false the connection didn't work well
        if (ServiceThymioOrders.isConnected && !ServiceThymioOrders.isReady) {
            ctrl.sendErrorMessage("Thymio not recognized");
            disconnect();
            return false;
        } else if (!ServiceThymioOrders.isConnected && !ServiceThymioOrders.isReady) {
            ctrl.sendErrorMessage("Please open the Thymio suite app before trying to connect to the Thymio and check that the Thymio is connected.");
            ctrl = null;
            return false;
        }
        running = true;
        return true;
    }

    /**
     * Disconnects from the Thymio robot. It updates the running state and sets
     * the controller reference to null.
     *
     * @throws Exception if an error occurs during the disconnection process
     */
    @Override
    public void disconnect() throws Exception {
        try {
            running = false;
            thymio.disconnect();
            ctrl = null;
        } catch (InterruptedException ex) {
            throw new Exception("Disconnection failed");
        }
    }

    /**
     * Plays a sound on the Thymio robot.
     *
     * @param frequence the frequency of the sound to play
     * @return true if the sound was played successfully, false otherwise
     */
    @Override
    public boolean playSound(int frequence) {
        return thymio.playSound(frequence);
    }

    /**
     * Moves the Thymio robot.
     *
     * @param motorLeftSpeed the speed of the left motor
     * @param motorRightSpeed the speed of the right motor
     * @return true if the movement command was sent successfully, false
     * otherwise
     */
    @Override
    public boolean moveThymio(int motorLeftSpeed, int motorRightSpeed) {
        return thymio.moveThymio(motorLeftSpeed, motorRightSpeed);
    }

    /**
     * Turns on an LED on the Thymio robot.
     *
     * @param red the red component of the LED color
     * @param green the green component of the LED color
     * @param blue the blue component of the LED color
     * @param led the identifier of the LED to turn on
     * @return true if the LED was turned on successfully, false otherwise
     */
    @Override
    public boolean turnLedOn(int red, int green, int blue, String led) {
        return thymio.turnLedOn(red, green, blue, led);
    }

    /**
     * Sets the reference to the main controller.
     *
     * @param ctrl the controller to set
     */
    public void setRefController(IControllerForServiceThymio ctrl) {
        this.ctrl = ctrl;
    }

}
