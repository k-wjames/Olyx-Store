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

import java.util.ArrayList;

import ke.co.ideagalore.olyxstore.R;
import ke.co.ideagalore.olyxstore.adapters.StockAdapter;
import ke.co.ideagalore.olyxstore.databinding.FragmentStockBinding;
import ke.co.ideagalore.olyxstore.models.StockItem;

public class StockFragment extends Fragment implements View.OnClickListener {

    FragmentStockBinding binding;
    ArrayList<StockItem> stockArrayList;
    StockItem stockItem;
    StockAdapter adapter;

    public StockFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStockBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        stockArrayList = new ArrayList<>();
        getStockData();
        binding.ivBack.setOnClickListener(this);
    }

    private void getStockData() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Catalogue");
        binding.progressBar.setVisibility(View.VISIBLE);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot stockSnapshot : snapshot.getChildren()) {

                    binding.progressBar.setVisibility(View.GONE);

                    stockItem = stockSnapshot.getValue(StockItem.class);
                    stockArrayList.add(stockItem);

                    if (stockArrayList.size() == 0) {
                        binding.animationView.setVisibility(View.VISIBLE);
                    }
                }


                binding.rvStock.setLayoutManager(new LinearLayoutManager(getActivity()));
                binding.rvStock.setHasFixedSize(true);
                adapter = new StockAdapter(getActivity(), stockArrayList);
                binding.rvStock.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(View view) {
        Navigation.findNavController(view).navigate(R.id.homeFragment);
    }
}