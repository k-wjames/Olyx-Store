package ke.co.ideagalore.olyxstore.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ke.co.ideagalore.olyxstore.R;
import ke.co.ideagalore.olyxstore.models.Transaction;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.ViewHolder> {

    Context context;
    ArrayList<Transaction> transactionArrayList;

    Transaction transaction;

    public TransactionsAdapter() {
    }

    public TransactionsAdapter(Context context, ArrayList<Transaction> transactionArrayList) {
        this.context = context;
        this.transactionArrayList = transactionArrayList;
    }

    @NonNull
    @Override
    public TransactionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.transaction_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionsAdapter.ViewHolder holder, int position) {

        transaction = transactionArrayList.get(position);
        holder.product.setText(transaction.getProduct() + " *" + transaction.getQuantity());
        holder.transactionType.setText(transaction.getTransactionType());
        holder.price.setText("Price KES " + transaction.getTotalPrice());

        if (getDate().equals(transaction.getDate())) {
            holder.time.setText(transaction.getTime());
        } else {
            holder.time.setText(transaction.getDate() + " " + transaction.getTime());
        }
    }

    @Override
    public int getItemCount() {
        return transactionArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView product, transactionType, price, time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            product = itemView.findViewById(R.id.tv_product);
            transactionType = itemView.findViewById(R.id.tv_transaction_type);
            price = itemView.findViewById(R.id.tv_price);
            time = itemView.findViewById(R.id.tv_time);
        }
    }

    String getDate() {
        String dateString;
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        dateString = formatter.format(date);
        return dateString;

    }
}
