/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ch.emf.Application_Demonstration.services;

import ch.emf.Thymio_Java_Connnect.services.ServiceThymioOrders;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author YerlyT04
 */
public class ServiceThymio implements IServiceThymioForController {

    private ServiceThymioOrders thymio;

    public ServiceThymio() {
        thymio = new ServiceThymioOrders();
    }

    @Override
    public boolean connect(String ThymioName) throws Exception {
        thymio.connect(ThymioName);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            throw ex;
        }
        if (ServiceThymioOrders.isConnected && !ServiceThymioOrders.isReady) {
            disconnect();
            Thread.sleep(5000);
            thymio.connect(ThymioName);
        } else if (!ServiceThymioOrders.isConnected && !ServiceThymioOrders.isReady) {
            return false;
        }
        return true;
    }

    @Override
    public void disconnect() throws Exception {
        try {
            thymio.disconnect();
            Thread.sleep(5000);
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

}
