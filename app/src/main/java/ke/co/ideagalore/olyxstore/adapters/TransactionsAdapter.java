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
import java.util.List;

import ke.co.ideagalore.olyxstore.R;
import ke.co.ideagalore.olyxstore.models.Transaction;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.ViewHolder> {


    public interface OnItemClickListener {
        void onItemClick(Transaction transaction);
    }
    Context context;
    List<Transaction> transactionArrayList;

    private final OnItemClickListener listener;
    Transaction transaction;

    public TransactionsAdapter(ArrayList<Transaction> transactionArrayList, OnItemClickListener listener) {
        this.transactionArrayList = transactionArrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TransactionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionsAdapter.ViewHolder holder, int position) {

        holder.bind(transactionArrayList.get(position),listener);
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

        public void bind(Transaction transaction, OnItemClickListener listener) {
            product.setText(transaction.getProduct() + " *" + transaction.getQuantity());
            transactionType.setText(transaction.getTransactionType());
            price.setText("Price KES " + transaction.getTotalPrice());

            if (getDate().equals(transaction.getDate())) {
                time.setText(transaction.getTime());
            } else {
                time.setText(transaction.getDate() + " " + transaction.getTime());
            }


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
