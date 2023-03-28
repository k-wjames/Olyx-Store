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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import ke.co.ideagalore.olyxstore.R;
import ke.co.ideagalore.olyxstore.commons.CustomDialogs;
import ke.co.ideagalore.olyxstore.commons.ValidateFields;
import ke.co.ideagalore.olyxstore.databinding.FragmentUserLoginBinding;
import ke.co.ideagalore.olyxstore.ui.activities.Home;

public class UserLoginFragment extends Fragment implements View.OnClickListener {

    FragmentUserLoginBinding binding;

    CustomDialogs customDialogs = new CustomDialogs();
    ValidateFields validator = new ValidateFields();
    String attendantStore, attendantName, terminalId, status, attendantEmail, signInEmail;

    FirebaseAuth auth;

    public UserLoginFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.tvSignup.setOnClickListener(this);
        binding.btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == binding.tvSignup) {
            Navigation.findNavController(view).navigate(R.id.terminalIdFragment);
        } else if (view == binding.btnLogin) {
            if (validator.validateEmailAddress(requireActivity(), binding.edtEmail)
                    && validator.validateEditTextFields(requireActivity(), binding.edtPassword)) {
                signInUser();
            }
        }
    }

    private void signInUser() {

        customDialogs.showProgressDialog(requireActivity(), "Signing in");
        signInEmail = binding.edtEmail.getText().toString().trim();
        auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(signInEmail, binding.edtPassword.getText().toString().trim())
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        getTerminalData(auth.getUid());
                    }

                }).addOnFailureListener(e -> {
                    customDialogs.dismissProgressDialog();
                    customDialogs.showSnackBar(requireActivity(), e.getMessage());
                });
    }

    private void getTerminalData(String attendantId) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Attendants").child(attendantId);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    attendantStore = snapshot.child("store").getValue(String.class);
                    attendantName = snapshot.child("attendant").getValue(String.class);
                    terminalId = snapshot.child("terminal").getValue(String.class);
                    status = snapshot.child("status").getValue(String.class);
                    attendantEmail = snapshot.child("emailId").getValue(String.class);

                    if (status.equals("authenticate")) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("status", "authenticated");
                        map.put("emailId", signInEmail);
                        ref.updateChildren(map).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                savePreferencesData();
                            }
                        });
                        return;
                    }

                    if (status.equals("terminated")) {
                        customDialogs.dismissProgressDialog();
                        customDialogs.showSnackBar(requireActivity(), "Access denied. Please check with admin");
                        auth.signOut();
                        clearSharedPrefs();
                        return;

                    } if(status.equals("authenticated")) {
                        savePreferencesData();

                    }

                } else {
                    customDialogs.dismissProgressDialog();
                    auth.signOut();
                    clearSharedPrefs();
                    customDialogs.showSnackBar(requireActivity(), "User does not exist. Please check with admin");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void startHomeActivity() {
        startActivity(new Intent(requireActivity(), Home.class));
        requireActivity().finish();
    }

    private void clearSharedPrefs() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("Terminal", MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
    }

    public void savePreferencesData() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("Terminal", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", attendantName);
        editor.putString("store", attendantStore);
        editor.putString("terminal", terminalId);
        editor.commit();

        customDialogs.dismissProgressDialog();
        startHomeActivity();
    }
}