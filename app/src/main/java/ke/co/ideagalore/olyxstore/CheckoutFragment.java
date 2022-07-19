package ke.co.ideagalore.olyxstore;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ke.co.ideagalore.olyxstore.databinding.FragmentCheckoutBinding;
import ke.co.ideagalore.olyxstore.models.Sale;

public class CheckoutFragment extends Fragment implements View.OnClickListener{

    FragmentCheckoutBinding binding;

    String brand;
    int capacity;
    int price;

    public CheckoutFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentCheckoutBinding.inflate(inflater, container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle arguments = getArguments();

        if (arguments != null) {
            String prodId = arguments.get("prodId").toString();
            brand = arguments.get("brand").toString();
            capacity = Integer.parseInt(arguments.get("capacity").toString());
            price = Integer.parseInt(arguments.get("price").toString());

            binding.tvItem.setText(brand);
            binding.tvDescription.setText(capacity+" kgs");
            binding.tvPrice.setText(price+"");

            binding.btnCheckOut.setText("Pay Kshs. "+price+ " for "+brand+" "+capacity+"kgs");

        }

        binding.btnCheckOut.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view==binding.btnCheckOut){

            DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Sales");
            String key=reference.push().getKey();
            Sale sale=new Sale();
            sale.setSaleId(key);
            sale.setPrice(String.valueOf(price));
            sale.setBrand(brand);
            sale.setQuantity(String.valueOf(capacity));

            reference.child(key).setValue(sale).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    Toast.makeText(getActivity(), "Sale successful", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(view).navigate(R.id.homeFragment);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }

    }
}