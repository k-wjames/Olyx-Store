package ke.co.ideagalore.olyxstore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.util.Calendar;
import java.util.Locale;

import ke.co.ideagalore.olyxstore.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment implements View.OnClickListener {

    FragmentHomeBinding binding;


    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getCurrentDate();
        binding.cvSell.setOnClickListener(this);
        binding.cvTransactions.setOnClickListener(this);
        binding.cvStock.setOnClickListener(this);
        binding.cvOrders.setOnClickListener(this);
    }

    private void getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);
        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());

        String dayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());

        binding.tvDay.setText(dayOfWeek + ",");
        binding.tvDate.setText(day + " " + month + " " + year);

    }

    @Override
    public void onClick(View view) {
        if (view == binding.cvSell) {
            Navigation.findNavController(view).navigate(R.id.sellFragment);
        } else if (view == binding.cvTransactions) {
            Navigation.findNavController(view).navigate(R.id.transactionsFragment);
        } else if (view == binding.cvStock) {
            Navigation.findNavController(view).navigate(R.id.stockFragment);
        } else if (view == binding.cvOrders) {
            Navigation.findNavController(view).navigate(R.id.ordersFragment);
        }
    }
}