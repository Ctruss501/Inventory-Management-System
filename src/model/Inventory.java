package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This is the Inventory class for parts and products.
 */
public class Inventory {

    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     *  Will add a new part to the part observable list.
     */
    public static void addPart(Part newPart) {

        allParts.add(newPart);
    }

    /**
     * Will add a new product to the product observable list.
     */
    public static void addProduct(Product newProduct) {

        allProducts.add(newProduct);
    }

    /**
     * Searches for part by Part ID.
     */
    public static Part lookupPart(int partId) {

        ObservableList<Part> allParts = Inventory.getAllParts();

        for (Part pn : allParts) {
            if (pn.getId() == partId) {
                return pn;
            }
        }
        return null;
    }

    /**
     * Searches for product by Product ID.
     */
    public static Product lookupProduct(int productId) {

        ObservableList<Product> allProducts = Inventory.getAllProducts();

        for (Product pn : allProducts) {
            if (pn.getId() == productId) {
                return pn;
            }
        }
        return null;
    }

    /**
     * Searches for part by name/partial name
     */
    public static ObservableList<Part> lookupPart(String partialName) {

        ObservableList<Part> searchedPart = FXCollections.observableArrayList();

        ObservableList<Part> allParts = Inventory.getAllParts();

        for(Part pn: allParts){
            if(pn.getName().contains(partialName)){
                searchedPart.add(pn);
            }
        }

        return searchedPart;
    }

    /**
     * Searches for product by name/partial name.
     */
    public static ObservableList<Product> lookupProduct(String productName){

        ObservableList<Product> searchedProduct = FXCollections.observableArrayList();

        ObservableList<Product> allProducts = Inventory.getAllProducts();

        for(Product pn: allProducts){
            if(pn.getName().contains(productName)){
                searchedProduct.add(pn);
            }
        }
        return searchedProduct;
    }

    /**
     * Updates the selected part.
     */
    public static void updatePart(int index, Part selectedPart){

        allParts.set(index, selectedPart);
    }

    /**
     * Updates the selected product.
     */
    public static void updateProduct(int index, Product newProduct){

        allProducts.set(index, newProduct);
    }

    /**
     * Deletes the selected part.
     */
    public static boolean deletePart(Part selectedPart){

        return allParts.remove(selectedPart);
    }

    /**
     * Deletes the selected product.
     */
    public static boolean deleteProduct(Product selectedProduct){

        return allProducts.remove(selectedProduct);
    }

    /**
     * Will get an observable list of all parts.
     */
    public static ObservableList<Part> getAllParts(){

        return allParts;
    }

    /**
     * Will get and observable list of all products.
     */
    public static ObservableList<Product> getAllProducts(){

        return allProducts;
    }

}
