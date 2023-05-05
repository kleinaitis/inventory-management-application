package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Product {
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    public Product() {
        this(0, null, 0.0, 0, 0, 0);
    }

    /**
     * @return all parts currently associated with the Product
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }

    /** This method deletes an associated part from a Product
     *
     * @param selectedPart associated part selected by the user
     * @return the associated parts remaining after removing the selectedPart
     */
    public boolean deleteAssociatedParts(Part selectedPart) {
        return associatedParts.remove(selectedPart);
    }

    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * @return the Product id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the Product id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the Product name
     */
    public String getName() { return name; }

    /**
     * @param name the Product name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the Product price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the Product price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the Product stock level
     */
    public int getStock() {
        return stock;
    }

    /**
     * @param stock the Product stock level to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * @return the Product min stock level
     */
    public int getMin() {
        return min;
    }

    /**
     * @param min the Product min stock to set
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * @return the Product max stock level
     */
    public int getMax() {
        return max;
    }

    /**
     * @param max the Product max stock to set
     */
    public void setMax(int max) {
        this.max = max;
    }
}
