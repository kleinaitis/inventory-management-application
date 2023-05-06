package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** This creates the Inventory class that is a collection of parts and products within the application. */
public class Inventory {
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /** This method adds a part to the inventory
     *
     * @param newPart the part to add to the inventory
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * @return all parts in the inventory
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /** This method updates a part within the inventory at the specified index with a new part
     *
     * @param index index of the part being updated
     * @param selectedPart the new part that replaces the old part
     */
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     * This method deletes a part from the inventory
     *
     * @param selectedPart part being deleted
     */
    public static void deletePart(Part selectedPart) {
        allParts.remove(selectedPart);
    }

    /**
     * @return all products in the inventory
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    /** This method adds a product to the inventory
     *
     * @param newProduct the part to add to the inventory
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /** This method looks for a product within the inventory based on the specified search string.
     *
     * @param searchName product name or ID being searched
     * @return an ObservableList of matching products found in the inventory
     */
    public static ObservableList<Product> lookupProduct(String searchName) {
        ObservableList<Product> foundProducts = FXCollections.observableArrayList();

        if (searchName == null || searchName.isEmpty()) {
            foundProducts.addAll(allProducts);
        } else {
            for (Product product : allProducts) {
                if (product.getName().toLowerCase().contains(searchName.toLowerCase())
                        || String.valueOf(product.getId()).contains(searchName)) {
                    foundProducts.add(product);
                }
            }
        }
        return foundProducts;
    }

    /** This method looks for a part within the inventory based on the specified search string.
     *
     * @param searchName part name or ID being searched
     * @return an ObservableList of matching parts found in the inventory
     */
    public static ObservableList<Part> lookupPart(String searchName) {
        ObservableList<Part> foundParts = FXCollections.observableArrayList();

        if (searchName == null || searchName.isEmpty()) {
            foundParts.addAll(allParts);
        } else {
            for (Part part : allParts) {
                if (part.getName().toLowerCase().contains(searchName.toLowerCase())
                        || String.valueOf(part.getId()).contains(searchName)) {
                    foundParts.add(part);
                }
            }
        }
        return foundParts;
    }

    /** This method updates a product within the inventory at the specified index with a new product and adds associated parts to it.
     *
     * @param index index of the part being updated
     * @param product the new product that replaces the old part
     */
    public static void updateProduct(int index, Product product) {
        allProducts.set(index, product);
        for (Part part : product.getAllAssociatedParts()) {
            if (!allParts.contains(part)) {
                allParts.add(part);
            }
        }
    }
    /**
     * This method deletes a product from the inventory
     *
     * @param selectedProduct product being deleted
     */
    public static void deleteProduct(Product selectedProduct) {
        allProducts.remove(selectedProduct);
    }

}

