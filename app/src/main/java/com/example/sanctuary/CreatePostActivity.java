package com.example.sanctuary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CreatePostActivity extends AppCompatActivity {

    private ImageView ivSelectedImage;
    private EditText etPostContent;
    private Button btnCreatePost, btnSelectImage;
    private Uri selectedImageUri;
    private static final int PICK_IMAGE_REQUEST = 1;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        ivSelectedImage = findViewById(R.id.ivSelectedImage);
        etPostContent = findViewById(R.id.etPostContent);
        btnCreatePost = findViewById(R.id.btnCreatePost);
        btnSelectImage = findViewById(R.id.btnSelectImage);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        btnSelectImage.setOnClickListener(v -> openImagePicker());

        btnCreatePost.setOnClickListener(v -> createPost());
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            Glide.with(this).load(selectedImageUri).into(ivSelectedImage);
        }
    }

    private void createPost() {
        String content = etPostContent.getText().toString().trim();

        if (content.isEmpty()) {
            etPostContent.setError("Please enter post content");
            etPostContent.requestFocus();
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating post...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String userId = firebaseAuth.getCurrentUser().getUid();
        StorageReference imageRef = FirebaseStorage.getInstance().getReference()
                .child("post_images").child(userId + "_" + System.currentTimeMillis() + ".jpg");

        UploadTask uploadTask = imageRef.putFile(selectedImageUri);

        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return imageRef.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();
                String imageUrl = downloadUri.toString();

                Post post = new Post(content, imageUrl, userId, System.currentTimeMillis());

                firestore.collection("posts")
                        .add(post)
                        .addOnSuccessListener(documentReference -> {
                            progressDialog.dismiss();
                            Toast.makeText(this, "Post created successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            progressDialog.dismiss();
                            Toast.makeText(this, "Error creating post: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            } else {
                progressDialog.dismiss();
                Toast.makeText(this, "Error uploading image: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
