package ke.co.ideagalore.olyxstore.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ke.co.ideagalore.olyxstore.R;
import ke.co.ideagalore.olyxstore.commons.CustomDialogs;
import ke.co.ideagalore.olyxstore.commons.ValidateFields;
import ke.co.ideagalore.olyxstore.databinding.FragmentUserStoreBinding;
import ke.co.ideagalore.olyxstore.models.Stores;

public class UserStoreFragment extends Fragment implements View.OnClickListener {

    FragmentUserStoreBinding binding;
    String terminal, selectedItem;

    List<String> storesList = new ArrayList<>();

    CustomDialogs customDialogs = new CustomDialogs();
    ValidateFields validator = new ValidateFields();

    public UserStoreFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserStoreBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getBundleData();
        getStoresData();

        binding.btnNext.setOnClickListener(this);
    }

    private void getStoresData() {

        customDialogs.showProgressDialog(requireActivity(), "Fetching stores");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(terminal).child("Stores");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot storeSnapshot : snapshot.getChildren()) {

                    Stores store = storeSnapshot.getValue(Stores.class);
                    String storeName = store.getStore();
                    storesList.add(0, storeName);

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, storesList);
                    arrayAdapter.setDropDownViewResource(R.layout.spinner_item);
                    binding.spinnerStore.setAdapter(arrayAdapter);
                    binding.spinnerStore.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedItem = binding.spinnerStore.getSelectedItem().toString();

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                    binding.spinnerStore.setAdapter(arrayAdapter);
                    customDialogs.dismissProgressDialog();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                customDialogs.dismissProgressDialog();
                customDialogs.showSnackBar(requireActivity(), error.getMessage());

            }
        });

    }

    @Override
    public void onClick(View view) {

        if (view == binding.btnNext) {
            Bundle bundle = new Bundle();
            bundle.putString("terminalId", terminal);
            bundle.putString("selectedStore", selectedItem);
            Navigation.findNavController(view).navigate(R.id.userSignUpFragment, bundle);
        }

    }

    private void getBundleData() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            terminal = arguments.get("terminalId").toString();
        }
    }
}