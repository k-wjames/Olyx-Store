package ke.co.ideagalore.olyxstore.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import java.util.ArrayList;
import java.util.Date;

import ke.co.ideagalore.olyxstore.R;
import ke.co.ideagalore.olyxstore.adapters.TransactionsAdapter;
import ke.co.ideagalore.olyxstore.databinding.FragmentTransactionsBinding;
import ke.co.ideagalore.olyxstore.models.SaleItem;

public class TransactionsFragment extends Fragment implements View.OnClickListener {

    FragmentTransactionsBinding binding;
    ArrayList<SaleItem> saleItemArrayList;
    String dateToday;

    TransactionsAdapter adapter;

    SaleItem saleItem;

    public TransactionsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentTransactionsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        saleItemArrayList = new ArrayList<>();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        dateToday = formatter.format(date);
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
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Sales");
        binding.progressBar.setVisibility(View.VISIBLE);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                binding.progressBar.setVisibility(View.GONE);

                for (DataSnapshot transactionSnapshot : snapshot.getChildren()) {

                    saleItem = transactionSnapshot.getValue(SaleItem.class);
                    saleItemArrayList.add(saleItem);

                    if (saleItemArrayList.size() < 1) {
                        binding.animationView.setVisibility(View.VISIBLE);
                    } else {

                        for (SaleItem item : saleItemArrayList) {

                            String date = item.getDate();

                            if (date.equals(dateToday)) {

                                displayList();

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

    private void displayList() {
        binding.rvTransactions.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvTransactions.setHasFixedSize(true);
        TransactionsAdapter adapter = new TransactionsAdapter(getActivity(), saleItemArrayList);
        binding.rvTransactions.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void searchProduct(String item) {
        ArrayList<SaleItem> filteredList = new ArrayList<>();
        for (SaleItem object : saleItemArrayList) {
            if (object.getSaleType().toLowerCase().contains(item.toLowerCase())) {
                filteredList.add(object);
            }
        }
        binding.rvTransactions.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvTransactions.setHasFixedSize(true);
        adapter = new TransactionsAdapter(getActivity(), filteredList);
        binding.rvTransactions.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        Navigation.findNavController(view).navigate(R.id.homeFragment);
    }
}