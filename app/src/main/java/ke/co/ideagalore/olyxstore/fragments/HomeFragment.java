package ke.co.ideagalore.olyxstore.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ke.co.ideagalore.olyxstore.R;
import ke.co.ideagalore.olyxstore.databinding.FragmentHomeBinding;
import ke.co.ideagalore.olyxstore.models.SaleItem;

public class HomeFragment extends Fragment implements View.OnClickListener {

    FragmentHomeBinding binding;

    ArrayList<SaleItem> saleItemArrayList;

    String dateToday;

    SaleItem saleItem;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getCurrentDate();
        getTransactionsData();

        saleItemArrayList = new ArrayList<>();

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        dateToday = formatter.format(date);

        binding.cvSell.setOnClickListener(this);
        binding.cvTransactions.setOnClickListener(this);
        binding.cvStock.setOnClickListener(this);
        binding.cvOrders.setOnClickListener(this);
    }

    private void getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);
        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());

        String dayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());

        binding.tvDay.setText(dayOfWeek + ",");
        binding.tvDate.setText(day + " " + month + " " + year);

    }

    @Override
    public void onClick(View view) {
        if (view == binding.cvSell) {
            Navigation.findNavController(view).navigate(R.id.sellFragment);
        } else if (view == binding.cvTransactions) {
            Navigation.findNavController(view).navigate(R.id.transactionsFragment);
        } else if (view == binding.cvStock) {
            Navigation.findNavController(view).navigate(R.id.stockFragment);
        } else if (view == binding.cvOrders) {
            //Navigation.findNavController(view).navigate(R.id.ordersFragment);
            Toast.makeText(getActivity(), "Hold tight! This is coming very soon.", Toast.LENGTH_SHORT).show();
        }
    }

    private void getTransactionsData() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Sales");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot transactionSnapshot : snapshot.getChildren()) {

                    saleItem = transactionSnapshot.getValue(SaleItem.class);
                    saleItemArrayList.add(saleItem);

                    for (int i = 0; i < saleItemArrayList.size(); i++) {
                        int sales = 0;
                        int gasRefillSales = 0;
                        int gasSales = 0;
                        int accessorySales = 0;

                        for (SaleItem item : saleItemArrayList) {
                            String date = saleItemArrayList.get(i).getDate();

                            if (date.equals(dateToday)) {

                                int transactions = saleItemArrayList.size();
                                binding.tvTransactions.setText(transactions + "");

                                sales = sales + item.getTotalPrice();
                                binding.tvSales.setText("Kshs. " + sales);
                            }

                            if (item.getSaleType().equals("Gas refill") && date.equals(dateToday)) {

                                gasRefillSales = gasRefillSales + item.getTotalPrice();
                                binding.tvRefillSales.setText("Kshs. "+ gasRefillSales);

                            }

                            if (item.getSaleType().equals("Gas sale") && date.equals(dateToday)) {

                                gasSales = gasSales + item.getTotalPrice();
                                binding.tvGasSales.setText("Kshs. "+gasSales);

                            }

                            if (item.getSaleType().equals("Accessory sale") && date.equals(dateToday)) {

                                accessorySales = accessorySales + item.getTotalPrice();
                                binding.tvAccessorySales.setText("Kshs. "+accessorySales);

                            }
                        }
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}