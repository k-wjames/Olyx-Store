package ke.co.ideagalore.olyxstore.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ke.co.ideagalore.olyxstore.R;
import ke.co.ideagalore.olyxstore.models.StockItem;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.ViewHolder> {

    Context context;
    ArrayList<StockItem> stockItems;

    StockItem stockItem;

    public StockAdapter() {
    }

    public StockAdapter(Context context, ArrayList<StockItem> stockItems) {
        this.context = context;
        this.stockItems = stockItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.stock_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        stockItem=stockItems.get(position);
        holder.product.setText(stockItem.getProduct());
        holder.description.setText(stockItem.getDescription());
        holder.quantity.setText(stockItem.getQuantity()+"");

    }

    @Override
    public int getItemCount() {
        return stockItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView product, description,quantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            product=itemView.findViewById(R.id.tv_product);
            description=itemView.findViewById(R.id.tv_description);
            quantity=itemView.findViewById(R.id.tv_quantity);
        }
    }
}
