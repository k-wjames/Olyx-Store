package ke.co.ideagalore.olyxstore;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ke.co.ideagalore.olyxstore.databinding.FragmentSellBinding;
import ke.co.ideagalore.olyxstore.models.Catalogue;
import ke.co.ideagalore.olyxstore.models.Product;
import ke.co.ideagalore.olyxstore.models.TestItem;

public class SellFragment extends Fragment implements View.OnClickListener {

    FragmentSellBinding binding;

    ArrayList<TestItem> myGasArray,myAccessoriesArray;

    TestItem testItem;

    int price, sellingPrice;

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
        myGasArray = new ArrayList<>();
        myAccessoriesArray = new ArrayList<>();
        getCatalogueData();
        binding.llRefill.setOnClickListener(this);
        binding.llBuyGas.setOnClickListener(this);
        binding.llBuyAccessory.setOnClickListener(this);


       /* binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchProduct(newText);
                return true;
            }
        });*/
    }

    private void getCatalogueData() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Catalogue");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot catalogueSnapshot : snapshot.getChildren()) {
                    Catalogue catalogue = catalogueSnapshot.getValue(Catalogue.class);

                    ArrayList<Catalogue> catalogueArrayList = new ArrayList<>();
                    catalogueArrayList.add(catalogue);

                    for (int i = 0; i < catalogueArrayList.size(); i++) {

                        String prod = catalogueArrayList.get(i).getProduct();
                        price = catalogueArrayList.get(i).getSellingPrice();
                        String category = catalogueArrayList.get(i).getCategory();

                            testItem = new TestItem();
                            if (category.equals("Gas")){

                                testItem.setPrice(price);
                                testItem.setProduct(prod);
                                myGasArray.add(testItem);

                            }else {
                                testItem.setPrice(price);
                                testItem.setProduct(prod);
                                myAccessoriesArray.add(testItem);
                            }


                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getActivity(), "Oops! Something went wrong. Be sure it's not you.", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void searchProduct(String item) {
         /* ArrayList<Product> filteredList = new ArrayList<>();
        for (Product object : productArrayList) {
            if (object.getBrand().toLowerCase().contains(item.toLowerCase())) {
                filteredList.add(object);
            }
        }
      binding.rvStock.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvStock.setHasFixedSize(true);
        adapter = new SellAdapter(getActivity(), filteredList);
        binding.rvStock.setAdapter(adapter);*/
    }

    @Override
    public void onClick(View view) {
        if (view == binding.llRefill) {

            showRefillDialog();
        } else if (view == binding.llBuyGas) {

            showSellNewGasDialog();

        } else if (view == binding.llBuyAccessory) {

            sellAnAccessoryDialog();
        }
    }

    private void sellAnAccessoryDialog() {

        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.sell_accessory_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        EditText quantity = dialog.findViewById(R.id.edt_quantity);
        quantity.setText(1 + "");

        EditText price = dialog.findViewById(R.id.edt_selling_price);


        Spinner spinner = dialog.findViewById(R.id.spinner_product);

        ArrayAdapter<TestItem> arrayAdapter = new ArrayAdapter<TestItem>(getActivity(), android.R.layout.simple_spinner_item, myAccessoriesArray);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TestItem item = (TestItem) spinner.getSelectedItem();
                sellingPrice = item.getPrice();
                price.setText(sellingPrice + "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner.setAdapter(arrayAdapter);

        TextView cancel = dialog.findViewById(R.id.tv_cancel);
        cancel.setOnClickListener(view -> dialog.dismiss());

        Button save = dialog.findViewById(R.id.btn_add);
        save.setOnClickListener(view -> dialog.dismiss());
    }

    private void showSellNewGasDialog() {

        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.sell_gas_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        final Product[] selectedProduct = new Product[1];
        EditText edtProduct = dialog.findViewById(R.id.edtProduct);
        Spinner spinner = dialog.findViewById(R.id.spinner_product);
        TextInputLayout tilProduct = dialog.findViewById(R.id.til_product);


        tilProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        selectedProduct[0] = (Product) adapterView.getAdapter().getItem(i);
                        edtProduct.setText(selectedProduct[0].getBrand());

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        });
        TextView cancel = dialog.findViewById(R.id.tv_cancel);
        cancel.setOnClickListener(view -> dialog.dismiss());

        Button save = dialog.findViewById(R.id.btn_add);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });
    }

    private void showRefillDialog() {

        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.refill_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        final Product[] selectedProduct = new Product[1];
        EditText edtProduct = dialog.findViewById(R.id.edtProduct);
        Spinner spinner = dialog.findViewById(R.id.spinner_product);
        TextInputLayout tilProduct = dialog.findViewById(R.id.til_product);


        tilProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        selectedProduct[0] = (Product) adapterView.getAdapter().getItem(i);
                        edtProduct.setText(selectedProduct[0].getBrand());

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        });
        TextView cancel = dialog.findViewById(R.id.tv_cancel);
        cancel.setOnClickListener(view -> dialog.dismiss());

        Button save = dialog.findViewById(R.id.btn_add);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });
    }
}