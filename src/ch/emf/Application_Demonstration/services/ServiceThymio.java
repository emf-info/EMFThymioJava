/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ch.emf.Application_Demonstration.services;

import ch.emf.Application_Demonstration.ctrl.Controller;
import ch.emf.Application_Demonstration.ctrl.IControllerForServiceThymio;
import ch.emf.Thymio_Java_Connnect.services.ServiceThymioOrders;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author YerlyT04
 */
public class ServiceThymio implements IServiceThymioForController {

    private IControllerForServiceThymio ctrl;
    private volatile ServiceThymioOrders thymio;
    private volatile boolean running;

    public ServiceThymio() {
        thymio = new ServiceThymioOrders();
    }

    @Override
    public boolean connect(String ThymioName) throws Exception {
        thymio.connect(ThymioName);
        setRefController(Controller.getInstance());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            throw ex;
        }
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

    @Override
    public boolean playSound(int frequence) {
        return thymio.playSound(frequence);
    }

    @Override
    public boolean moveThymio(int motorLeftSpeed, int motorRightSpeed) {
        return thymio.moveThymio(motorLeftSpeed, motorRightSpeed);
    }

    @Override
    public boolean turnLedOn(int red, int green, int blue, String led) {
        return thymio.turnLedOn(red, green, blue, led);
    }

    public void setRefController(IControllerForServiceThymio ctrl) {
        this.ctrl = ctrl;
    }

}
