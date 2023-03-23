package ke.co.ideagalore.olyxstore.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ke.co.ideagalore.olyxstore.commons.CustomDialogs;
import ke.co.ideagalore.olyxstore.databinding.ActivitySplashBinding;

public class Splash extends AppCompatActivity {

    ActivitySplashBinding binding;

    Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        new Handler().postDelayed(() -> {
            checkAuthenticationStatus();

        }, 1000);

    }

    private void checkAuthenticationStatus() {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Attendants").child(auth.getUid());
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String status = snapshot.child("status").getValue(String.class);
                        switch (status) {
                            case "authenticate":
                                reAuthenticateAttendant(auth);
                                break;
                            case "authenticated":
                                startActivity(new Intent(getApplicationContext(), Home.class));
                                break;
                            case "terminated":
                                CustomDialogs customDialogs = new CustomDialogs();
                                if (activity != null) {
                                    customDialogs.showSnackBar(activity, "Access denied. Exiting....");
                                    clearSharedPrefs();
                                    finish();
                                }
                                break;
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } else {
            startActivity(new Intent(getApplicationContext(), Onboard.class));
            finish();
        }
    }

    private void reAuthenticateAttendant(FirebaseAuth auth) {
        auth.signOut();
        clearSharedPrefs();
    }

    private void clearSharedPrefs() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Terminal", MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
        startActivity(new Intent(getApplicationContext(), Onboard.class));
    }
}