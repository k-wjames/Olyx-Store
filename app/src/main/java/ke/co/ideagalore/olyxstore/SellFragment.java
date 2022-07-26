package ke.co.ideagalore.olyxstore;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ke.co.ideagalore.olyxstore.adapters.SaleAdapter;
import ke.co.ideagalore.olyxstore.databinding.FragmentSellBinding;
import ke.co.ideagalore.olyxstore.models.Catalogue;
import ke.co.ideagalore.olyxstore.models.SaleItem;
import ke.co.ideagalore.olyxstore.models.TestItem;

public class SellFragment extends Fragment implements View.OnClickListener {

    FragmentSellBinding binding;

    ArrayList<TestItem> myGasArray, myAccessoriesArray, myGasRefillArray;
    ArrayList<SaleItem> myTransactionArray = new ArrayList<>();

    TestItem testItem;

    int price, markedPrice, buyingPrice;

    String transactionType, selectedItem, dateToday;

    SaleItem saleItem, newItem;

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
        myGasRefillArray = new ArrayList<>();

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        dateToday = formatter.format(date);

        getGasRefillData();
        getCatalogueData();

        binding.llRefill.setOnClickListener(this);
        binding.llBuyGas.setOnClickListener(this);
        binding.llBuyAccessory.setOnClickListener(this);
        binding.btnCheckOut.setOnClickListener(this);

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

                        } else if (category.equals("Accessory")) {
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

    private void getGasRefillData() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Refill");
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
                        int buyingPrice = catalogueArrayList.get(i).getBuyingPrice();

                        testItem = new TestItem();
                        testItem.setMarkedPrice(price);
                        testItem.setProduct(prod);
                        testItem.setBuyingPrice(buyingPrice);
                        myGasRefillArray.add(testItem);

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {
        if (view == binding.llRefill) {
            transactionType = "Gas refill";
            refillGasDialog(transactionType);
        } else if (view == binding.llBuyGas) {
            transactionType = "Gas sale";
            sellNewGasDialog(transactionType);

        } else if (view == binding.llBuyAccessory) {
            transactionType = "Accessory sale";
            sellAnAccessoryDialog(transactionType);
        } else if (view == binding.btnCheckOut) {

            if (myTransactionArray.size() > 0) {

                for (int b = 0; b < myTransactionArray.size(); b++) {
                    newItem = myTransactionArray.get(b);
                    String key = newItem.getSaleId();

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Sales");
                    ref.child(key).setValue(newItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                myTransactionArray.clear();
                                binding.tvTotalSpend.setText("0.00");
                                displayList();
                            }

                        }
                    });

                }
            } else {
                Toast.makeText(getActivity(), "Empty cart", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void refillGasDialog(String transType) {

        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.refill_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        EditText edtQuantity = dialog.findViewById(R.id.edt_quantity);
        EditText edtPrice = dialog.findViewById(R.id.edt_selling_price);


        Spinner spinner = dialog.findViewById(R.id.spinner_product);

        ArrayAdapter<TestItem> arrayAdapter = new ArrayAdapter<TestItem>(getActivity(), android.R.layout.simple_spinner_item, myGasRefillArray);
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


            String quantity = edtQuantity.getText().toString();
            int unitsSold;
            if (TextUtils.isEmpty(quantity)) {
                unitsSold = 1;
            } else {
                unitsSold = Integer.parseInt(quantity);
            }

            int pricePerUnit = Integer.parseInt(edtPrice.getText().toString());
            int totalPrice = unitsSold * pricePerUnit;
            int profit = totalPrice - (buyingPrice * unitsSold);

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Sales");
            String salesKey = ref.push().getKey();

            DateFormat formatter = new SimpleDateFormat("hh:mm:ss a");
            String time = formatter.format(new Date());

            saleItem = new SaleItem();
            saleItem.setProduct(selectedItem);
            saleItem.setQuantity(unitsSold);
            saleItem.setTotalPrice(totalPrice);
            saleItem.setSaleId(salesKey);
            saleItem.setTime(time);
            saleItem.setShop("Mwimuto");
            saleItem.setDate(dateToday);
            saleItem.setProfit(profit);
            saleItem.setSaleType(transType);
            myTransactionArray.add(saleItem);
            int totalShillings = 0;
            for (int i = 0; i < myTransactionArray.size(); i++) {

                totalShillings = +totalShillings + myTransactionArray.get(i).getTotalPrice();
                binding.tvTotalSpend.setText(totalShillings + "");
            }
            displayList();

        });
    }

    private void sellNewGasDialog(String transType) {

        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.sell_gas_dialog);
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


            String quantity = edtQuantity.getText().toString();
            int unitsSold;
            if (TextUtils.isEmpty(quantity)) {
                unitsSold = 1;
            } else {
                unitsSold = Integer.parseInt(quantity);
            }

            int pricePerUnit = Integer.parseInt(edtPrice.getText().toString());
            int totalPrice = unitsSold * pricePerUnit;
            int profit = totalPrice - (buyingPrice * unitsSold);

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Sales");
            String salesKey = ref.push().getKey();

            DateFormat formatter = new SimpleDateFormat("hh:mm:ss a");
            String time = formatter.format(new Date());

            saleItem = new SaleItem();
            saleItem.setProduct(selectedItem);
            saleItem.setQuantity(unitsSold);
            saleItem.setTotalPrice(totalPrice);
            saleItem.setSaleId(salesKey);
            saleItem.setTime(time);
            saleItem.setShop("Mwimuto");
            saleItem.setDate(dateToday);
            saleItem.setProfit(profit);
            saleItem.setSaleType(transType);
            myTransactionArray.add(saleItem);
            int totalShillings = 0;
            for (int i = 0; i < myTransactionArray.size(); i++) {

                totalShillings = +totalShillings + myTransactionArray.get(i).getTotalPrice();
                binding.tvTotalSpend.setText(totalShillings + "");
            }
            displayList();

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


            String quantity = edtQuantity.getText().toString();
            int unitsSold;
            if (TextUtils.isEmpty(quantity)) {
                unitsSold = 1;
            } else {
                unitsSold = Integer.parseInt(quantity);
            }

            int pricePerUnit = Integer.parseInt(edtPrice.getText().toString());
            int totalPrice = unitsSold * pricePerUnit;
            int profit = totalPrice - (buyingPrice * unitsSold);

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Sales");
            String salesKey = ref.push().getKey();

            DateFormat formatter = new SimpleDateFormat("hh:mm:ss a");
            String time = formatter.format(new Date());

            saleItem = new SaleItem();
            saleItem.setProduct(selectedItem);
            saleItem.setQuantity(unitsSold);
            saleItem.setTotalPrice(totalPrice);
            saleItem.setSaleId(salesKey);
            saleItem.setTime(time);
            saleItem.setShop("Mwimuto");
            saleItem.setDate(dateToday);
            saleItem.setProfit(profit);
            saleItem.setSaleType(transType);
            myTransactionArray.add(saleItem);
            int totalShillings = 0;
            for (int i = 0; i < myTransactionArray.size(); i++) {

                totalShillings =+ totalShillings + myTransactionArray.get(i).getTotalPrice();
                binding.tvTotalSpend.setText(totalShillings + "");
            }
            displayList();

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