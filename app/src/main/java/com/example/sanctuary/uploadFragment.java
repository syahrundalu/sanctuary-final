package com.example.sanctuary;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.annotation.Nullable;

import com.example.sanctuary.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class uploadFragment extends Fragment {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText editText;
    private ImageView photoImageView;
    private Uri selectedPhotoUri;

    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload, container, false);

        db = FirebaseFirestore.getInstance();
        editText = view.findViewById(R.id.editText);
        photoImageView = view.findViewById(R.id.photoImageView);

        Button postButton = view.findViewById(R.id.postButton);
        postButton.setOnClickListener(v -> createPost(editText.getText().toString(), selectedPhotoUri));

        photoImageView.setOnClickListener(v -> openImagePicker());

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            selectedPhotoUri = data.getData();
            photoImageView.setImageURI(selectedPhotoUri);
        }
    }

    private void createPost(String text, Uri photoUri) {
        Map<String, Object> post = new HashMap<>();
        post.put("text", text);
        if (photoUri != null) {
            // Upload photo and add download URL to the 'post' map
            // ...

        }
        db.collection("posts")
                .add(post)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getActivity(), "Post created", Toast.LENGTH_SHORT).show();
                    editText.setText("");
                    photoImageView.setImageResource(R.drawable.tambah);
                    selectedPhotoUri = null;
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getActivity(), "Failed to create post", Toast.LENGTH_SHORT).show();
                });
    }
    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }
}
