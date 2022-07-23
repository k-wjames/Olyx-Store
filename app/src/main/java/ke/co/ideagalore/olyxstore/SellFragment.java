package ke.co.ideagalore.olyxstore;

import android.app.Dialog;
import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ke.co.ideagalore.olyxstore.adapters.SaleAdapter;
import ke.co.ideagalore.olyxstore.databinding.FragmentSellBinding;
import ke.co.ideagalore.olyxstore.models.Catalogue;
import ke.co.ideagalore.olyxstore.models.SaleItem;
import ke.co.ideagalore.olyxstore.models.TestItem;
import ke.co.ideagalore.olyxstore.models.Transaction;

public class SellFragment extends Fragment implements View.OnClickListener {

    FragmentSellBinding binding;

    ArrayList<TestItem> myGasArray, myAccessoriesArray;
    ArrayList<SaleItem> myTransactionArray = new ArrayList<>();

    TestItem testItem;

    int price, markedPrice, buyingPrice, sellingPrice, totalSellingPrice, sellingQuantity;

    String transactionType, selectedItem, dateToday;

    Dialog dialog;

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

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        dateToday = formatter.format(date);


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
                        int buyingPrice = catalogueArrayList.get(i).getBuyingPrice();

                        testItem = new TestItem();
                        if (category.equals("Gas")) {

                            testItem.setMarkedPrice(price);
                            testItem.setProduct(prod);
                            myGasArray.add(testItem);

                        } else {
                            testItem.setMarkedPrice(price);
                            testItem.setProduct(prod);
                            testItem.setBuyingPrice(buyingPrice);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {
        if (view == binding.llRefill) {
            transactionType = "refill";
            refillGasDialog(transactionType);
        } else if (view == binding.llBuyGas) {
            transactionType = "sale";
            sellNewGasDialog(transactionType);

        } else if (view == binding.llBuyAccessory) {
            transactionType = "sale";
            sellAnAccessoryDialog(transactionType);
        }
    }

    private void sellNewGasDialog(String transType) {

        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.sell_accessory_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        EditText edtQuantity = dialog.findViewById(R.id.edt_quantity);
        EditText edtPrice = dialog.findViewById(R.id.edt_selling_price);


        Spinner spinner = dialog.findViewById(R.id.spinner_product);

        ArrayAdapter<TestItem> arrayAdapter = new ArrayAdapter<TestItem>(getActivity(), android.R.layout.simple_spinner_item, myGasArray);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = spinner.getSelectedItem().toString();
                TestItem item = (TestItem) spinner.getSelectedItem();
                markedPrice = item.getMarkedPrice();
                buyingPrice = item.getBuyingPrice();
                edtPrice.setText(markedPrice + "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner.setAdapter(arrayAdapter);

        TextView cancel = dialog.findViewById(R.id.tv_cancel);
        cancel.setOnClickListener(view -> dialog.dismiss());

        Button save = dialog.findViewById(R.id.btn_add);
        save.setOnClickListener(view -> {

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Transactions");
            String transactId = reference.push().getKey();

            int unitsSold = Integer.parseInt(edtQuantity.getText().toString());
            int pricePerUnit = Integer.parseInt(edtPrice.getText().toString());
            int totalPrice = unitsSold * pricePerUnit;

            Transaction transaction = new Transaction();
            transaction.setTransactionId(transactId);
            transaction.setTransactionType(transType);
            transaction.setPrice(totalPrice);
            transaction.setProduct(selectedItem);
            transaction.setQuantity(unitsSold);
            transaction.setDate(dateToday);
            transaction.setProfit(totalPrice - (buyingPrice * unitsSold));
            transaction.setShop("Mwimuto");

            SaleItem saleItem = new SaleItem();
            saleItem.setProduct(selectedItem);
            saleItem.setQuantity(unitsSold);
            saleItem.setTotalPrice(totalPrice);
            myTransactionArray.add(saleItem);
            int totalShillings = 0;
            for (int i = 0; i < myTransactionArray.size(); i++) {

                totalShillings = +totalShillings + myTransactionArray.get(i).getTotalPrice();
                binding.tvTotalSpend.setText(totalShillings + "");
            }
            displayList();

           /* reference.child(transactId).setValue(transaction).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()){
                        dialog.dismiss();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(getActivity(), "Oops! Something went wrong. Try again.", Toast.LENGTH_SHORT).show();

                }
            });*/


        });
    }

    private void refillGasDialog(String transType) {

        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.sell_accessory_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        EditText edtQuantity = dialog.findViewById(R.id.edt_quantity);
        EditText edtPrice = dialog.findViewById(R.id.edt_selling_price);


        Spinner spinner = dialog.findViewById(R.id.spinner_product);

        ArrayAdapter<TestItem> arrayAdapter = new ArrayAdapter<TestItem>(getActivity(), android.R.layout.simple_spinner_item, myGasArray);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = spinner.getSelectedItem().toString();
                TestItem item = (TestItem) spinner.getSelectedItem();
                markedPrice = item.getMarkedPrice();
                buyingPrice = item.getBuyingPrice();
                edtPrice.setText(markedPrice + "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner.setAdapter(arrayAdapter);

        TextView cancel = dialog.findViewById(R.id.tv_cancel);
        cancel.setOnClickListener(view -> dialog.dismiss());

        Button save = dialog.findViewById(R.id.btn_add);
        save.setOnClickListener(view -> {

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Transactions");
            String transactId = reference.push().getKey();

            int unitsSold = Integer.parseInt(edtQuantity.getText().toString());
            int pricePerUnit = Integer.parseInt(edtPrice.getText().toString());
            int totalPrice = unitsSold * pricePerUnit;

            Transaction transaction = new Transaction();
            transaction.setTransactionId(transactId);
            transaction.setTransactionType(transType);
            transaction.setPrice(totalPrice);
            transaction.setProduct(selectedItem);
            transaction.setQuantity(unitsSold);
            transaction.setDate(dateToday);
            transaction.setProfit(totalPrice - (buyingPrice * unitsSold));
            transaction.setShop("Mwimuto");

            SaleItem saleItem = new SaleItem();
            saleItem.setProduct(selectedItem);
            saleItem.setQuantity(unitsSold);
            saleItem.setTotalPrice(totalPrice);
            myTransactionArray.add(saleItem);
            int totalShillings = 0;
            for (int i = 0; i < myTransactionArray.size(); i++) {

                totalShillings = +totalShillings + myTransactionArray.get(i).getTotalPrice();
                binding.tvTotalSpend.setText(totalShillings + "");
            }
            displayList();

           /* reference.child(transactId).setValue(transaction).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()){
                        dialog.dismiss();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(getActivity(), "Oops! Something went wrong. Try again.", Toast.LENGTH_SHORT).show();

                }
            });*/


        });
    }

    private void sellAnAccessoryDialog(String transType) {

        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.sell_accessory_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        EditText edtQuantity = dialog.findViewById(R.id.edt_quantity);
        EditText edtPrice = dialog.findViewById(R.id.edt_selling_price);


        Spinner spinner = dialog.findViewById(R.id.spinner_product);

        ArrayAdapter<TestItem> arrayAdapter = new ArrayAdapter<TestItem>(getActivity(), android.R.layout.simple_spinner_item, myAccessoriesArray);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = spinner.getSelectedItem().toString();
                TestItem item = (TestItem) spinner.getSelectedItem();
                markedPrice = item.getMarkedPrice();
                buyingPrice = item.getBuyingPrice();
                edtPrice.setText(markedPrice + "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner.setAdapter(arrayAdapter);

        TextView cancel = dialog.findViewById(R.id.tv_cancel);
        cancel.setOnClickListener(view -> dialog.dismiss());

        Button save = dialog.findViewById(R.id.btn_add);
        save.setOnClickListener(view -> {

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Transactions");
            String transactId = reference.push().getKey();

            int unitsSold = Integer.parseInt(edtQuantity.getText().toString());
            int pricePerUnit = Integer.parseInt(edtPrice.getText().toString());
            int totalPrice = unitsSold * pricePerUnit;

            Transaction transaction = new Transaction();
            transaction.setTransactionId(transactId);
            transaction.setTransactionType(transType);
            transaction.setPrice(totalPrice);
            transaction.setProduct(selectedItem);
            transaction.setQuantity(unitsSold);
            transaction.setDate(dateToday);
            transaction.setProfit(totalPrice - (buyingPrice * unitsSold));
            transaction.setShop("Mwimuto");

            SaleItem saleItem = new SaleItem();
            saleItem.setProduct(selectedItem);
            saleItem.setQuantity(unitsSold);
            saleItem.setTotalPrice(totalPrice);
            myTransactionArray.add(saleItem);
            int totalShillings = 0;
            for (int i = 0; i < myTransactionArray.size(); i++) {

                totalShillings = +totalShillings + myTransactionArray.get(i).getTotalPrice();
                binding.tvTotalSpend.setText(totalShillings + "");
            }
            displayList();

           /* reference.child(transactId).setValue(transaction).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()){
                        dialog.dismiss();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(getActivity(), "Oops! Something went wrong. Try again.", Toast.LENGTH_SHORT).show();

                }
            });*/


        });
    }

    private void displayList() {
        binding.rvSales.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvSales.setHasFixedSize(true);
        SaleAdapter adapter = new SaleAdapter(getActivity(), myTransactionArray);
        binding.rvSales.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        dialog.dismiss();
    }
}