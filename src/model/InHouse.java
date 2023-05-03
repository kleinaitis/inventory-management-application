package model;

/** This creates the InHouse subclass that inherits from the Parts superclass. */
public class InHouse extends Part {

    private static int machineid;

    public InHouse(int id, String name, double price, int stock, int min, int max, int machineID) {
        super(id, name, price, stock, min, max);
        machineid = machineID;
    }

    /**
     * @return the Machine ID
     */
    public int getMachineid() {
        return machineid;
    }

    /**
     * @param machineid the Machine ID to set
     */
    public void setMachineid(int machineid) {
        InHouse.machineid = machineid;
    }
}
