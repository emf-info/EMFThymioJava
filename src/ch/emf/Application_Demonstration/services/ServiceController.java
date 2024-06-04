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
import javafx.application.Platform;

/**
 *
 * @author YerlyT04
 */
public class ServiceController extends Thread implements IServiceControllerForController {

    private IControllerForServiceController ctrl;
    private final static XboxController xc = new XboxController();
    private volatile double leftMagnitude = 0.0;
    private volatile boolean running = false;
    private volatile int frequence = 0;
    private volatile int previousFrequence = 0;
    private volatile double direction = 0.0;
    private volatile double previousDirection = 0.0;

    @Override
    public boolean connect() {
        if (!xc.isConnected()) {
            xc.release();
        }
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

        setRefController(Controller.getInstance());

        return true;

    }

    @Override
    public void run() {
        running = true;
        while (running) {
            if (previousFrequence != frequence) {
                ctrl.playSound(frequence);
                frequence = 0;
            }
            double intensity = leftMagnitude;
            int leftSpeed = 0;
            int rightSpeed = 0;
            if (intensity > 0.6) {
                double angle = direction - 90;
                if (angle < 0) {
                    angle += 360;
                }
                if (angle <= 180 || angle >= 360) {
                    intensity *= -1;
                }
                double cosValue = Math.cos(Math.toRadians(angle));

                leftSpeed = (int) (intensity * 500 + cosValue * intensity * 500);
                rightSpeed = (int) (intensity * 500 - cosValue * intensity * 500);

                leftSpeed = Math.max(-500, Math.min(500, leftSpeed));
                rightSpeed = Math.max(-500, Math.min(500, rightSpeed));

            }
            ctrl.moveThymio(leftSpeed, rightSpeed);
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(ServiceController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @Override
    public void disconnect() throws Exception {
        try {
            xc.release();
            running = false;
            ctrl = null;
        } catch (Exception e) {
            throw e;
        }
    }

    public void setRefController(IControllerForServiceController ctrl) {
        this.ctrl = ctrl;
    }

}
