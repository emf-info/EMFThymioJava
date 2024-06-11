package ch.emf.Thymio_Java_Connnect.services;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import mobsya.fb.AnyMessage;
import mobsya.fb.ErrorType;
import mobsya.fb.Message;
import mobsya.fb.NodesChanged;
import mobsya.fb.RequestCompleted;
import ch.emf.Thymio_Java_Connnect.models.Thymio;
import mobsya.fb.Node;
import mobsya.fb.NodeStatus;

/**
 * Thymio service for Thymio Java Connect. This service is used to receive
 * messages from the Thymio.
 *
 * @author Tom Yerly
 * @version 1.0
 */
public class ServiceThymioCommunicator extends WebSocketClient {

    /**
     * The URL of the Thymio Java Connect server.
     */
    public static final String URL = "ws://localhost:8597";

    /**
     * The Thymio model.
     */
    private Thymio thymio;

    /**
     * The name of the desired Thymio.
     */
    private String name;

    /**
     * Constructor of the ThymioService class.
     *
     * @param thymio The Thymio model.
     * @param name The name of the desired Thymio.
     * @throws URISyntaxException If the URI is not valid.
     */
    public ServiceThymioCommunicator(Thymio thymio, String name) throws URISyntaxException {
        super(new URI(URL));
        this.thymio = thymio;
        this.name = name;

    }

    /**
     * Constructor of the ThymioService class.
     *
     * @param thymio The Thymio model.
     * @param name The name of the desired Thymio.
     * @param url The URL of the Thymio Java Connect server.
     * @throws URISyntaxException If the URI is not valid.
     */
    public ServiceThymioCommunicator(Thymio thymio, String name, String url) throws URISyntaxException {
        super(new URI(url));
        this.thymio = thymio;
        this.name = name;
    }

    /**
     * Event when the connection is opened.
     *
     * @param handshakedata the server handshake
     */
    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Opened connection");
        System.out.println("Status code: " + handshakedata.getHttpStatus());
        System.out.println("Status message: " + handshakedata.getHttpStatusMessage());
    }

    /**
     * Event when a message is received.
     */
    @Override
    public void onMessage(String message) {
        System.out.println("Received message: " + message);
    }

    /**
     * Event when a message is received.
     *
     * @param bytes the bytes of the message
     */
    @Override
    public void onMessage(ByteBuffer bytes) {
        Message msg = Message.getRootAsMessage(bytes);
        System.out.println("> Message type: " + AnyMessage.name(msg.messageType()));
        //Processes the message and print information of the message in the console
        switch (msg.messageType()) {
            case AnyMessage.ConnectionHandshake:
                System.out.println("ConnectionHandshake");
                break;
            case AnyMessage.RequestCompleted:
                System.out.println("RequestCompleted");
                RequestCompleted requestCompleted = (RequestCompleted) msg.message(new RequestCompleted());
                System.out.println("Request ID : " + requestCompleted.requestId());
                break;
            case AnyMessage.NodesChanged:
                System.out.println("NodesChanged");
                NodesChanged nodesChanged = (NodesChanged) msg.message(msg.message(new NodesChanged()));
                System.out.println("Number node : " + nodesChanged.nodesLength());
                for (int i = 0; i < nodesChanged.nodesLength(); i++) {
                    try {
                        Node node = nodesChanged.nodes(i);
                        System.out.println(node.name());
                        if (name != null) {
                            if (node.name().equals(name)) {
                                System.out.println("===============");
                                System.out.println("Node name: " + node.name());
                                System.out.println("Node status: " + NodeStatus.name(node.status()));
                                System.out.println("Node fwVersion: " + node.fwVersion());
                                System.out.println("Node type: " + node.type());
                                System.out.println("Node NodeId: " + node.nodeId());
                                System.out.println("===============");

                                if (NodeStatus.name(node.status()).equals("ready")) {
                                    // Tells that the Thymio is ready
                                    ServiceThymioOrders.isReady = true;
                                }
                                synchronized (thymio) {
                                    thymio.setThymioNode(node);
                                    thymio.notify();
                                }
                            }
                        }
                    } catch (IllegalArgumentException ex) {
                        ServiceThymioOrders.isConnected = true;
                        ServiceThymioOrders.isReady = false;
                        System.out.println("IllegalArguments");
                    }
                }
                break;
            case AnyMessage.CompileAndLoadCodeOnVM:
                System.out.println("CompileAndLoadCodeOnVM");
                break;
            case AnyMessage.Ping:
                System.out.println("Ping");
                break;
            case AnyMessage.Error:
                synchronized (thymio) {
                    mobsya.fb.Error error = (mobsya.fb.Error) msg.message(new mobsya.fb.Error());
                    String errorMsg = ErrorType.names[error.error()];
                    ServiceThymioOrders.isConnected = false;
                    ServiceThymioOrders.isReady = false;
                    break;
                }
            case AnyMessage.RequestListOfNodes:
                System.out.println("RequestListOfNodes");
                break;
            case AnyMessage.CompilationResultSuccess:
                System.out.println("CompilationResultSuccess");
                break;
            case AnyMessage.CompilationResultFailure:
                System.out.println("CompilationResultFailure");
                break;
            default:
                System.out.println("Unknown message type " + msg.messageType());
                break;
        }

    }

    /**
     * Event when the connection is closed.
     *
     * @param code the code number of the disconnection
     * @param reason the reason of the disconnection
     * @param remote tells if the connexion was remote
     */
    @Override
    public void onClose(int code, String reason, boolean remote) {
        synchronized (thymio) {
            System.out.println("Closed connection\nCode: " + code + "\nReason: " + reason + "\nRemote: " + remote);
            ServiceThymioOrders.isConnected = false;
            ServiceThymioOrders.isReady = false;
        }
    }

    /**
     * Event when an error occurs.
     *
     * @param ex the exception
     */
    @Override
    public void onError(Exception ex) {
        synchronized (thymio) {
            System.out.println("Error " + ex.getMessage());
            ServiceThymioOrders.isConnected = false;
            ServiceThymioOrders.isReady = false;
        }
    }

}
