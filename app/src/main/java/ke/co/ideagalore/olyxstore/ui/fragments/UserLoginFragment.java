package ke.co.ideagalore.olyxstore.ui.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ke.co.ideagalore.olyxstore.R;
import ke.co.ideagalore.olyxstore.commons.CustomDialogs;
import ke.co.ideagalore.olyxstore.commons.ValidateFields;
import ke.co.ideagalore.olyxstore.databinding.FragmentUserLoginBinding;
import ke.co.ideagalore.olyxstore.ui.activities.Home;

public class UserLoginFragment extends Fragment implements View.OnClickListener {

    FragmentUserLoginBinding binding;

    CustomDialogs customDialogs = new CustomDialogs();
    ValidateFields validator = new ValidateFields();
    String store, name, terminal,attendantId,attendantStore,attendantName,terminalId;

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
        } else {
            if (validator.validateEmailAddress(requireActivity(), binding.edtEmail)
                    && validator.validateEditTextFields(requireActivity(), binding.edtPassword)) {
                signInUser();
            }
        }
    }

    private void signInUser() {

        customDialogs.showProgressDialog(requireActivity(), "Signing in");
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(binding.edtEmail.getText().toString().trim(), binding.edtPassword.getText().toString().trim())
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        getPreferenceData();
                    }

                }).addOnFailureListener(e -> {
                    customDialogs.dismissProgressDialog();
                    customDialogs.showSnackBar(requireActivity(), e.getMessage());
                });
    }

    private void getPreferenceData() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("Terminal", MODE_PRIVATE);
        store = sharedPreferences.getString("store", null);
        terminal = sharedPreferences.getString("terminal", null);
        name = sharedPreferences.getString("name", null);

        if (TextUtils.isEmpty(name) && TextUtils.isEmpty(terminal) && TextUtils.isEmpty(store)) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            attendantId = auth.getUid();
            getTerminalData(attendantId);

        } else {
            customDialogs.dismissProgressDialog();
            startActivity(new Intent(getActivity(), Home.class));
            requireActivity().finish();
        }

    }

    private void getTerminalData(String attendantId) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Attendants").child(attendantId);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                attendantStore = snapshot.child("store").getValue(String.class);
                attendantName = snapshot.child("attendant").getValue(String.class);
                terminalId = snapshot.child("terminal").getValue(String.class);
                savePreferencesData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void savePreferencesData() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("Terminal", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", attendantName);
        editor.putString("store", attendantStore);
        editor.putString("terminal", terminalId);
        editor.commit();

        customDialogs.dismissProgressDialog();
        startActivity(new Intent(getActivity(), Home.class));
        requireActivity().finish();
    }
}