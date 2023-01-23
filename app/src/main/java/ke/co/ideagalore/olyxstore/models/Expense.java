package ke.co.ideagalore.olyxstore.models;

public class Expense {

    String expense, expenseId, time, description;
    int price, quantity;
    long date;

    public Expense() {
    }

    public Expense(String expense, String expenseId, String time, String description, int price, int quantity, long date) {
        this.expense = expense;
        this.expenseId = expenseId;
        this.time = time;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.date = date;
    }

    public String getExpense() {
        return expense;
    }

    public void setExpense(String expense) {
        this.expense = expense;
    }

    public String getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(String expenseId) {
        this.expenseId = expenseId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
