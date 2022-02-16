package org.me.socialmediaapp;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class UploadFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "TAG";
    private EditText mDescEt;
    private Button mLocationBtn, mUploadBtn, mPublishBtn;
    private ImageView mPostIv;

    private Uri mImgUri;
    private String mImgUid;

    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_upload, container, false);

        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        initInterface(v);
        return v;
    }

    public void initInterface(View v) {
        mDescEt = v.findViewById(R.id.descEt);
        mLocationBtn = v.findViewById(R.id.locationBtn);
        mLocationBtn.setOnClickListener(this);
        mUploadBtn = v.findViewById(R.id.uploadBtn);
        mUploadBtn.setOnClickListener(this);
        mPublishBtn = v.findViewById(R.id.publishBtn);
        mPublishBtn.setOnClickListener(this);
        mPostIv = v.findViewById(R.id.postIv);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.locationBtn:
                //getLocation();
                break;
            case R.id.uploadBtn:
                selectImage();
                break;
            case R.id.publishBtn:
                publish();
                break;
        }
    }

    public void publish() {
        String desc = mDescEt.getText().toString().trim();
        String author = mAuth.getCurrentUser().getUid();
        String imgRef = mImgUid;
        Post post = new Post(desc, imgRef, author);
        addPostToFirestore(post);
    }

    public void addPostToFirestore(Post post) {
        String postUid = UUID.randomUUID().toString();
        post.setPostUid(postUid);
        Timestamp timestamp = Timestamp.now();
        post.setTimestamp(timestamp);
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        fStore.collection("posts").document(postUid)
                .set(post).addOnSuccessListener(new OnSuccessListener(){
            @Override
            public void onSuccess(Object o) {
                Snackbar.make(getActivity().findViewById(android.R.id.content), "Post Uploaded.", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    private void uploadImage() {
        mImgUid = UUID.randomUUID().toString();
        StorageReference imgRef = mStorageRef.child("images/" + mImgUid);
        imgRef.putFile(mImgUri)
                .addOnFailureListener(e -> {
                    //Toast.makeText(getContext().getApplicationContext(), "Failed to Upload.", Toast.LENGTH_LONG).show();
                })
                .addOnSuccessListener(taskSnapshot -> {
                    //Snackbar.make(getActivity().findViewById(android.R.id.content), "Image Uploaded.", Snackbar.LENGTH_LONG).show();
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImgUri = data.getData();
            mPostIv.setImageURI(mImgUri);
            uploadImage();
        }
    }
}
