package ke.co.ideagalore.olyxstore.ui.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;

import ke.co.ideagalore.olyxstore.R;
import ke.co.ideagalore.olyxstore.adapters.RecentSalesAdapter;
import ke.co.ideagalore.olyxstore.adapters.TransactionsAdapter;
import ke.co.ideagalore.olyxstore.databinding.FragmentTransactionsBinding;
import ke.co.ideagalore.olyxstore.models.Transaction;

public class TransactionsFragment extends Fragment implements View.OnClickListener {

    FragmentTransactionsBinding binding;
    ArrayList<Transaction> transactionArrayList;
    String terminal;
    long dateToday;

    RecentSalesAdapter adapter;

    Transaction transaction;

    public TransactionsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentTransactionsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        transactionArrayList = new ArrayList<>();
        LocalDate localDate = LocalDate.now(ZoneOffset.UTC);
        dateToday = localDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();

        getPreferenceData();
        getTransactionsData();

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchProduct(newText);
                return true;
            }
        });

        binding.ivBack.setOnClickListener(this);
    }

    private void getTransactionsData() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(terminal).child("Transactions").child("Sales");
        binding.progressBar.setVisibility(View.VISIBLE);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                binding.progressBar.setVisibility(View.GONE);

                for (DataSnapshot transactionSnapshot : snapshot.getChildren()) {

                    transaction = transactionSnapshot.getValue(Transaction.class);
                    transactionArrayList.add(0, transaction);

                    if (transactionArrayList.size() == 0) {
                        binding.animationView.setVisibility(View.VISIBLE);
                    } else {

                        displayList();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void displayList() {
        binding.rvTransactions.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvTransactions.setHasFixedSize(true);
        RecentSalesAdapter adapter = new RecentSalesAdapter(transactionArrayList, new RecentSalesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Transaction transaction) {
                Toast.makeText(requireActivity(), transaction.getProduct(), Toast.LENGTH_SHORT).show();
            }
        } );
        binding.rvTransactions.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void searchProduct(String item) {
        ArrayList<Transaction> filteredList = new ArrayList<>();
        for (Transaction object : transactionArrayList) {
            if (object.getProduct().toLowerCase().contains(item.toLowerCase())) {
                filteredList.add(object);
            }
        }
        binding.rvTransactions.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvTransactions.setHasFixedSize(true);
        adapter = new RecentSalesAdapter(filteredList, new RecentSalesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Transaction transaction) {
                Toast.makeText(requireActivity(), transaction.getTransactionId(), Toast.LENGTH_SHORT).show();
            }
        });
        binding.rvTransactions.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        Navigation.findNavController(view).navigate(R.id.homeFragment);
    }

    private void getPreferenceData() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("Terminal", MODE_PRIVATE);
        terminal = sharedPreferences.getString("terminal", null);
    }
}