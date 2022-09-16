package ke.co.ideagalore.olyxstore.ui.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ke.co.ideagalore.olyxstore.R;
import ke.co.ideagalore.olyxstore.adapters.CreditAdapter;
import ke.co.ideagalore.olyxstore.commons.ValidateFields;
import ke.co.ideagalore.olyxstore.databinding.FragmentCreditBinding;
import ke.co.ideagalore.olyxstore.models.Credit;

public class CreditFragment extends Fragment implements View.OnClickListener {

    FragmentCreditBinding binding;

    ValidateFields validator = new ValidateFields();

    List<Credit> creditList = new ArrayList<>();

    String dateToday, time;

    public CreditFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreditBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        dateToday = formatter.format(date);

        DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
        time = timeFormat.format(new Date());

        getCreditorsData();

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

        binding.fabCredit.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == binding.fabCredit) {
            showCreditDialog();
        } else {
            Navigation.findNavController(view).navigate(R.id.homeFragment);
        }
    }

    private void searchProduct(String item) {
        ArrayList<Credit> filteredList = new ArrayList<>();
        for (Credit object : creditList) {
            if (object.getPhone().toLowerCase().contains(item.toLowerCase())) {
                filteredList.add(object);
            }
        }
        displayData(filteredList);
    }

    private void getCreditorsData() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Creditors");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                creditList.clear();

                for (DataSnapshot creditSnapshot : snapshot.getChildren()) {

                    Credit credit = creditSnapshot.getValue(Credit.class);
                    creditList.add(0, credit);
                }

                displayData(creditList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void displayData(List<Credit> creditList) {
        CreditAdapter adapter = new CreditAdapter(creditList, getActivity());
        binding.rvCreditors.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvCreditors.setHasFixedSize(true);
        binding.rvCreditors.setAdapter(adapter);
    }

    private void showCreditDialog() {
        Dialog myDialog = new Dialog(getActivity());
        myDialog.setContentView(R.layout.credit_dialog);
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        myDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        myDialog.show();

        EditText product, quantity, amount, name, phone;
        product = myDialog.findViewById(R.id.edt_product);
        quantity = myDialog.findViewById(R.id.edt_quantity);
        amount = myDialog.findViewById(R.id.edt_amount);
        name = myDialog.findViewById(R.id.edt_customer);
        phone = myDialog.findViewById(R.id.edt_phone);

        ProgressBar progressBar = myDialog.findViewById(R.id.progress_bar);

        Button save = myDialog.findViewById(R.id.btn_add_credit);
        TextView cancel = myDialog.findViewById(R.id.tv_cancel);

        cancel.setOnClickListener(view -> myDialog.dismiss());

        save.setOnClickListener(view -> {

            if (validator.validateEditTextFields(requireActivity(), product)
                    && validator.validateEditTextFields(requireActivity(), quantity)
                    && validator.validateEditTextFields(requireActivity(), amount)
                    && validator.validateEditTextFields(requireActivity(), name)
                    && validator.validateEditTextFields(requireActivity(), phone)) {
                progressBar.setVisibility(View.VISIBLE);
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Creditors");
                String key = reference.push().getKey();
                Credit credit = new Credit();
                credit.setCreditId(key);
                credit.setDate(dateToday);
                credit.setTime(time);
                credit.setProduct(product.getText().toString().trim());
                credit.setQuantity(quantity.getText().toString().trim());
                credit.setAmount(amount.getText().toString().trim());
                credit.setName(name.getText().toString().trim());
                credit.setPhone(phone.getText().toString().trim());

                assert key != null;
                reference.child(key).setValue(credit).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        myDialog.dismiss();
                    }
                }).addOnFailureListener(e -> {

                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Oops! Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();

                });
            }
        });
    }

}