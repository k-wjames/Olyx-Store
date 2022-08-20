package ke.co.ideagalore.olyxstore.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ke.co.ideagalore.olyxstore.R;
import ke.co.ideagalore.olyxstore.models.SaleItem;

public class RecentSalesAdapter extends RecyclerView.Adapter<RecentSalesAdapter.ViewHolder> {

    List<SaleItem> saleItemList;

    public RecentSalesAdapter(List<SaleItem> saleItemList) {
        this.saleItemList = saleItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_sales_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SaleItem saleItem = saleItemList.get(position);
        holder.tvId.setText(saleItem.getSaleId());
        holder.tvTransaction.setText(saleItem.getSaleType()+"-"+saleItem.getProduct()+" *"+saleItem.getQuantity());
        holder.tvDateTime.setText(saleItem.getDate()+" "+saleItem.getTime());
        holder.tvPrice.setText("KES "+saleItem.getTotalPrice());

    }

    @Override
    public int getItemCount() {
        return saleItemList.size();
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
