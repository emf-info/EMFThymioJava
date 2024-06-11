package ch.emf.Thymio_Java_Connnect.services;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.UUID;
import com.google.flatbuffers.ByteVector;
import com.google.flatbuffers.FlatBufferBuilder;
import mobsya.fb.AnyMessage;
import mobsya.fb.CompilationOptions;
import mobsya.fb.CompileAndLoadCodeOnVM;
import mobsya.fb.ConnectionHandshake;
import mobsya.fb.LockNode;
import mobsya.fb.Message;
import mobsya.fb.ProgrammingLanguage;
import mobsya.fb.SetVMExecutionState;
import mobsya.fb.VMExecutionStateCommand;
import ch.emf.Thymio_Java_Connnect.models.Thymio;

/**
 * Thymio sender application for Thymio Java Connect. This application is used
 * to send messages to the Thymio on a thread.
 *
 * @author Tom Yerly
 * @version 1.0
 */
public class ServiceThymioSender extends Thread {

    /**
     * Necessary attributes to send orders to the Thymio
     */
    private String thymioName;
    private Thymio thymio;
    private boolean isRunning;
    private ArrayList<String> programList;

    /**
     * Constructor ot the ThymioSender class.
     */
    public ServiceThymioSender(String thymioName) {
        this.thymioName = thymioName;
        this.isRunning = true;
        this.programList = new ArrayList<String>();
    }

    /**
     * Method that can add an order to the lists of orders
     *
     * @param program order in aseba code
     */
    public boolean sendProgram(String program) {
        programList.add(program);
        return true;
    }

