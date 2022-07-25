package ke.co.ideagalore.olyxstore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ke.co.ideagalore.olyxstore.adapters.SaleAdapter;
import ke.co.ideagalore.olyxstore.adapters.TransactionsAdapter;
import ke.co.ideagalore.olyxstore.databinding.FragmentTransactionsBinding;
import ke.co.ideagalore.olyxstore.models.SaleItem;

public class TransactionsFragment extends Fragment {

    FragmentTransactionsBinding binding;
    ArrayList<SaleItem> saleItemArrayList;
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

        saleItemArrayList=new ArrayList<>();
        getTransactionsData();
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
        TransactionsAdapter adapter = new TransactionsAdapter(getActivity(), saleItemArrayList);
        binding.rvTransactions.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}