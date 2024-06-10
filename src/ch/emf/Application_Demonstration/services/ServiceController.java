/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ch.emf.Application_Demonstration.services;

import ch.aplu.xboxcontroller.*;
import ch.emf.Application_Demonstration.ctrl.Controller;
import ch.emf.Application_Demonstration.ctrl.IControllerForServiceController;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The ServiceController class handles the interaction with an Xbox controller.
 * It extends the Thread class and implements the
 * IServiceControllerForController interface. This class manages the connection,
 * input handling, and disconnection of the controller. It follows a
 * multithreaded approach to handle controller inputs asynchronously.
 *
 * @author YerlyT04
 * @version 1.0
 */
public class ServiceController extends Thread implements IServiceControllerForController {

    /**
     * The objects of the class
     */
    private IControllerForServiceController ctrl;
    private static XboxController xc;
    private final Object lock = new Object();
    private volatile double leftMagnitude = 0.0;
    private volatile boolean running = false;
    private volatile int frequence = 0;
    private volatile int previousFrequence = 0;
    private volatile double direction = 0.0;

    /**
     * Connects to the Xbox controller and starts the ServiceController thread.
     *
     * @return true if the connection and thread start were successful, false
     * otherwise
     */
    @Override
    public boolean connect() {
        synchronized (lock) {
            try {
                this.start();
            } catch (Exception e) {
                Logger.getLogger(ServiceController.class.getName()).log(Level.SEVERE, "Failed to start the ServiceController thread", e);
                return false;
            }
            return true;
        }
    }

    /**
     * Disconnects the Xbox controller and stops the ServiceController thread.
     *
     * @throws Exception if an error occurs during disconnection
     */
    @Override
    public void disconnect() throws Exception {
        running = false;
        this.interrupt();
        try {
            this.join(1000);
        } catch (InterruptedException e) {
            Logger.getLogger(ServiceController.class.getName()).log(Level.SEVERE, "Failed to join the ServiceController thread", e);
        }
    }

    /**
     * The run method contains the main logic of the ServiceController thread.
     * It loads the Xbox controller, sets the reference to the main controller,
     * and continuously handles controller inputs while the controller is
     * running.
     */
    @Override
    public void run() {

        synchronized (lock) {
            running = true;
            try {
                loadXboxController();
                setRefController(Controller.getInstance());

                while (running) {
                    handleControllerInputs();

                    // wait to prevent too much requests
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        running = false;
                        Thread.currentThread().interrupt();
                    }

                    //Check that the controller is still connected
                    if (!xc.isConnected()) {
                        if (ctrl != null) {
                            ctrl.sendErrorMessage("The controller is disconnected.");
                        }
                        running = false;
                    }
                }
            } finally {
                cleanUp();
            }
        }
    }

    /**
     * Loads the Xbox controller and sets up the listener for controller events.
     */
    private void loadXboxController() {
        synchronized (lock) {
            if (xc != null) {
                if (!xc.isConnected()) {
                    xc.release();
                }
            }
            try {
                xc = new XboxController();

                //Set the listeners of the controller
                xc.addXboxControllerListener(new XboxControllerAdapter() {

                    @Override
                    public void buttonY(boolean pressed) {
                        if (pressed) {
                            frequence = 20;
                        }
                    }

                    @Override
                    public void buttonX(boolean pressed) {
                        if (pressed) {
                            frequence = 500;
                        }
                    }

                    @Override
                    public void buttonA(boolean pressed) {
                        if (pressed) {
                            frequence = 1000;
                        }
                    }

                    @Override
                    public void buttonB(boolean pressed) {
                        if (pressed) {
                            frequence = 10000;
                        }
                    }

                    @Override
                    public void leftThumbMagnitude(double magnitude) {
                        leftMagnitude = magnitude;
                    }

                    @Override
                    public void leftThumbDirection(double angle) {
                        direction = angle;
                    }
                });
            } catch (Exception e) {

            }

        }

    }

    /**
     * Handles the inputs from the Xbox controller. It updates the Thymio
     * robot's movements and sounds based on the controller's inputs.
     */
    private void handleControllerInputs() {
        if (previousFrequence != frequence) {
            ctrl.playSound(frequence);
            previousFrequence = frequence;
            frequence = 0;
        }
        double intensity = leftMagnitude;
        int leftSpeed = 0;
        int rightSpeed = 0;
        //check that the joystick has moved enough
        if (intensity > 0.6) {
            double angle = direction - 90;
            if (angle < 0) {
                angle += 360;
            }
            if (angle <= 180 || angle >= 360) {
                intensity *= -1;
            }
            double cosValue = Math.cos(Math.toRadians(angle));

            //define the value of the motors of the Thymio
            leftSpeed = (int) (intensity * 500 + cosValue * intensity * 500);
            rightSpeed = (int) (intensity * 500 - cosValue * intensity * 500);

            leftSpeed = Math.max(-500, Math.min(500, leftSpeed));
            rightSpeed = Math.max(-500, Math.min(500, rightSpeed));
        }
        if (ctrl != null) {
            ctrl.moveThymio(leftSpeed, rightSpeed);
        }
    }

    /**
     * Cleans up resources by releasing the Xbox controller and resetting the
     * main controller's reference.
     */
    private void cleanUp() {
        synchronized (lock) {
            try {
                if (xc != null) {
                    xc.release();
                }
                if (ctrl != null) {
                    ctrl.moveThymio(0, 0);
                    ctrl = null;
                }
            } catch (Exception e) {
                Logger.getLogger(ServiceController.class
                        .getName()).log(Level.SEVERE, "Failed to clean up resources", e);
            }
        }

    }

    /**
     * Sets the reference to the main controller.
     *
     * @param ctrl the controller to set
     */
    public void setRefController(IControllerForServiceController ctrl) {
        this.ctrl = ctrl;
    }
}
