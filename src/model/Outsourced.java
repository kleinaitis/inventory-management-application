package model;

/** This creates the Outsourced subclass that inherits from the Parts superclass. */
public class Outsourced extends Part {

    private static String companyName;

    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        Outsourced.companyName = companyName;
    }

    /**
     * @return the Company Name
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * @param companyName the Company Name to set
     */
    public void setCompanyName(String companyName) {
        Outsourced.companyName = companyName;
    }
}
