package ke.co.ideagalore.olyxstore.adapters;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

import ke.co.ideagalore.olyxstore.R;
import ke.co.ideagalore.olyxstore.models.Transaction;

public class RecentSalesAdapter extends RecyclerView.Adapter<RecentSalesAdapter.ViewHolder> {


    public interface OnItemClickListener {
        void onItemClick(Transaction transaction);
    }

    List<Transaction> transactionList;

    private final OnItemClickListener listener;

    public RecentSalesAdapter(List<Transaction> transactionList, OnItemClickListener listener) {
        this.transactionList = transactionList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_sales_item, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.bind(transactionList.get(position),listener);

    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvId, tvTransaction, tvDateTime, tvPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvId = itemView.findViewById(R.id.tv_id);
            tvTransaction = itemView.findViewById(R.id.tv_transaction);
            tvDateTime = itemView.findViewById(R.id.tv_date_time);
            tvPrice = itemView.findViewById(R.id.tv_price);
        }

        public void bind(Transaction transaction, OnItemClickListener listener) {
            tvId.setText(transaction.getTransactionId());
            tvTransaction.setText(transaction.getTransactionType()+"-"+ transaction.getProduct()+" *"+ transaction.getQuantity());
            tvPrice.setText("KES "+ transaction.getTotalPrice());

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String date=sdf.format(new Date(transaction.getDate()));

            LocalDate localDate = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                localDate = LocalDate.now(ZoneOffset.UTC);
            }
            long dateToday = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                dateToday = localDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
            }

            if (transaction.getDate()==dateToday) {
                tvDateTime.setText(transaction.getTime());
            } else {
                tvDateTime.setText(date + " " + transaction.getTime());
            }

            itemView.setOnClickListener(v -> listener.onItemClick(transaction));
        }
    }
}
