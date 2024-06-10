/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package ch.emf.Application_Demonstration.view;

import ch.emf.Application_Demonstration.ctrl.IControllerForView;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
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
 * FXML Controller class The View class is responsible for handling the user
 * interface of the application. It initializes the GUI components, manages the
 * state transitions, and handles user interactions. It implements the
 * Initializable and IViewForController interfaces.
 *
 * @author YerlyT04
 * @version 1.0
 */
public class View implements Initializable, IViewForController {

    /**
     * Attributes required to communicate with the controller, the GUI and to
     * check the connections
     */
    private IControllerForView ctrl;
    private Scene principalScene;
    private Stage mainStage;
    private ColorPicker colorPicker;
    private boolean controllerAlreadyConnected;
    private boolean isControllerConnected;
    private boolean isThymioConnected;
    private ArrayList<String> lstLeds;

    /**
     * Components of the GUI
     */
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

    /**
     * Constructs a View instance and initializes the controller reference to
     * null.
     */
    public View() {
        this.ctrl = null;
    }

    /**
     * Initializes the controller class.
     *
     * @param url the location used to resolve relative paths for the root
     * object, or null if the location is not known
     * @param rb the resources used to localize the root object, or null if the
     * root object was not localized
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    /**
     * Starts the JavaFX application and sets up the main stage.
     */
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

                //If the want to close the app
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
            //add choices in the list
            lstLeds = new ArrayList<>();
            lstLeds.add("top");
            lstLeds.add("left");
            lstLeds.add("right");
            cbbLed.getItems().addAll(lstLeds);

            //Sets the variables of the state of the connection to false
            isControllerConnected = false;
            isThymioConnected = false;
        });
    }

    /**
     * Updates the view to indicate that the Thymio robot is connected.
     */
    @Override
    public void thymioConnected() {
        synchronized (ctrl) {
            Platform.runLater(() -> {
                thymioConnectedState();
            });
        }

    }

    /**
     * Updates the view to indicate that the controller is connected.
     */
    @Override
    public void controllerConnected() {
        synchronized (ctrl) {
            Platform.runLater(() -> {
                controllerConnectedState();
            });
        }
    }

    /**
     * Displays an error message in a dialog box.
     *
     * @param error the error message to display
     */
    @Override
    public void displayError(String error) {
        synchronized (ctrl) {
            Platform.runLater(() -> {

                String message = error;

                if (error.equals("The controller is disconnected.")) {
                    disconnectController(new ActionEvent());
                } else if (error.equals("Thymio not recognized")) {
                    message = "The Thymio has not been recognized by Thymio suite. Check that the Thymio name appears in the Thymio suite application before trying a new connection.";
                    disconnectThymio(new ActionEvent());
                } else if (error.equals("Xbox problem")) {
                    message = "Due to a problem in the controller's communication library, it is not possible to reconnect without encountering a problem. If you still want to use the controller please restart this application.";
                }

                Alert alert = new Alert(AlertType.ERROR);
                alert.initOwner(null);
                alert.setTitle("Error occured");
                alert.setHeaderText("See details :");
                alert.setContentText(message);
                alert.showAndWait();
            });
        }

    }

    /**
     * Handles the action to turn the LED on.
     *
     * @param event the action event
     */
    @FXML
    private void turnLedOn(ActionEvent event) {
        synchronized (ctrl) {
            Platform.runLater(() -> {
                String led = cbbLed.getSelectionModel().getSelectedItem();
                try {
                    if (!led.equals("top")) {
                        led = "bottom." + led;
                    }
                    //round the value to fit the Thymios obligations
                    ctrl.turnLedOn(((int) Math.round(clpLed.getValue().getRed() * 32)), ((int) Math.round(clpLed.getValue().getGreen() * 32)), ((int) Math.round(clpLed.getValue().getBlue() * 32)), led);
                } catch (NullPointerException e) {
                    displayError("Please choose a led");
                }
            });
        }
    }

    /**
     * Handles the action to connect the controller.
     *
     * @param event the action event
     */
    @FXML
    private void connectController(ActionEvent event) {
        synchronized (ctrl) {
            Platform.runLater(() -> {
                if (!controllerAlreadyConnected) {
                    if (ctrl.connectController()) {
                        controllerConnectedState();
                        isControllerConnected = true;
                        controllerAlreadyConnected = true;
                    }
                } else {
                    displayError("Xbox problem");
                }

            });
        }
    }

    /**
     * Handles the action to connect the Thymio robot.
     *
     * @param event the action event
     */
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

    /**
     * Handles the action to disconnect the controller.
     *
     * @param event the action event
     */
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

    /**
     * Handles the action to disconnect the Thymio robot.
     *
     * @param event the action event
     */
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

    /**
     * Updates the view to its initial state.
     */
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

    /**
     * Updates the view to indicate that the controller is connected.
     */
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

    /**
     * Updates the view to indicate that the Thymio robot is connected.
     */
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

    /**
     * Sets the reference to the controller.
     *
     * @param ctrl the controller to set
     */
    public void setRefController(IControllerForView ctrl) {
        this.ctrl = ctrl;
    }

}
