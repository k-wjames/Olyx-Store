package ke.co.ideagalore.olyxstore;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ke.co.ideagalore.olyxstore.adapters.ProductAdapter;
import ke.co.ideagalore.olyxstore.databinding.FragmentStockBinding;
import ke.co.ideagalore.olyxstore.models.Product;

public class StockFragment extends Fragment {

    FragmentStockBinding binding;
    ArrayList<Product> productArrayList;
    Product product;
    ProductAdapter adapter;

    public StockFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentStockBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        productArrayList=new ArrayList<>();
        getStockData();
    }

    private void getStockData() {

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Stock");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot stockSnapshot:snapshot.getChildren()){

                    product=stockSnapshot.getValue(Product.class);
                    productArrayList.add(product);

                    if (productArrayList.size()==0){
                        binding.animationView.setVisibility(View.VISIBLE);
                    }
                }


                binding.rvStock.setLayoutManager(new LinearLayoutManager(getActivity()));
                binding.rvStock.setHasFixedSize(true);
                adapter=new ProductAdapter(getActivity(),productArrayList);
                binding.rvStock.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}