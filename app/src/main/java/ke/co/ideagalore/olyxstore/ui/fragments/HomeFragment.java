package ke.co.ideagalore.olyxstore.ui.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ke.co.ideagalore.olyxstore.R;
import ke.co.ideagalore.olyxstore.adapters.RecentSalesAdapter;
import ke.co.ideagalore.olyxstore.databinding.FragmentHomeBinding;
import ke.co.ideagalore.olyxstore.models.Expense;
import ke.co.ideagalore.olyxstore.models.Transaction;

public class HomeFragment extends Fragment implements View.OnClickListener {

    FragmentHomeBinding binding;

    ArrayList<Transaction> transactionArrayList;

    List<Expense> expenseList = new ArrayList<>();

    String store, name, terminal, terminalId, attendantStore, attendantId, attendantName;
    long dateToday;

    Transaction transaction;

    Expense expense;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getCurrentDate();
        getPreferenceData();

        transactionArrayList = new ArrayList<>();

        LocalDate localDate = LocalDate.now(ZoneOffset.UTC);
        dateToday = localDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();

        binding.cvSell.setOnClickListener(this);
        binding.cvCredit.setOnClickListener(this);
        binding.cvExpenditure.setOnClickListener(this);
        binding.tvAllTransactions.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == binding.cvSell) {
            Navigation.findNavController(view).navigate(R.id.sellFragment);
        } else if (view == binding.tvAllTransactions) {
            Navigation.findNavController(view).navigate(R.id.transactionsFragment);
        } else if (view == binding.cvExpenditure) {
            Navigation.findNavController(view).navigate(R.id.expenditureFragment);
        } else {
            Navigation.findNavController(view).navigate(R.id.creditFragment);
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

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(terminal).child("Transactions").child("Sales");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                transactionArrayList.clear();

                for (DataSnapshot transactionSnapshot : snapshot.getChildren()) {

                    transaction = transactionSnapshot.getValue(Transaction.class);
                    transactionArrayList.add(0, transaction);

                    int sales = 0;
                    int gasRefillSales = 0;
                    int gasSales = 0;
                    int accessorySales = 0;

                    List<Transaction> soldItems = new ArrayList<>();

                    for (Transaction item : transactionArrayList) {

                        if (item.getDate()==dateToday && item.getStore().equals(store)) {

                            soldItems.add(item);

                            int transactions = soldItems.size();
                            binding.tvTransactions.setText(transactions + "");

                            displayList(soldItems);


                            sales = sales + item.getTotalPrice();
                            binding.tvSales.setText("KES " + sales);

                            if (item.getTransactionType().equals("Gas refill")) {

                                gasRefillSales = gasRefillSales + item.getTotalPrice();
                                binding.tvRefillSales.setText("KES " + gasRefillSales);

                            }

                            if (item.getTransactionType().equals("Gas sale")) {

                                gasSales = gasSales + item.getTotalPrice();
                                binding.tvGasSales.setText("KES " + gasSales);

                            }

                            if (item.getTransactionType().equals("Accessory sale")) {

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

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(terminal).child("Transactions").child("Expenditure");
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

                            if (item.getDate()==dateToday) {
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

    private void displayList(List<Transaction> list) {
        binding.rvTransactions.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvTransactions.setHasFixedSize(true);
        RecentSalesAdapter adapter = new RecentSalesAdapter(list);
        binding.rvTransactions.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void getPreferenceData() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("Terminal", MODE_PRIVATE);
        store = sharedPreferences.getString("store", null);
        terminal = sharedPreferences.getString("terminal", null);
        name = sharedPreferences.getString("name", null);

        if (TextUtils.isEmpty(name) && TextUtils.isEmpty(terminal) && TextUtils.isEmpty(store)) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            attendantId = auth.getUid();
            getTerminalData(attendantId);

        } else {
            binding.tvDash.setText(store + ",");
            getTransactionsData();
            getExpenditureData();
        }

    }

    private void getTerminalData(String attendantId) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Attendants").child(attendantId);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                attendantStore = snapshot.child("store").getValue(String.class);
                attendantName = snapshot.child("attendant").getValue(String.class);
                terminalId = snapshot.child("terminal").getValue(String.class);
                savePreferencesData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void savePreferencesData() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("Terminal", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", attendantName);
        editor.putString("store", attendantStore);
        editor.putString("terminal", terminalId);
        editor.commit();
    }
}