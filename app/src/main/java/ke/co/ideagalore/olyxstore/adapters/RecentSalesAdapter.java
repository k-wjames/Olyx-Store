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

    List<Transaction> transactionList;

    public RecentSalesAdapter(List<Transaction> transactionList) {
        this.transactionList = transactionList;
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

        Transaction transaction = transactionList.get(position);
        holder.tvId.setText(transaction.getTransactionId());
        holder.tvTransaction.setText(transaction.getTransactionType()+"-"+ transaction.getProduct()+" *"+ transaction.getQuantity());
        //holder.tvDateTime.setText(transaction.getDate()+" "+ transaction.getTime());
        holder.tvPrice.setText("KES "+ transaction.getTotalPrice());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String date=sdf.format(new Date(transaction.getDate()));

        LocalDate localDate = LocalDate.now(ZoneOffset.UTC);
        long dateToday = localDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();

        if (transaction.getDate()==dateToday) {
            holder.tvDateTime.setText(transaction.getTime());
        } else {
            holder.tvDateTime.setText(date + " " + transaction.getTime());
        }

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
    }
}
