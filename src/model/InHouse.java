package model;

/**
 * This is the In-House class parts for Parts Class
 */
public class InHouse extends Part {
    int machineId;

    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * Setter for Machine ID.
     */
    public void setMachineId(int id) {

        this.machineId = id;
    }

    /**
     * Getter for Company Name.
     */
    public int getMachineId() {

        return machineId;
    }
}