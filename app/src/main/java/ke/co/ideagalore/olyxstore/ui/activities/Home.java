package ke.co.ideagalore.olyxstore.ui.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ke.co.ideagalore.olyxstore.R;
import ke.co.ideagalore.olyxstore.databinding.ActivityHomeBinding;

public class Home extends AppCompatActivity {

    FirebaseAuth auth=FirebaseAuth.getInstance();

    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        binding=ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        checkAuthenticationStatus();

    }
    private void checkAuthenticationStatus() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Attendants").child(auth.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String status = snapshot.child("status").getValue(String.class);
                    switch (status) {
                        case "authenticate":
                            showAuthenticateDialog();
                            break;
                        case "terminated":

                            showTerminatedDialog();

                            break;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void showAuthenticateDialog() {

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.expired_session_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        new Handler().postDelayed(() -> {
            dialog.dismiss();
            reAuthenticateAttendant();
        }, 1000);

    }

    private void reAuthenticateAttendant() {
        auth.signOut();
        clearSharedPrefs();
    }
    private void showTerminatedDialog() {
        Dialog myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.terminated_dialog);
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        myDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();

        new Handler().postDelayed(() -> {
            myDialog.dismiss();
            reAuthenticateAttendant();
        }, 500);
    }
    private void clearSharedPrefs() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Terminal", MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
        startActivity(new Intent(getApplicationContext(), Onboard.class));
        finish();
    }
}