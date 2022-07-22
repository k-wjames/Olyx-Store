package ke.co.ideagalore.olyxstore.models;

public class TestItem {

    int price;
    String product;

    public TestItem() {
    }

    public TestItem(int price, String product) {
        this.price = price;
        this.product = product;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }
}
