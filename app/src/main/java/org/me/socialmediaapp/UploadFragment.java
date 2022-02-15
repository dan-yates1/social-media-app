package org.me.socialmediaapp;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class UploadFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "TAG";
    private EditText mTitleEt, mDescEt;
    private Button mLocationBtn, mUploadBtn, mPublishBtn;
    private ImageView mPostIv;

    private Uri mImgUri;
    private Location mLocation;

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
        mTitleEt = v.findViewById(R.id.titleEt);
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
        Post post = new Post();
        post.setTitle(mTitleEt.getText().toString().trim());
        post.setDesc(mDescEt.getText().toString().trim());
        //post.setImage(mImgUri);
        // TODO: Add post to firebase
    }

    public void addPostToFirestore(Post post) {
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        DocumentReference docRef = fStore
                .collection("users")
                .document(mAuth.getCurrentUser().getUid())
                .collection("posts")
                .document(mAuth.getCurrentUser().getUid());
        Map<String, Object> userMap = new HashMap<>();
        docRef.set(userMap).addOnSuccessListener((OnSuccessListener) o ->
                Log.d(TAG, "onSuccess: User profile is created for" + mAuth.getCurrentUser().getUid()));
    }

    public void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImgUri = data.getData();
            mPostIv.setImageURI(mImgUri);
        }
    }
}
