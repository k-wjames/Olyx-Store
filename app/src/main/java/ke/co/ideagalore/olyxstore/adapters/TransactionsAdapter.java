package ke.co.ideagalore.olyxstore.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ke.co.ideagalore.olyxstore.R;
import ke.co.ideagalore.olyxstore.models.SaleItem;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.ViewHolder> {

    Context context;
    ArrayList<SaleItem>saleItemArrayList;

    SaleItem saleItem;

    public TransactionsAdapter() {
    }

    public TransactionsAdapter(Context context, ArrayList<SaleItem> saleItemArrayList) {
        this.context = context;
        this.saleItemArrayList = saleItemArrayList;
    }

    @NonNull
    @Override
    public TransactionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.transaction_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionsAdapter.ViewHolder holder, int position) {

        saleItem=saleItemArrayList.get(position);
        holder.product.setText(saleItem.getProduct());
        holder.transactionType.setText(saleItem.getSaleType());
        holder.quantity.setText(saleItem.getQuantity()+"");
        holder.price.setText(saleItem.getTotalPrice()+"");
        holder.time.setText(saleItem.getTime());
    }

    @Override
    public int getItemCount() {
        return saleItemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView product, transactionType, quantity, price, time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            product= itemView.findViewById(R.id.tv_product);
            transactionType=itemView.findViewById(R.id.tv_transaction_type);
            quantity=itemView.findViewById(R.id.tv_quantity);
            price=itemView.findViewById(R.id.tv_price);
            time=itemView.findViewById(R.id.tv_time);
        }
    }
}
