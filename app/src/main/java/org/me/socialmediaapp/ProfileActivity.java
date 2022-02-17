package org.me.socialmediaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private CircleImageView mProfilePic;
    private TextView nameTv, bioTv, postCountTv, likeCountTv, commentCountTv;
    private ImageButton mBackButton;

    private FirestoreRecyclerAdapter mAdapter;
    private String mProfileUid;

    private FirebaseAuth mAuth;
    private FirebaseStorage mStorage;
    private StorageReference mStorageRef;
    private FirebaseFirestore mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference();
        mDb = FirebaseFirestore.getInstance();

        mProfileUid = mAuth.getCurrentUser().getUid();

        initInterface();

        Query query = FirebaseFirestore.getInstance()
                .collection("posts")
                .whereEqualTo("authorId", mProfileUid)
                .orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Post> options = new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(query, Post.class)
                .build();

        mAdapter = new FirestoreRecyclerAdapter<Post, ProfileActivity.PostViewHolder>(options) {
            @NonNull
            @Override
            public ProfileActivity.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile_post, parent, false);
                return new ProfileActivity.PostViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ProfileActivity.PostViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Post model) {
                holder.post = model;
                holder.position = position;

                mStorageRef.child("images/" + model.getImgRef()).getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    holder.postImg.setImageBitmap(bmp);
                });
            }
        };

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);

        setData();
    }

    private void setData() {
        DocumentReference docRef = mDb.collection("users").document(mAuth.getCurrentUser().getUid());
        docRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("name");
                        nameTv.setText(name);
                    }
                });

        mStorageRef.child("images/" + mProfileUid).getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            mProfilePic.setImageBitmap(bmp);
        });
    }

    private void initInterface() {
        mRecyclerView = this.findViewById(R.id.recyclerView);
        mProfilePic = this.findViewById(R.id.profilePicIv);
        nameTv = this.findViewById(R.id.nameTv);
        bioTv = this.findViewById(R.id.bioTv);
        postCountTv = this.findViewById(R.id.postCountTv);
        likeCountTv = this.findViewById(R.id.likeCountTv);
        commentCountTv = this.findViewById(R.id.commentCountTv);
        mBackButton = this.findViewById(R.id.backBtn);
        mBackButton.setOnClickListener(v -> finish());
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

    private class PostViewHolder extends RecyclerView.ViewHolder {

        private Post post;
        private int position;

        private ImageView postImg;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            postImg = itemView.findViewById(R.id.postIv);
        }
    }
}