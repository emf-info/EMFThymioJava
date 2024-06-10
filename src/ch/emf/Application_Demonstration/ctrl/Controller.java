/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ch.emf.Application_Demonstration.ctrl;

import ch.emf.Application_Demonstration.view.IViewForController;
import ch.emf.Application_Demonstration.services.IServiceControllerForController;
import ch.emf.Application_Demonstration.services.IServiceThymioForController;
import ch.emf.Application_Demonstration.services.ServiceController;
import ch.emf.Application_Demonstration.services.ServiceThymio;

/**
 * The Controller class is responsible for managing the interactions between the
 * view and the services. It implements multiple interfaces to provide various
 * functionalities to the view and service layers. This class follows the
 * Singleton design pattern to ensure only one instance of the controller
 * exists.
 *
 * @author YerlyT04
 * @version 1.0
 */
public class Controller implements IControllerForView, IControllerForServiceController, IControllerForServiceThymio {

    /**
     * xbox controller communication service
     */
    private IServiceControllerForController controller;

    /**
     * Thymio communication service
     */
    private IServiceThymioForController thymio;

    /**
     * View of the application
     */
    private IViewForController view;

    /**
     * Private constructor to prevent instantiation. Initializes the service and
     * view references to null.
     */
    private Controller() {
        this.thymio = null;
        this.controller = null;
        this.view = null;
    }

    /**
     * Connects to the service controller.
     *
     * @return true if the connection was successful, false otherwise
     */
    @Override
    public boolean connectController() {
        synchronized (getInstance()) {
            controller = new ServiceController();
            boolean ok = controller.connect();
            return ok;
        }
    }

    /**
     * Connects to the Thymio robot with the given name.
     *
     * @param ThymioName the name of the Thymio robot
     * @return true if the connection was successful, false otherwise
     */
    @Override
    public boolean connectThymio(String ThymioName) {
        synchronized (getInstance()) {
            boolean ok = false;
            try {
                thymio = new ServiceThymio();
                ok = thymio.connect(ThymioName);
            } catch (Exception ex) {
                view.displayError(ex.getMessage());
            }
            return ok;
        }
    }

    /**
     * Disconnects from the service controller.
     */
    @Override
    public void disconnectController() {
        synchronized (getInstance()) {
            try {
                controller.disconnect();
                controller = null;
            } catch (Exception ex) {
                view.displayError(ex.getMessage());
            }
        }
    }

    /**
     * Disconnects from the Thymio robot.
     */
    @Override
    public void disconnectThymio() {
        synchronized (getInstance()) {
            try {
                thymio.disconnect();
                thymio = null;
            } catch (Exception ex) {
                view.displayError(ex.getMessage());
            }
        }
    }

    /**
     * Turns on the LED of the Thymio robot with the specified color and LED
     * type.
     *
     * @param red the intensity of the red color
     * @param green the intensity of the green color
     * @param blue the intensity of the blue color
     * @param led the type of LED to turn on
     * @return true if the operation was successful, false otherwise
     */
    @Override
    public boolean turnLedOn(int red, int green, int blue, String led) {
        synchronized (getInstance()) {
            boolean ok = thymio.turnLedOn(red, green, blue, led);

            if (!ok) {
                view.displayError("The led cannot be lit in this color");
            }
            return ok;
        }

    }

    /**
     * Plays a sound with the specified frequency on the Thymio robot.
     *
     * @param frequence the frequency of the sound
     * @return true if the operation was successful, false otherwise
     */
    @Override
    public boolean playSound(int frequence) {
        synchronized (getInstance()) {
            if (frequence != 0) {
                boolean ok = thymio.playSound(frequence);
                if (!ok) {
                    view.displayError("The sound was impossible to play");
                }
                return ok;
            } else {
                return true;
            }
        }
    }

    /**
     * Moves the Thymio robot with the specified motor speeds.
     *
     * @param motorLeftSpeed the speed of the left motor
     * @param motorRightSpeed the speed of the right motor
     * @return true if the operation was successful, false otherwise
     */
    @Override
    public boolean moveThymio(int motorLeftSpeed, int motorRightSpeed) {
        synchronized (getInstance()) {
            boolean ok = thymio.moveThymio(motorLeftSpeed, motorRightSpeed);
            if (!ok) {
                view.displayError("The Thymio couldnt move");
            }
            return ok;
        }
    }

    /**
     * Sends an error message to the view.
     *
     * @param error the error message to send
     */
    @Override
    public void sendErrorMessage(String error) {
        synchronized (getInstance()) {
            view.displayError(error);
        }
    }

    /**
     * Starts the view.
     */
    public void Start() {
        synchronized (getInstance()) {
            view.start();
        }
    }

    /**
     * Sets the reference to the view.
     *
     * @param view the view to set
     */
    public void setRefView(IViewForController view) {
        synchronized (getInstance()) {
            this.view = view;
        }
    }

    /**
     * Returns the singleton instance of the Controller.
     *
     * @return the singleton instance of the Controller
     */
    public synchronized static Controller getInstance() {
        return Singleton_Controller.INSTANCE;
    }

    /**
     * Inner static class responsible for holding the singleton instance of the
     * Controller class.
     */
    private static class Singleton_Controller {

        private static final Controller INSTANCE = new Controller();
    }

}
