package ke.co.ideagalore.olyxstore.models;

public class Credit {
    String product, quantity, name, amount, phone, creditId, date, time;

    public Credit() {
    }

    public Credit(String product, String quantity, String name, String amount, String phone, String creditId, String date, String time) {
        this.product = product;
        this.quantity = quantity;
        this.name = name;
        this.amount = amount;
        this.phone = phone;
        this.creditId = creditId;
        this.date = date;
        this.time = time;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCreditId() {
        return creditId;
    }

    public void setCreditId(String creditId) {
        this.creditId = creditId;
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
}
