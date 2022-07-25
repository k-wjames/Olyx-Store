package ke.co.ideagalore.olyxstore.models;

public class StockItem {
    String product, description;
    int quantity;

    public StockItem() {
    }

    public StockItem(String product, String description, int quantity) {
        this.product = product;
        this.description = description;
        this.quantity = quantity;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
