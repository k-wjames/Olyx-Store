package ke.co.ideagalore.olyxstore.models;

public class Transaction {
    String transactionType, product, price, profit, quantity, time, shop, transactionId;

    public Transaction() {
    }

    public Transaction(String transactionType, String product, String price, String profit,
                       String quantity, String time, String shop, String transactionId) {
        this.transactionType = transactionType;
        this.product = product;
        this.price = price;
        this.profit = profit;
        this.quantity = quantity;
        this.time = time;
        this.shop = shop;
        this.transactionId = transactionId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
