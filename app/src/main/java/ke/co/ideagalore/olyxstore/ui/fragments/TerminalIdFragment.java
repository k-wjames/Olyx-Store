package ke.co.ideagalore.olyxstore.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import ke.co.ideagalore.olyxstore.R;
import ke.co.ideagalore.olyxstore.commons.ValidateFields;
import ke.co.ideagalore.olyxstore.databinding.FragmentTerminalIdBinding;

public class TerminalIdFragment extends Fragment implements View.OnClickListener {

    FragmentTerminalIdBinding binding;
    ValidateFields validator = new ValidateFields();

    public TerminalIdFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTerminalIdBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == binding.btnNext) {
            if (validator.validateEditTextFields(requireActivity(), binding.edtTerminalId)) {
                Bundle bundle=new Bundle();
                bundle.putString("terminalId",binding.edtTerminalId.getText().toString().trim());
                Navigation.findNavController(view).navigate(R.id.userStoreFragment, bundle);
            }
        }

    }
}