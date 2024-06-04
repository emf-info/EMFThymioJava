/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ch.emf.Application_Demonstration.app;
import ch.emf.Application_Demonstration.ctrl.Controller;
import ch.emf.Application_Demonstration.view.View;
import ch.emf.Application_Demonstration.services.ServiceController;
import ch.emf.Application_Demonstration.services.ServiceThymio;

/**
 *
 * @author YerlyT04
 */



public class App {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        View view = new View();
        Controller ctrl = Controller.getInstance();
        ctrl.setRefView(view);
        view.setRefController(ctrl);
        
        ctrl.Start();
    }
    
}
