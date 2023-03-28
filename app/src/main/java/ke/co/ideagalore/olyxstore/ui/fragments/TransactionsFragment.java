package ke.co.ideagalore.olyxstore.ui.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ke.co.ideagalore.olyxstore.R;
import ke.co.ideagalore.olyxstore.adapters.RecentSalesAdapter;
import ke.co.ideagalore.olyxstore.commons.CustomDialogs;
import ke.co.ideagalore.olyxstore.databinding.FragmentTransactionsBinding;
import ke.co.ideagalore.olyxstore.models.Catalogue;
import ke.co.ideagalore.olyxstore.models.Transaction;

public class TransactionsFragment extends Fragment implements View.OnClickListener {

    FragmentTransactionsBinding binding;
    ArrayList<Transaction> transactionArrayList;
    String terminal;
    long dateToday;

    RecentSalesAdapter adapter;

    Transaction transaction;

    DatabaseReference reference,ref;

    CustomDialogs customDialogs = new CustomDialogs();

    public TransactionsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentTransactionsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        transactionArrayList = new ArrayList<>();
        LocalDate localDate = LocalDate.now(ZoneOffset.UTC);
        dateToday = localDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();

        getPreferenceData();

        reference = FirebaseDatabase.getInstance().getReference("Users").child(terminal).child("Transactions").child("Sales");
        ref=FirebaseDatabase.getInstance().getReference("Users").child(terminal).child("Catalogue");
        getTransactionsData();

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

        binding.ivBack.setOnClickListener(this);
    }

    private void getTransactionsData() {
        binding.progressBar.setVisibility(View.VISIBLE);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                binding.progressBar.setVisibility(View.GONE);

                for (DataSnapshot transactionSnapshot : snapshot.getChildren()) {

                    transaction = transactionSnapshot.getValue(Transaction.class);
                    transactionArrayList.add(0, transaction);

                    if (transactionArrayList.size() == 0) {
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
        RecentSalesAdapter adapter = new RecentSalesAdapter(transactionArrayList, new RecentSalesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Transaction transaction) {

                //showManageTransactionsDialog(transaction);
                //Toast.makeText(requireActivity(), transaction.getProduct(), Toast.LENGTH_SHORT).show();
            }
        });
        binding.rvTransactions.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void showManageTransactionsDialog(Transaction transaction) {

        Dialog myDialog = new Dialog(requireActivity());
        myDialog.setContentView(R.layout.manage_transaction_dialog);
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        myDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        myDialog.show();

        RelativeLayout rlUpdateTransaction = myDialog.findViewById(R.id.rl_update_transaction);
        RelativeLayout rlDeleteTransaction = myDialog.findViewById(R.id.rl_delete_transaction);

        ImageView imageView = myDialog.findViewById(R.id.iv_cancel);
        imageView.setOnClickListener(view -> myDialog.dismiss());

        rlUpdateTransaction.setOnClickListener(view -> {
           /* Bundle bundle = new Bundle();
            bundle.putString("productId", item.getProdId());
            bundle.putString("product", item.getProduct());
            bundle.putString("category", item.getCategory());
            bundle.putInt("stockedItems", item.getStockedQuantity());
            bundle.putInt("buyingPrice", item.getBuyingPrice());
            bundle.putInt("sellingPrice", item.getMarkedPrice());
            bundle.putString("shop", item.getShop());
            bundle.putInt("availableStock",item.getAvailableStock());
            bundle.putInt("soldItems", item.getSoldItems());
            myDialog.dismiss();
            Navigation.findNavController(CatalogueItemsFragment.this.requireView()).navigate(R.id.editProductFragment, bundle);*/
        });

        rlDeleteTransaction.setOnClickListener(view -> deleteProduct(transaction.getTransactionId(), transaction.getProductId(), transaction.getQuantity(), myDialog));

    }

    private void deleteProduct(String transactionId, String productId, int quantity, Dialog myDialog) {
        reference.child(transactionId).setValue(null).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                getCatalogueData(productId,quantity,myDialog);

            }

        }).addOnFailureListener(e -> customDialogs.showSnackBar(requireActivity(), e.getMessage()));
    }

    private void getCatalogueData(String productId, int quantity, Dialog myDialog) {
        myDialog.dismiss();
        ref.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Catalogue catalogue=snapshot.getValue(Catalogue.class);
                int available=catalogue.getAvailableStock();
                int soldItems=catalogue.getSoldItems();

                int updatedAvailable=available+quantity;
                int updatedSoldItems=soldItems-quantity;

                updateCatalogue(productId,updatedSoldItems,updatedAvailable, myDialog);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //customDialogs.showSnackBar(requireActivity(),"Updating "+productId+" with "+quantity);
    }

    private void updateCatalogue(String productId, int updatedSoldItems, int updatedAvailable, Dialog myDialog) {

        Map<String, Object> map=new HashMap<>();
        map.put("availableStock",updatedAvailable);
        map.put("soldItems",updatedSoldItems);

        DatabaseReference myRef=ref=FirebaseDatabase.getInstance().getReference("Users").child(terminal).child("Catalogue").child(productId);
        myRef.updateChildren(map).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                myDialog.dismiss();
                customDialogs.showSnackBar(requireActivity(), "Transaction deleted.");
                getTransactionsData();

            }
        }).addOnFailureListener(e -> customDialogs.showSnackBar(requireActivity(), e.getMessage()));

    }


    private void searchProduct(String item) {
        ArrayList<Transaction> filteredList = new ArrayList<>();
        for (Transaction object : transactionArrayList) {
            if (object.getProduct().toLowerCase().contains(item.toLowerCase())) {
                filteredList.add(object);
            }
        }
        binding.rvTransactions.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvTransactions.setHasFixedSize(true);
        adapter = new RecentSalesAdapter(filteredList, new RecentSalesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Transaction transaction) {
                Toast.makeText(requireActivity(), transaction.getTransactionId(), Toast.LENGTH_SHORT).show();
            }
        });
        binding.rvTransactions.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        Navigation.findNavController(view).navigate(R.id.homeFragment);
    }

    private void getPreferenceData() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("Terminal", MODE_PRIVATE);
        terminal = sharedPreferences.getString("terminal", null);
    }
}