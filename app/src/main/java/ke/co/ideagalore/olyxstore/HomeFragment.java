package ke.co.ideagalore.olyxstore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import ke.co.ideagalore.olyxstore.adapters.TransactionsAdapter;
import ke.co.ideagalore.olyxstore.databinding.FragmentHomeBinding;
import ke.co.ideagalore.olyxstore.models.SaleItem;
import ke.co.ideagalore.olyxstore.models.TestItem;

public class HomeFragment extends Fragment implements View.OnClickListener {

    FragmentHomeBinding binding;

    ArrayList<SaleItem> saleItemArrayList, gasRefillArrayList, gasSaleArrayList, accessorySaleArrayList;
    TransactionsAdapter adapter;

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
        gasRefillArrayList = new ArrayList<>();
        gasSaleArrayList = new ArrayList<>();
        accessorySaleArrayList = new ArrayList<>();
        getTransactionsData();

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
            Navigation.findNavController(view).navigate(R.id.ordersFragment);
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

                    binding.tvTransactions.setText(saleItemArrayList.size() + "");

                    if (saleItemArrayList.size() > 0) {

                        int totalSales = 0;
                        for (int i = 0; i < saleItemArrayList.size(); i++) {

                            totalSales = +totalSales + saleItemArrayList.get(i).getTotalPrice();
                            binding.tvSales.setText("Kshs. " + totalSales);
                        }

                        int refillSales = 0;
                        int gasSales = 0;
                        int accessorySales = 0;

                        for (int k = 0; k < saleItemArrayList.size(); k++) {

                            int price = saleItemArrayList.get(k).getTotalPrice();
                            String saleType = saleItemArrayList.get(k).getSaleType();
                            if (saleType.equals("Gas refill")) {

                                saleItem = new SaleItem();
                                saleItem.setTotalPrice(price);
                                gasRefillArrayList.add(saleItem);

                                for (int j=0; j<gasRefillArrayList.size(); j++){

                                    refillSales=+ refillSales +gasRefillArrayList.get(j).getTotalPrice();
                                    binding.tvRefillSales.setText("Kshs. "+refillSales);

                                }

                            } else if (saleType.equals("Gas sale")) {

                                saleItem = new SaleItem();
                                saleItem.setTotalPrice(price);
                                gasSaleArrayList.add(saleItem);

                                for (int j=0; j<gasSaleArrayList.size(); j++){

                                    gasSales=+ gasSales +gasSaleArrayList.get(j).getTotalPrice();
                                    binding.tvGasSales.setText("Kshs. "+gasSales);

                                }

                            } else if (saleType.equals("Accessory sale")) {

                                saleItem = new SaleItem();
                                saleItem.setTotalPrice(price);
                                accessorySaleArrayList.add(saleItem);

                                for (int j=0; j<accessorySaleArrayList.size(); j++){

                                    accessorySales=+ accessorySales +accessorySaleArrayList.get(j).getTotalPrice();
                                    binding.tvAccessorySales.setText("Kshs. "+accessorySales);

                                }

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