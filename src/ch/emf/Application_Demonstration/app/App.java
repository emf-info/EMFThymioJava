/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ch.emf.Application_Demonstration.app;

import ch.emf.Application_Demonstration.ctrl.Controller;
import ch.emf.Application_Demonstration.view.View;

/**
 *
 * @author YerlyT04
 * @version 1.0
 */
/**
 * The App class serves as the entry point for the application. It initializes
 * the View and Controller components and starts the Controller.
 */
public class App {

    /**
     * The main method which serves as the entry point for the application. It
     * initializes the View and Controller, sets up their references to each
     * other, and starts the Controller.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //initialization of the view and the controller
        View view = new View();
        Controller ctrl = Controller.getInstance();
        ctrl.setRefView(view);
        view.setRefController(ctrl);
        
        //Start of the application
        ctrl.Start();
    }

}
