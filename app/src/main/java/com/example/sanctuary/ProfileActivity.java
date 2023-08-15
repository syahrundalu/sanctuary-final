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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.sanctuary.CreatePostActivity;
import com.example.sanctuary.Post;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.example.sanctuary.PostAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    // Initialize variables
    ImageView ivImage, profilePost;
    TextView tvName, tvEmail;
    FirebaseAuth firebaseAuth;
    GoogleSignInClient googleSignInClient;
    RecyclerView rvPosts;
    PostAdapter postAdapter;
    List<Post> postList;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Assign variables
        ivImage = findViewById(R.id.iv_image);
        profilePost = findViewById(R.id.profile_call);
        tvName = findViewById(R.id.tv_name);
        tvEmail = findViewById(R.id.tv_email);
        rvPosts = findViewById(R.id.rvPosts);

        // Initialize Firebase instances
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Initialize Google SignIn client
        googleSignInClient = GoogleSignIn.getClient(ProfileActivity.this, GoogleSignInOptions.DEFAULT_SIGN_IN);

        // Initialize postList and postAdapter
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(postList);

        // Set up RecyclerView
        rvPosts.setLayoutManager(new LinearLayoutManager(this));
        rvPosts.setAdapter(postAdapter);

        // Fetch and display user profile data and posts
        fetchUserProfileData();
        fetchUserPosts();

        ivImage.setOnClickListener(view -> showDialogLogout());

        // Set click listener for the button to open CreatePostActivity
        ImageView btnOpenUpload = findViewById(R.id.tambahfeed);
        btnOpenUpload.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, CreatePostActivity.class);
            startActivity(intent);
        });
    }

    private void fetchUserProfileData() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            Glide.with(ProfileActivity.this).load(firebaseUser.getPhotoUrl()).into(ivImage);
            Glide.with(ProfileActivity.this).load(firebaseUser.getPhotoUrl()).into(profilePost);
            tvName.setText(firebaseUser.getDisplayName());
            tvEmail.setText(firebaseUser.getEmail());
        }
    }

    private void fetchUserPosts() {
        String userId = firebaseAuth.getCurrentUser().getUid();

        firestore.collection("posts")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    postList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String content = document.getString("content");
                        String imageUrl = document.getString("imageUrl");
                        long timestamp = document.getLong("timestamp");
                        Post post = new Post(content, imageUrl, userId, timestamp);
                        postList.add(post);
                    }
                    postAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    // Handle error
                });
    }

    private void showDialogLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.logout_dialog, null);
        builder.setView(dialogView);

        Button btnLogout = dialogView.findViewById(R.id.btnLogout);
        AlertDialog dialog = builder.create();

        btnLogout.setOnClickListener(view -> {
            googleSignInClient.signOut().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    firebaseAuth.signOut();
                    Toast.makeText(getApplicationContext(), "Logout successful", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
            dialog.dismiss();
        });

        dialog.show();
    }
}
