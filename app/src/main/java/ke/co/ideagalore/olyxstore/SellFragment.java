package ke.co.ideagalore.olyxstore;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ke.co.ideagalore.olyxstore.databinding.FragmentSellBinding;

public class SellFragment extends Fragment {

    FragmentSellBinding binding;

    public SellFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentSellBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
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

    }

    private void searchProduct(String item) {
        /*ArrayList<Product> filteredList = new ArrayList<>();
        for (Product object : recyclerViewItems) {
            if (object.getProduct().toLowerCase().contains(item.toLowerCase())) {
                filteredList.add(object);
            }
        }
        iFirebaseLoadListener.onFirebaseLoadSuccess(filteredList);*/
    }
}