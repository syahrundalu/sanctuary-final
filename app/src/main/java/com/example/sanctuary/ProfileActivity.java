package com.example.sanctuary;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ProfileActivity extends AppCompatActivity {
    // Initialize variable
    ImageView ivImage, profilePost;
    TextView tvName,tvEmail;
    FirebaseAuth firebaseAuth;
    GoogleSignInClient googleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Assign variable
        ivImage = findViewById(R.id.iv_image);
        profilePost = findViewById(R.id.profile_call);
        tvName = findViewById(R.id.tv_name);
        tvEmail = findViewById(R.id.tv_email);

        // Initialize firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize firebase user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        // Check condition
        if (firebaseUser != null) {
            // When firebase user is not equal to null set image on image view
            Glide.with(ProfileActivity.this).load(firebaseUser.getPhotoUrl()).into(ivImage);
            Glide.with(ProfileActivity.this).load(firebaseUser.getPhotoUrl()).into(profilePost);
            // set name on text view
            tvName.setText(firebaseUser.getDisplayName());
            tvEmail.setText(firebaseUser.getEmail());
        }
        ivImage.setOnClickListener(view -> showDialogLogout());

        // Initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(ProfileActivity.this, GoogleSignInOptions.DEFAULT_SIGN_IN);

        // Set click listener for the button to open uploadFragment
        ImageView btnOpenUpload = findViewById(R.id.tambahfeed);

        // Set click listener for the button to open CreatePostActivity
        btnOpenUpload.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, CreatePostActivity.class);
            startActivity(intent);
        });


    }



    private void showDialogLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.logout_dialog, null);
        builder.setView(dialogView);

        Button btnLogout = dialogView.findViewById(R.id.btnLogout);
        AlertDialog dialog = builder.create();

        btnLogout.setOnClickListener(view -> {
            // Handle logout logic here (sign out user, navigate to login screen, etc.)
            // For example:
            // Sign out from google
            googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    // Check condition
                    if (task.isSuccessful()) {
                        // When task is successful sign out from firebase
                        firebaseAuth.signOut();
                        // Display Toast
                        Toast.makeText(getApplicationContext(), "Logout successful", Toast.LENGTH_SHORT).show();
                        // Finish activity
                        finish();
                    }
                }

            });
            dialog.dismiss();
        });

        dialog.show();
    }




}
