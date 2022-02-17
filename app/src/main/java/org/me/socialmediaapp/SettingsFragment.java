package org.me.socialmediaapp;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsFragment extends Fragment implements View.OnClickListener {
    private CircleImageView mImgView;
    private Button mName, mBio, mImg, mLogout;

    private Uri mImgUri;

    private FirebaseAuth mAuth;
    private FirebaseStorage mStorage;
    private StorageReference mStorageRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference();

        initInterface(v);
        fetchProfilePic();

        return v;
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
            Uri uri = data.getData();
            mImgUri = uri;
            mImgView.setImageURI(uri);
            uploadImage();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.changePicBtn:
                selectImage();
                break;
            case R.id.changeBioBtn:
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

    public void changeName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Change Name");

        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.input, (ViewGroup) getView(), false);

        final EditText input = (EditText) viewInflated.findViewById(R.id.input);

        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
            dialog.dismiss();
            String name = input.getText().toString();
            updateNameInFirebase(name);
        });
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void updateNameInFirebase(String name) {

    }
}
