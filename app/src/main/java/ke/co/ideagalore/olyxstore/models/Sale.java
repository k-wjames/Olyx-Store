package ke.co.ideagalore.olyxstore.models;

public class Sale {
    String saleId, brand, quantity, price, shop;

    public Sale() {
    }

    public Sale(String saleId, String brand, String quantity, String price, String shop) {
        this.saleId = saleId;
        this.brand = brand;
        this.quantity = quantity;
        this.price = price;
        this.shop = shop;
    }

    public String getSaleId() {
        return saleId;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }
}
