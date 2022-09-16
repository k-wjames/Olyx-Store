package ke.co.ideagalore.olyxstore.ui.fragments;

import android.content.Intent;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import ke.co.ideagalore.olyxstore.R;
import ke.co.ideagalore.olyxstore.commons.CustomDialogs;
import ke.co.ideagalore.olyxstore.commons.ValidateFields;
import ke.co.ideagalore.olyxstore.databinding.FragmentUserLoginBinding;
import ke.co.ideagalore.olyxstore.ui.activities.Home;

public class UserLoginFragment extends Fragment implements View.OnClickListener {

    FragmentUserLoginBinding binding;

    CustomDialogs customDialogs = new CustomDialogs();
    ValidateFields validator = new ValidateFields();

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
                        customDialogs.dismissProgressDialog();
                        startActivity(new Intent(getActivity(), Home.class));
                        requireActivity().finish();
                    }

                }).addOnFailureListener(e -> {
                    customDialogs.dismissProgressDialog();
                    customDialogs.showSnackBar(requireActivity(), e.getMessage());
                });
    }
}