package ke.co.ideagalore.olyxstore.ui.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import ke.co.ideagalore.olyxstore.adapters.ExpenseAdapter;
import ke.co.ideagalore.olyxstore.databinding.FragmentExpenditureBinding;
import ke.co.ideagalore.olyxstore.models.Expense;

public class ExpenditureFragment extends Fragment implements View.OnClickListener {

    FragmentExpenditureBinding binding;
    String dateToday;

    List<Expense> expenseList = new ArrayList<>();

    ExpenseAdapter adapter;

    Dialog dialog;

    public ExpenditureFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentExpenditureBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        dateToday = formatter.format(date);

        getExpenditureData();

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

        binding.fabExpense.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        if (view == binding.fabExpense)
            showAddExpenseDialog();
        else Navigation.findNavController(view).navigate(R.id.homeFragment);
    }

    private void searchProduct(String newText) {

        List<Expense> filteredList = new ArrayList<>();
        for (Expense object : expenseList) {
            if (object.getExpense().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(object);
            }
        }
        binding.rvExpenditure.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvExpenditure.setHasFixedSize(true);
        adapter = new ExpenseAdapter(getActivity(), filteredList);
        binding.rvExpenditure.setAdapter(adapter);
    }

    private void showAddExpenseDialog() {
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.add_expense_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        TextView cancel = dialog.findViewById(R.id.tv_cancel);
        cancel.setOnClickListener(view -> dialog.dismiss());

        EditText expense, description, quantity, cost;
        expense = dialog.findViewById(R.id.edt_expense);
        description = dialog.findViewById(R.id.edt_description);
        quantity = dialog.findViewById(R.id.edt_quantity);
        cost = dialog.findViewById(R.id.edt_cost);

        Button save = dialog.findViewById(R.id.btn_add_expense);
        save.setOnClickListener(view -> {
            String expenseQuantity, expenseItem, expenseDescription, totalCost;
            expenseItem = expense.getText().toString();
            expenseDescription = description.getText().toString();
            expenseQuantity = quantity.getText().toString();
            totalCost = cost.getText().toString();

            if (TextUtils.isEmpty(expenseItem)) {
                Toast.makeText(getActivity(), "Expense field cannot be empty.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(expenseDescription)) {
                Toast.makeText(getActivity(), "Expense description field cannot be empty.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(expenseQuantity)) {
                quantity.setText("1");
                return;
            }

            if (TextUtils.isEmpty(totalCost)) {
                Toast.makeText(getActivity(), "Amount field cannot be empty.", Toast.LENGTH_SHORT).show();
                return;
            }

            addNewExpenditure(expenseItem, expenseDescription, expenseQuantity, totalCost);
        });

    }

    private void addNewExpenditure(String expenseItem, String expenseDescription, String expenseQuantity, String totalCost) {

        int unitsSold;
        if (TextUtils.isEmpty(expenseQuantity)) {
            unitsSold = 1;
        } else {
            unitsSold = Integer.parseInt(expenseQuantity);
        }

        DateFormat formatter = new SimpleDateFormat("hh:mm:ss a");
        String time = formatter.format(new Date());
        String expenseId;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Expenditure");
        expenseId = ref.push().getKey();
        Expense exp = new Expense();
        exp.setExpenseId(expenseId);
        exp.setExpense(expenseItem);
        exp.setDescription(expenseDescription);
        exp.setPrice(Integer.parseInt(totalCost));
        exp.setDate(dateToday);
        exp.setTime(time);
        exp.setQuantity(unitsSold);

        ref.child(expenseId).setValue(exp).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                getExpenditureData();
                dialog.dismiss();

            } else {
                Toast.makeText(getActivity(), "Failed to add expense. Please try again.", Toast.LENGTH_SHORT).show();
            }

        });

    }


    private void getExpenditureData() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Expenditure");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                expenseList.clear();
                for (DataSnapshot expenseSnapshot : snapshot.getChildren()) {
                    Expense expense = expenseSnapshot.getValue(Expense.class);
                    expenseList.add(0, expense);

                    List<Expense> expenseIncurred = new ArrayList<>();

                    for (Expense exp : expenseList) {
                        String date = exp.getDate();

                        if (date.equals(dateToday))
                            expenseIncurred.add(exp);

                        displayExpenses(expenseIncurred);
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void displayExpenses(List<Expense> expenseIncurred) {
        binding.rvExpenditure.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvExpenditure.setHasFixedSize(true);
        adapter = new ExpenseAdapter(getActivity(), expenseIncurred);
        binding.rvExpenditure.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}