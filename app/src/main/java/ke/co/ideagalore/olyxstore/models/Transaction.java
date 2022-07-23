package ke.co.ideagalore.olyxstore.models;


public class Transaction {
    String transactionType, product, shop,date, transactionId;
    int  price, profit, quantity;


    public Transaction() {
    }

    public Transaction(String transactionType, String product, String shop, String date,
                       String transactionId, int price, int profit, int quantity) {
        this.transactionType = transactionType;
        this.product = product;
        this.shop = shop;
        this.date = date;
        this.transactionId = transactionId;
        this.price = price;
        this.profit = profit;
        this.quantity = quantity;
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

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getProfit() {
        return profit;
    }

    public void setProfit(int profit) {
        this.profit = profit;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
