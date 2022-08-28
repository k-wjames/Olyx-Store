package ke.co.ideagalore.olyxstore.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ke.co.ideagalore.olyxstore.R;
import ke.co.ideagalore.olyxstore.models.Credit;

public class CreditAdapter extends RecyclerView.Adapter<CreditAdapter.ViewHolder> {

    List<Credit>creditList;

    public CreditAdapter(List<Credit> creditList) {
        this.creditList = creditList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.credit_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Credit credit=creditList.get(position);

        holder.name.setText(credit.getName());
        holder.product.setText(credit.getProduct());
        holder.amount.setText("KES "+credit.getAmount());
        holder.date.setText(credit.getDate()+" "+credit.getTime());
    }

    @Override
    public int getItemCount() {
        return creditList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, product, amount, date,phone;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.tv_customer);
            product=itemView.findViewById(R.id.tv_product);
            amount=itemView.findViewById(R.id.tv_amount);
            date=itemView.findViewById(R.id.tv_date_time);
        }
    }
}
