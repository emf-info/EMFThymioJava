/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package ch.emf.Application_Demonstration.view;

import ch.emf.Application_Demonstration.ctrl.IControllerForView;
import ch.emf.Thymio_Java_Connnect.services.ServiceThymioOrders;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author YerlyT04
 */
public class View implements Initializable, IViewForController {

    private IControllerForView ctrl;

    private Scene principalScene;
    private Stage mainStage;
    private ColorPicker colorPicker;
    @FXML
    private Button btnLed;
    @FXML
    private Button btnDisconnectController;
    @FXML
    private Button btnDisconnectThymio;
    @FXML
    private Button btnConnectController;
    @FXML
    private Button btnConnectThymio;
    @FXML
    private ColorPicker clpLed;
    @FXML
    private ComboBox<String> cbbLed;
    @FXML
    private TextField txtController;
    @FXML
    private TextField txtThymio;

    private ArrayList<String> lstLeds;

    private boolean isControllerConnected;
    private boolean isThymioConnected;

    public View() {
        this.ctrl = null;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @Override
    public void start() {
        Platform.startup(() -> {
            try {
                mainStage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ch/emf/Application_Demonstration/view/view.fxml"));
                fxmlLoader.setControllerFactory(type -> this);
                Parent root = fxmlLoader.load();
                principalScene = new Scene(root);
                mainStage.setScene(principalScene);
                mainStage.setTitle("Demonsration application");
                mainStage.setResizable(false);
                mainStage.show();

                mainStage.setOnCloseRequest((WindowEvent e) -> {
                    if (isControllerConnected) {
                        disconnectController(new ActionEvent());
                    }
                    if (isThymioConnected) {
                        disconnectThymio(new ActionEvent());
                    }
                    Platform.exit();
                    System.exit(0);
                });
            } catch (IOException ex) {
                System.out.println("Can't start the IHM because: " + ex);
                Platform.exit();
            }
            lstLeds = new ArrayList<>();
            lstLeds.add("top");
            lstLeds.add("left");
            lstLeds.add("right");
            cbbLed.getItems().addAll(lstLeds);

            isControllerConnected = false;
            isThymioConnected = false;
        });
    }

    @Override
    public void thymioConnected() {
        synchronized (ctrl) {
            Platform.runLater(() -> {
                thymioConnectedState();
            });
        }

    }

    @Override
    public void controllerConnected() {
        synchronized (ctrl) {
            Platform.runLater(() -> {
                controllerConnectedState();
            });
        }
    }

    @Override
    public void displayError(String error) {
        synchronized (ctrl) {
            Platform.runLater(() -> {
                Alert alert = new Alert(AlertType.ERROR);
                alert.initOwner(null);
                alert.setTitle("Error occured");
                alert.setHeaderText("See details :");
                alert.setContentText(error);
                alert.showAndWait();
            });
        }

    }

    public void setRefController(IControllerForView ctrl) {
        this.ctrl = ctrl;
    }

    @FXML
    private void turnLedOn(ActionEvent event) {
        synchronized (ctrl) {
            Platform.runLater(() -> {
                String led = cbbLed.getSelectionModel().getSelectedItem();
                try {
                    if (!led.equals("top")) {
                        led = "bottom." + led;
                    }
                    ctrl.turnLedOn(((int) clpLed.getValue().getRed() * 32), ((int) clpLed.getValue().getGreen() * 32), ((int) clpLed.getValue().getBlue() * 32), led);
                } catch (NullPointerException e) {
                    displayError("Please choose a led");
                }
            });
        }
    }

    @FXML
    private void disconnectController(ActionEvent event) {
        synchronized (ctrl) {
            Platform.runLater(() -> {
                ctrl.disconnectController();
                isControllerConnected = false;
                thymioConnectedState();
            });
        }
    }

    @FXML
    private void disconnectThymio(ActionEvent event) {
        synchronized (ctrl) {
            Platform.runLater(() -> {
                ctrl.disconnectThymio();
                isThymioConnected = false;
                initialState();
            });
        }
    }

    @FXML
    private void connectController(ActionEvent event) {
        synchronized (ctrl) {
            Platform.runLater(() -> {
                if (ctrl.connectController()) {
                    controllerConnectedState();
                }
            });
        }
    }

    @FXML
    private void connectThymio(ActionEvent event) {
        synchronized (ctrl) {
            Platform.runLater(() -> {
                if (ctrl.connectThymio(txtThymio.getText())) {
                    thymioConnected();
                    isThymioConnected = true;
                }
            });
        }
    }

    private void initialState() {
        synchronized (ctrl) {
            Platform.runLater(() -> {
                btnConnectThymio.setDisable(false);
                txtThymio.setDisable(false);
                btnDisconnectThymio.setDisable(true);
                btnConnectController.setDisable(true);
                btnDisconnectController.setDisable(true);
                btnLed.setDisable(true);
                cbbLed.setDisable(true);
                clpLed.setDisable(true);
                txtController.setText("Disconnected");
            });
        }
    }

    private void controllerConnectedState() {
        synchronized (ctrl) {
            Platform.runLater(() -> {
                btnConnectThymio.setDisable(true);
                btnDisconnectThymio.setDisable(true);
                btnConnectController.setDisable(true);
                btnDisconnectController.setDisable(false);
                txtController.setText("Connected");
            });
        }
    }

    private void thymioConnectedState() {
        synchronized (ctrl) {
            Platform.runLater(() -> {
                btnConnectThymio.setDisable(true);
                txtThymio.setDisable(true);
                btnDisconnectThymio.setDisable(false);
                btnConnectController.setDisable(false);
                btnDisconnectController.setDisable(true);
                btnLed.setDisable(false);
                cbbLed.setDisable(false);
                clpLed.setDisable(false);
                txtController.setText("Disconnected");
            });
        }
    }

}
