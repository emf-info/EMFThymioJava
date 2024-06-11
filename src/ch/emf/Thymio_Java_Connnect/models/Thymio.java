package ch.emf.Thymio_Java_Connnect.models;

import mobsya.fb.Node;

/**
 * Thymio model for Thymio Java Connect.
 */
public class Thymio {

    /**
     * The Thymio node.
     */
    private Node thymioNode;

    /**
     * Constructor of the Thymio class.
     */
    public Thymio() {
        this.thymioNode = null;
    }

    /**
     * @return the thymioNode
     */
    public Node getThymioNode() {
        return thymioNode;
    }

    /**
     * @param thymioNode the thymioNode to set
     */
    public void setThymioNode(Node thymioNode) {
        this.thymioNode = thymioNode;
    }



}
