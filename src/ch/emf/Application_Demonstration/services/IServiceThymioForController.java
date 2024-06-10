/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ch.emf.Application_Demonstration.services;

/**
 * Interface including controller Thymio service methods
 * @author YerlyT04
 * @version 1.0
 */
public interface IServiceThymioForController {
        
    public boolean connect(String ThymioName) throws Exception;
    
    public void disconnect() throws Exception;
    
    public boolean playSound(int frequence);
    
    public boolean moveThymio(int motorLeftSpeed, int motorRightSpeed);
    
    public boolean turnLedOn(int red, int green, int blue, String led);
    

}
