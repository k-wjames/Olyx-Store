package ke.co.ideagalore.olyxstore.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import ke.co.ideagalore.olyxstore.models.Credit;

public class CreditAdapter extends RecyclerView.Adapter<CreditAdapter.ViewHolder> {

    List<Credit> creditList;
    Context context;
    String phone;

    public CreditAdapter(List<Credit> creditList, Context context) {
        this.creditList = creditList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.credit_item, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Credit credit = creditList.get(position);

        LocalDate localDate = LocalDate.now(ZoneOffset.UTC);
        long dateToday = localDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String creditDate = sdf.format(new Date(credit.getDate()));

        holder.name.setText(credit.getName());
        holder.product.setText(credit.getProduct());
        holder.amount.setText("KES " + credit.getAmount());

        if (credit.getDate() == dateToday) {
            holder.date.setText(credit.getTime());
        } else {
            holder.date.setText(creditDate + " " + credit.getTime());
        }

        phone = credit.getPhone();

        holder.ivCall.setOnClickListener(view -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + phone));
            context.startActivity(callIntent);
        });

    }

    @Override
    public int getItemCount() {
        return creditList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, product, amount, date;
        ImageView ivCall;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_customer);
            product = itemView.findViewById(R.id.tv_product);
            amount = itemView.findViewById(R.id.tv_amount);
            date = itemView.findViewById(R.id.tv_date_time);
            ivCall = itemView.findViewById(R.id.iv_call);
        }
    }


}
