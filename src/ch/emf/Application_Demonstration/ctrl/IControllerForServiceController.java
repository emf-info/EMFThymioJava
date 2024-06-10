/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ch.emf.Application_Demonstration.ctrl;

/**
 * Interface including controller service controller methods
 * @author YerlyT04
 * @version 1.0
 */
public interface IControllerForServiceController {

    public boolean playSound(int frequence);

    public boolean moveThymio(int motorLeftSpeed, int motorRightSpeed);
    
    public void sendErrorMessage(String error);
}
