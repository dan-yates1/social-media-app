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

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {
    private CircleImageView mProfilePic;
    private RecyclerView mRecyclerView;

    FirestoreRecyclerAdapter mAdapter;
    private ArrayList<Post> mPosts = new ArrayList<>();;

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

        initInterface(v);

        return v;
    }

    private void initInterface(View v) {
        mProfilePic = v.findViewById(R.id.profilePic);
        mRecyclerView = v.findViewById(R.id.recyclerView);
        fetchProfilePic();
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        Query query = FirebaseFirestore.getInstance()
                .collection("posts");

        FirestoreRecyclerOptions<Post> options = new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(query, Post.class)
                .build();

        mAdapter = new FirestoreRecyclerAdapter<Post, PostViewHolder>(options) {
            @NonNull
            @Override
            public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
                return new PostViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull Post model) {
                holder.nameTv.setText(model.getTitle());
                holder.descriptionTv.setText(model.getDesc());

                mStorageRef.child("images/" + model.getImgRef()).getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    holder.postImage.setImageBitmap(bmp);
                }).addOnFailureListener(exception -> {
                    // Handle any errors
                });

                mStorageRef.child("images/" + mAuth.getCurrentUser().getUid()).getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    holder.profilePic.setImageBitmap(bmp);
                }).addOnFailureListener(exception -> {
                    // Handle any errors
                });
            }
        };

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void fetchProfilePic() {
        mStorageRef.child("images/" + mAuth.getCurrentUser().getUid()).getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            mProfilePic.setImageBitmap(bmp);
        }).addOnFailureListener(exception -> {
            // Handle any errors
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }
}
