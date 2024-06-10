/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ch.emf.Application_Demonstration.ctrl;

/**
 * Interface including View controller methods
 * @author YerlyT04
 * @version 1.0
 */
public interface IControllerForView {

    public boolean connectController();

    public boolean connectThymio(String ThymioName);

    public void disconnectController();

    public void disconnectThymio();

    public boolean turnLedOn(int red, int green, int blue, String led);
}
