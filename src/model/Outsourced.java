package model;

/**
 * This is the Outsourced class parts for Parts class.
 */
public class Outsourced extends Part {
    String companyName;

    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * Setter for Company Name.
     */
    public void setCompanyName(String companyName) {

        this.companyName = companyName;
    }

    /**
     * Getter for Company Name.
     */
    public String getCompanyName() {

        return companyName;
    }
}