    /**
     * Method used to start or stop the ServiceThymioSender Thread
     *
     * @param isRunning the isRunning to set
     */
    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    /**
     * The thread run method that sends messages to the Thymio. The sequence of
     * messages is: - ConnectionHandshake - LockNode - CompileAndLoadCodeOnVM -
     * SetVMExecutionState
     */
    @Override
    public void run() {
        try {
            while (isRunning) {
                thymio = new Thymio();
                ServiceThymioCommunicator client = new ServiceThymioCommunicator(thymio, thymioName);

                // Connect and send FlatBuffer message
                client.connect();

                while (!client.isOpen()) {
                    // wait for the connection to open
                }

                // Initialize FlatBuffers builders
                FlatBufferBuilder builderConnectionHandshake = new FlatBufferBuilder();
                FlatBufferBuilder builderLockNode = new FlatBufferBuilder();
                FlatBufferBuilder builderCompileAndLoadCodeOnVM = new FlatBufferBuilder();
                FlatBufferBuilder builderVMExecutionState = new FlatBufferBuilder();

                // Initialize variables
                int offsetPassword = builderConnectionHandshake.createString("");

                // Create a ConnectionHandshake message
                ConnectionHandshake.startConnectionHandshake(builderConnectionHandshake);
                ConnectionHandshake.addMinProtocolVersion(builderConnectionHandshake, 1);
                ConnectionHandshake.addProtocolVersion(builderConnectionHandshake, 1);
                ConnectionHandshake.addPassword(builderConnectionHandshake, offsetPassword);
                int offset = ConnectionHandshake.endConnectionHandshake(builderConnectionHandshake);

                // Send the ConnectionHandshake message
                Message.startMessage(builderConnectionHandshake);
                Message.addMessageType(builderConnectionHandshake, AnyMessage.ConnectionHandshake);
                Message.addMessage(builderConnectionHandshake, offset);
                int messageOffset = Message.endMessage(builderConnectionHandshake);
                builderConnectionHandshake.finish(messageOffset);
                client.send(builderConnectionHandshake.dataBuffer());

                // Wait for the Thymio to send the NodesChanged message
                synchronized (thymio) {

                    try {
                        // Wait the connexion with the Thymio is successfull
                        thymio.wait(5000);
                        // If the connexion has failed it put an end at this thread
                        if (thymio.getThymioNode() == null) {
                            client.close();
                            return;
                        }
                        ServiceThymioOrders.isConnected = true;
                        System.out.println("ThymioSender: NodesChanged message received");

                        ByteVector byteVector = thymio.getThymioNode().nodeId().idVector();
                        byte[] bytes = new byte[byteVector.length()];
                        for (int i = 0; i < byteVector.length(); i++) {
                            bytes[i] = byteVector.get(i);
                        }

                        int offsetNodeId = mobsya.fb.NodeId.createNodeId(builderLockNode,
                                mobsya.fb.NodeId.createIdVector(builderLockNode, bytes));

                        long lockUuid = UUID.randomUUID().getLeastSignificantBits();

                        // Create a LockNode message
                        LockNode.startLockNode(builderLockNode);
                        LockNode.addRequestId(builderLockNode, lockUuid);
                        LockNode.addNodeId(builderLockNode, offsetNodeId);
                        int offsetLock = LockNode.endLockNode(builderLockNode);

                        // Send the LockNode message
                        Message.startMessage(builderLockNode);
                        Message.addMessageType(builderLockNode, AnyMessage.LockNode);
                        Message.addMessage(builderLockNode, offsetLock);
                        int messageLockOffset = Message.endMessage(builderLockNode);
                        builderLockNode.finish(messageLockOffset);
                        client.send(builderLockNode.dataBuffer());

                        // clears the list of order to prevent orders from being requested before the connection is successful
                        programList.clear();
                        while (isRunning) {
                            // Tells that the connexion with the Thymio is sucessfull

                            if (programList.size() != 0) {
                                int offsetCodeId = builderCompileAndLoadCodeOnVM.createString(programList.get(0));
                                programList.remove(0);

                                int offsetNodeCodeVMId = mobsya.fb.NodeId.createNodeId(builderCompileAndLoadCodeOnVM,
                                        mobsya.fb.NodeId.createIdVector(builderCompileAndLoadCodeOnVM, bytes));

                                // Create a CompileAndLoadCodeOnVM message
                                long loadCodeVM = UUID.randomUUID().getLeastSignificantBits();
                                CompileAndLoadCodeOnVM.startCompileAndLoadCodeOnVM(builderCompileAndLoadCodeOnVM);
                                CompileAndLoadCodeOnVM.addRequestId(builderCompileAndLoadCodeOnVM, loadCodeVM);
                                CompileAndLoadCodeOnVM.addNodeId(builderCompileAndLoadCodeOnVM, offsetNodeCodeVMId);
                                CompileAndLoadCodeOnVM.addProgram(builderCompileAndLoadCodeOnVM, offsetCodeId);
                                CompileAndLoadCodeOnVM.addLanguage(builderCompileAndLoadCodeOnVM,
                                        ProgrammingLanguage.Aseba);
                                CompileAndLoadCodeOnVM.addOptions(builderCompileAndLoadCodeOnVM,
                                        CompilationOptions.LoadOnTarget);
                                int offsetLoadCodeVM = CompileAndLoadCodeOnVM
                                        .endCompileAndLoadCodeOnVM(builderCompileAndLoadCodeOnVM);

                                Message.startMessage(builderCompileAndLoadCodeOnVM);
                                Message.addMessageType(builderCompileAndLoadCodeOnVM,
                                        AnyMessage.CompileAndLoadCodeOnVM);
                                Message.addMessage(builderCompileAndLoadCodeOnVM, offsetLoadCodeVM);
                                int messageOffsetCode = Message.endMessage(builderCompileAndLoadCodeOnVM);
                                builderCompileAndLoadCodeOnVM.finish(messageOffsetCode);
                                client.send(builderCompileAndLoadCodeOnVM.dataBuffer());

                                int offsetNodeExecuteId = mobsya.fb.NodeId.createNodeId(builderVMExecutionState,
                                        mobsya.fb.NodeId.createIdVector(builderVMExecutionState, bytes));

                                // Create a ExecutionRequest message
                                long executionRequest = UUID.randomUUID().getLeastSignificantBits();
                                SetVMExecutionState.startSetVMExecutionState(builderVMExecutionState);
                                SetVMExecutionState.addRequestId(builderVMExecutionState, executionRequest);
                                SetVMExecutionState.addNodeId(builderVMExecutionState, offsetNodeExecuteId);
                                SetVMExecutionState.addCommand(builderVMExecutionState, VMExecutionStateCommand.Run);
                                int offsetExecutionRequest = SetVMExecutionState
                                        .endSetVMExecutionState(builderVMExecutionState);

                                Message.startMessage(builderVMExecutionState);
                                Message.addMessageType(builderVMExecutionState, AnyMessage.SetVMExecutionState);
                                Message.addMessage(builderVMExecutionState, offsetExecutionRequest);
                                int messageOffsetExecutionRequest = Message.endMessage(builderVMExecutionState);
                                builderVMExecutionState.finish(messageOffsetExecutionRequest);
                                client.send(builderVMExecutionState.dataBuffer());
                            }
                        }

                        //catches exceptions
                    } catch (InterruptedException e) {
                        System.out.println(e.toString());
                        e.printStackTrace();
                    } catch (Exception e) {
                        System.out.println(e.toString());
                        e.printStackTrace();
                    }
                }
                // Stop this Thread
                client.close();
            }

            //catches exceptions
        } catch (URISyntaxException e) {
            System.out.println(e.toString());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }

    }

}
