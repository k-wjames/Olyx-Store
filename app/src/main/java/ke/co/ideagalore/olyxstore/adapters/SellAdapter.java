package ke.co.ideagalore.olyxstore.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ke.co.ideagalore.olyxstore.R;
import ke.co.ideagalore.olyxstore.models.Product;

public class SellAdapter extends RecyclerView.Adapter<SellAdapter.ViewHolder> {

    Context context;
    ArrayList<Product>productArrayList;
    Product product;

    public SellAdapter() {
    }

    public SellAdapter(Context context, ArrayList<Product> productArrayList) {
        this.context = context;
        this.productArrayList = productArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.product_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        product=productArrayList.get(position);

        String id=product.getProdId();
        String brand=product.getBrand();
        int capacity=product.getCapacity();

        holder.brand.setText(product.getBrand());
        holder.capacity.setText(capacity+" Kg ");
        holder.quantity.setText(product.getQuantity()+"");
        holder.layout.setOnClickListener(view -> {

            Bundle bundle = new Bundle();
            bundle.putString("prodId", id);
            bundle.putString("brand", brand);
            bundle.putInt("capacity", capacity);
            bundle.putInt("price", 1200);
            Navigation.findNavController(view).navigate(R.id.checkoutFragment, bundle);
        });

    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView brand,capacity,quantity;
        LinearLayout layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            brand=itemView.findViewById(R.id.tv_brand);
            capacity=itemView.findViewById(R.id.tv_capacity);
            quantity=itemView.findViewById(R.id.tv_quantity);
            layout=itemView.findViewById(R.id.layout);
        }
    }
}
