package ke.co.ideagalore.olyxstore.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ke.co.ideagalore.olyxstore.R;
import ke.co.ideagalore.olyxstore.adapters.RecentSalesAdapter;
import ke.co.ideagalore.olyxstore.databinding.FragmentHomeBinding;
import ke.co.ideagalore.olyxstore.models.Expense;
import ke.co.ideagalore.olyxstore.models.SaleItem;

public class HomeFragment extends Fragment implements View.OnClickListener {

    FragmentHomeBinding binding;

    ArrayList<SaleItem> saleItemArrayList;

    List<Expense> expenseList = new ArrayList<>();

    String dateToday;

    SaleItem saleItem;

    Expense expense;

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
        getExpenditureData();

        saleItemArrayList = new ArrayList<>();

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        dateToday = formatter.format(date);

        binding.cvSell.setOnClickListener(this);
        binding.cvTransactions.setOnClickListener(this);
        binding.cvExpenditure.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == binding.cvSell) {
            Navigation.findNavController(view).navigate(R.id.sellFragment);
        } else if (view == binding.cvTransactions) {
            Navigation.findNavController(view).navigate(R.id.transactionsFragment);
        } else if (view == binding.cvExpenditure) {
            Navigation.findNavController(view).navigate(R.id.expenditureFragment);
        }
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

    private void getTransactionsData() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Sales");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot transactionSnapshot : snapshot.getChildren()) {

                    saleItem = transactionSnapshot.getValue(SaleItem.class);
                    saleItemArrayList.add(0,saleItem);

                    int sales = 0;
                    int gasRefillSales = 0;
                    int gasSales = 0;
                    int accessorySales = 0;

                    List<SaleItem>soldItems=new ArrayList<>();

                    for (SaleItem item : saleItemArrayList) {
                        String date = item.getDate();

                        if (date.equals(dateToday)) {

                            soldItems.add(item);

                            int transactions = soldItems.size();
                            binding.tvTransactions.setText(transactions + "");

                            displayList(soldItems);


                            sales = sales + item.getTotalPrice();
                            binding.tvSales.setText("KES " + sales);

                            if (item.getSaleType().equals("Gas refill")) {

                                gasRefillSales = gasRefillSales + item.getTotalPrice();
                                binding.tvRefillSales.setText("KES " + gasRefillSales);

                            }

                            if (item.getSaleType().equals("Gas sale")) {

                                gasSales = gasSales + item.getTotalPrice();
                                binding.tvGasSales.setText("KES " + gasSales);

                            }

                            if (item.getSaleType().equals("Accessory sale")) {

                                accessorySales = accessorySales + item.getTotalPrice();
                                binding.tvAccessorySales.setText("KES " + accessorySales);

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

    private void getExpenditureData() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Expenditure");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                expenseList.clear();
                for (DataSnapshot transactionSnapshot : snapshot.getChildren()) {

                    expense = transactionSnapshot.getValue(Expense.class);
                    expenseList.add(expense);

                    for (int i = 0; i < expenseList.size(); i++) {
                        int expenditure = 0;

                        for (Expense item : expenseList) {
                            String date = item.getDate();

                            if (date.equals(dateToday)) {
                                List<Expense> daysExpense = new ArrayList<>();
                                daysExpense.add(item);

                                for (int z = 0; z < daysExpense.size(); z++) {

                                    expenditure = expenditure + daysExpense.get(z).getPrice();
                                    binding.tvExpenditure.setText("KES " + expenditure);

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

    private void displayList(List<SaleItem> list) {
        binding.rvTransactions.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvTransactions.setHasFixedSize(true);
        RecentSalesAdapter adapter = new RecentSalesAdapter(list);
        binding.rvTransactions.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        binding.cvRecentTransactions.setVisibility(View.VISIBLE);
    }
}