package org.me.socialmediaapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {
    private CircleImageView mProfilePic;
    private RecyclerView mRecyclerView;

    private PostAdapter listAdapter;
    private ArrayList<Post> mPosts;

    private FirebaseAuth mAuth;
    private FirebaseStorage mStorage;
    private StorageReference mStorageRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference();

        mPosts = new ArrayList<>();

        initInterface(v);
        fetchProfilePic();

        return v;
    }

    private void initInterface(View v) {
        mProfilePic = v.findViewById(R.id.profilePic);
        mRecyclerView = v.findViewById(R.id.recyclerView);
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        listAdapter = new PostAdapter(mPosts, getContext());
        mRecyclerView.setAdapter(listAdapter);
        getPosts();
    }

    private void getPosts() {
        User user = new User();
        Post post = new Post("This is my posts", "This is a description, wow this is a cool posts.", "", user);
        mPosts.add(post);
        listAdapter.notifyDataSetChanged();
    }

    private void fetchProfilePic() {
        StorageReference imgRef = mStorageRef.child("images/" + mAuth.getCurrentUser().getUid());
        final long ONE_MEGABYTE = 1024 * 1024;

        imgRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                mProfilePic.setImageBitmap(bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //Toast.makeText(getContext().getApplicationContext(), "Failed to Fetch Profile Picture.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
