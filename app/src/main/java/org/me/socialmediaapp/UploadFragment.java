package org.me.socialmediaapp;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
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

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import java.util.ArrayList;
import java.util.UUID;

public class UploadFragment extends Fragment implements View.OnClickListener {
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;

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

        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        permissionsToRequest = findUnAskedPermissions(permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }

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
                getUserLocation();
                break;
            case R.id.uploadBtn:
                selectImage();
                break;
            case R.id.publishBtn:
                publish();
                break;
        }
    }

    private void getUserLocation() {
        locationTrack = new LocationTrack(getContext());
        if (locationTrack.canGetLocation()) {
            double longitude = locationTrack.getLongitude();
            double latitude = locationTrack.getLatitude();
        } else {
            locationTrack.showSettingsAlert();
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
                .set(post).addOnSuccessListener((OnSuccessListener) o ->
                Snackbar.make(getActivity().findViewById(android.R.id.content),
                        "Post Uploaded.", Snackbar.LENGTH_LONG).show());
    }

    public void selectImage() {
        ImagePicker.Companion.with(this)
                .cropSquare()
                .compress(1024)
                .start();
    }

    private void uploadImage() {
        mImgUid = UUID.randomUUID().toString();
        StorageReference imgRef = mStorageRef.child("images/" + mImgUid);
        imgRef.putFile(mImgUri)
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext().getApplicationContext(), "Failed to Upload Image.", Toast.LENGTH_LONG).show();
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mImgUri = data.getData();
        mPostIv.setImageURI(mImgUri);
        uploadImage();
    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }
    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return false;
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }
}
