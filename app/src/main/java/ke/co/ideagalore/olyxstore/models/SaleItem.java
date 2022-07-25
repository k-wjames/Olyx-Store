package ke.co.ideagalore.olyxstore.models;

public class SaleItem {

    String saleId,saleType,product, shop,date, time;
    int quantity, totalPrice, profit;

    public SaleItem() {
    }

    public SaleItem(String saleId, String saleType, String product, String shop, String date, String time, int quantity, int totalPrice, int profit) {
        this.saleId = saleId;
        this.saleType = saleType;
        this.product = product;
        this.shop = shop;
        this.date = date;
        this.time = time;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.profit = profit;
    }

    public String getSaleId() {
        return saleId;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    public String getSaleType() {
        return saleType;
    }

    public void setSaleType(String saleType) {
        this.saleType = saleType;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getProfit() {
        return profit;
    }

    public void setProfit(int profit) {
        this.profit = profit;
    }
}
