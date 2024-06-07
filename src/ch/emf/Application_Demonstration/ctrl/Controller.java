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
 *
 * @author YerlyT04
 */
public class Controller implements IControllerForView, IControllerForServiceController, IControllerForServiceThymio {

    private IServiceControllerForController controller;
    private IServiceThymioForController thymio;
    private IViewForController view;

    private Controller() {
        this.thymio = null;
        this.controller = null;
        this.view = null;
    }

    @Override
    public boolean connectController() {
        synchronized (getInstance()) {
            controller = new ServiceController();
            boolean ok = controller.connect();
            return ok;
        }
    }

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

    @Override
    public boolean turnLedOn(int red, int green, int blue, String led) {
        synchronized (getInstance()) {
            boolean ok = thymio.turnLedOn(red, green, blue, led);
            if (!ok) {
                view.displayError("The Thymio has been disconnected");
            }
            return ok;
        }

    }

    @Override
    public boolean playSound(int frequence) {
        synchronized (getInstance()) {
            if (frequence != 0) {
                boolean ok = thymio.playSound(frequence);
            if (!ok) {
                view.displayError("The Thymio has been disconnected");
            }
                return ok;
            } else {
                return true;
            }
        }
    }

    @Override
    public boolean moveThymio(int motorLeftSpeed, int motorRightSpeed) {
        synchronized (getInstance()) {
            boolean ok = thymio.moveThymio(motorLeftSpeed, motorRightSpeed);
            if (!ok) {
                view.displayError("The Thymio has been disconnected");
            }
            return ok;
        }
    }

    @Override
    public void sendErrorMessage(String error) {
        synchronized (getInstance()) {
            view.displayError(error);
        }
    }

    public void setRefView(IViewForController view) {
        synchronized (getInstance()) {
            this.view = view;
        }
    }

    public void Start() {
        synchronized (getInstance()) {
            view.start();
        }
    }

    public synchronized static Controller getInstance() {
        return Singleton_Controller.INSTANCE;
    }

    private static class Singleton_Controller {

        private static final Controller INSTANCE = new Controller();
    }

}
