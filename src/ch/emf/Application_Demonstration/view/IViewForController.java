/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ch.emf.Application_Demonstration.view;

/**
 * Interface including controller view methods
 *
 * @author YerlyT04
 * @version 1.0
 *
 */
public interface IViewForController {

    public void start();

    public void thymioConnected();

    public void controllerConnected();

    public void displayError(String error);

}
