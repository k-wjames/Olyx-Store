package ke.co.ideagalore.olyxstore.ui.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ke.co.ideagalore.olyxstore.R;
import ke.co.ideagalore.olyxstore.commons.CustomDialogs;
import ke.co.ideagalore.olyxstore.commons.ValidateFields;
import ke.co.ideagalore.olyxstore.databinding.FragmentUserSignUpBinding;
import ke.co.ideagalore.olyxstore.models.Attendant;
import ke.co.ideagalore.olyxstore.ui.activities.Home;

public class UserSignUpFragment extends Fragment implements View.OnClickListener {

    FragmentUserSignUpBinding binding;
    String terminal, selectedStore;

    CustomDialogs customDialogs = new CustomDialogs();
    ValidateFields validator = new ValidateFields();

    public UserSignUpFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserSignUpBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getBundleData();

        binding.btnSignup.setOnClickListener(this);
        binding.tvLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == binding.btnSignup) {

            if (validator.validateEditTextFields(requireActivity(), binding.edtUsername)
                    && validator.validateEmailAddress(requireActivity(), binding.edtEmail)
                    && validator.validateEditTextFields(requireActivity(), binding.edtPassword)
                    && validator.validateEditTextFields(requireActivity(), binding.edtConfirmPassword)
                    && binding.edtPassword.getText().toString().trim().equals(binding.edtConfirmPassword.getText().toString().trim())) {

                createUserAccount(binding.edtEmail.getText().toString().trim(), binding.edtPassword.getText().toString().trim());

            }
        } else {
            Navigation.findNavController(view).navigate(R.id.userLoginFragment);
        }
    }

    private void createUserAccount(String mail, String password) {
        customDialogs.showProgressDialog(requireActivity(), "Setting up user account");
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(terminal).child("Attendants");
                String userId = auth.getUid();
                Attendant attendant = new Attendant();
                attendant.setAttendantId(userId);
                attendant.setAttendant(binding.edtUsername.getText().toString().trim());
                attendant.setStore(selectedStore);

                reference.child(userId).setValue(attendant).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            customDialogs.dismissProgressDialog();
                            savePreferencesData();
                            Navigation.findNavController(requireView()).navigate(R.id.userLoginFragment);
                        }

                    }
                }).addOnFailureListener(e -> {
                    customDialogs.dismissProgressDialog();
                    customDialogs.showSnackBar(requireActivity(), e.getMessage());

                });

            }

        }).addOnFailureListener(e -> {

            customDialogs.dismissProgressDialog();
            customDialogs.showSnackBar(requireActivity(), e.getMessage());
        });
    }

    private void getBundleData() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            terminal = arguments.get("terminalId").toString();
            selectedStore = arguments.get("selectedStore").toString();
        }
    }

    public void savePreferencesData() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("Terminal", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", binding.edtUsername.getText().toString().trim());
        editor.putString("store", selectedStore);
        editor.putString("terminal", terminal);
        editor.commit();
    }

}