package ke.co.ideagalore.olyxstore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ke.co.ideagalore.olyxstore.adapters.SellAdapter;
import ke.co.ideagalore.olyxstore.databinding.FragmentSellBinding;
import ke.co.ideagalore.olyxstore.models.Product;

public class SellFragment extends Fragment {

    FragmentSellBinding binding;

    ArrayList<Product> productArrayList;
    Product product;
    SellAdapter adapter;

    public SellFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSellBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        productArrayList = new ArrayList<>();
        getStockItems();

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
    }

    private void getStockItems() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Stock");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot stockSnapshot : snapshot.getChildren()) {

                    product = stockSnapshot.getValue(Product.class);
                    productArrayList.add(0,product);

                }

                binding.rvStock.setLayoutManager(new LinearLayoutManager(getActivity()));
                binding.rvStock.setHasFixedSize(true);
                adapter = new SellAdapter(getActivity(), productArrayList);
                binding.rvStock.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void searchProduct(String item) {
        ArrayList<Product> filteredList = new ArrayList<>();
        for (Product object : productArrayList) {
            if (object.getBrand().toLowerCase().contains(item.toLowerCase())) {
                filteredList.add(object);
            }
        }
        binding.rvStock.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvStock.setHasFixedSize(true);
        adapter = new SellAdapter(getActivity(), filteredList);
        binding.rvStock.setAdapter(adapter);
    }
}