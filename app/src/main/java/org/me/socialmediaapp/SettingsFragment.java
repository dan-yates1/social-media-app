package org.me.socialmediaapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsFragment extends Fragment implements View.OnClickListener {
    private CircleImageView mImgView;
    private Button mName, mBio, mImg, mLogout;
    private TextView mUsername;

    private Uri mImgUri;

    private FirebaseAuth mAuth;
    private FirebaseStorage mStorage;
    private StorageReference mStorageRef;
    private FirebaseFirestore mDb;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference();
        mDb = FirebaseFirestore.getInstance();

        initInterface(v);
        fetchProfilePic();
        setData();

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.changePicBtn:
                selectImage();
                break;
            case R.id.changeBioBtn:
                changeBio();
                break;
            case R.id.changeNameBtn:
                changeName();
                break;
            case R.id.logoutBtn:
                mAuth.getInstance().signOut();
                startActivity(new Intent(v.getContext(), RegisterActivity.class));
                break;
        }
    }

    private void setData() {
        DocumentReference docRef = mDb.collection("users").document(mAuth.getCurrentUser().getUid());
        docRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("name");
                        mUsername.setText(name);
                    }
                });
    }

    private void initInterface(View v) {
        mImgView = v.findViewById(R.id.profilePicIv);
        mImgView.setOnClickListener(this);
        mName = v.findViewById(R.id.changeNameBtn);
        mName.setOnClickListener(this);
        mBio = v.findViewById(R.id.changeBioBtn);
        mBio.setOnClickListener(this);
        mImg = v.findViewById(R.id.changePicBtn);
        mImg.setOnClickListener(this);
        mLogout = v.findViewById(R.id.logoutBtn);
        mLogout.setOnClickListener(this);
        mUsername = v.findViewById(R.id.usernameTv);
    }

    public void selectImage() {
        ImagePicker.Companion.with(this)
                .cropSquare()
                .compress(1024)
                .start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        mImgUri = uri;
        mImgView.setImageURI(uri);
        uploadImage();
    }

    public void uploadImage() {
        StorageReference imgRef = mStorageRef.child("images/" + mAuth.getCurrentUser().getUid());
        imgRef.putFile(mImgUri);
    }

    private void fetchProfilePic() {
        mStorageRef.child("images/" + mAuth.getCurrentUser().getUid()).getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            mImgView.setImageBitmap(bmp);
        });
    }

    public void changeName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Change Name");
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.layout_change_name, (ViewGroup) getView(), false);
        final EditText input = (EditText) viewInflated.findViewById(R.id.input);

        builder.setView(viewInflated);
        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
            dialog.dismiss();
            String name = input.getText().toString();
            if (!name.isEmpty()) {
                updateNameInFirebase(name);
                mUsername.setText(name);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());
        builder.show();
    }

    public void changeBio() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Change Bio");
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.layout_change_bio, (ViewGroup) getView(), false);
        final EditText input = (EditText) viewInflated.findViewById(R.id.input);

        builder.setView(viewInflated);
        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
            dialog.dismiss();
            String bio = input.getText().toString();
            if (!bio.isEmpty()) {
                updateBioInFirebase(bio);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void updateBioInFirebase(String bio) {
        mDb.collection("users").document(mAuth.getCurrentUser().getUid())
                .update("bio", bio)
                .addOnSuccessListener(aVoid -> Snackbar.make(getActivity().findViewById(android.R.id.content), "Bio Updated.", Snackbar.LENGTH_LONG).show());
    }

    private void updateNameInFirebase(String name) {
        mDb.collection("users").document(mAuth.getCurrentUser().getUid())
                .update("name", name)
                .addOnSuccessListener(aVoid -> Snackbar.make(getActivity().findViewById(android.R.id.content), "Username Updated.", Snackbar.LENGTH_LONG).show());
    }
}
