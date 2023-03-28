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
import ke.co.ideagalore.olyxstore.models.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    Context context;
    ArrayList<Product> productList;
    Product product;

    public ProductAdapter() {
    }

    public ProductAdapter(Context context, ArrayList<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.product_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        product=productList.get(position);

       /* holder.brand.setText(product.getBrand());
        holder.capacity.setText(product.getCapacity()+" Kg ");
        holder.quantity.setText(product.getQuantity()+"");*/

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView brand,capacity,quantity;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

           /* brand=itemView.findViewById(R.id.tv_brand);
            capacity=itemView.findViewById(R.id.tv_capacity);
            quantity=itemView.findViewById(R.id.tv_quantity);*/

        }
    }
}
