package org.me.socialmediaapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {
    private CircleImageView mProfilePic;
    private RecyclerView mRecyclerView;

    private FirestoreRecyclerAdapter mAdapter;

    private FirebaseAuth mAuth;
    private FirebaseStorage mStorage;
    private StorageReference mStorageRef;
    private FirebaseFirestore mDb;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference();
        mDb = FirebaseFirestore.getInstance();

        getUserObj();
        initInterface(v);

        Query query = FirebaseFirestore.getInstance()
                .collection("posts")
                .orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Post> options = new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(query, Post.class)
                .build();

        mAdapter = new FirestoreRecyclerAdapter<Post, PostViewHolder>(options) {
            @NonNull
            @Override
            public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
                return new PostViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull PostViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Post model) {
                holder.post = model;
                holder.position = position;
                holder.descriptionTv.setText(model.getDesc());

                try {
                    mStorageRef.child("images/" + model.getAuthorUid()).getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        holder.profilePic.setImageBitmap(bmp);
                    });

                    mStorageRef.child("images/" + model.getImgRef()).getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        holder.postImage.setImageBitmap(bmp);
                    });

                    DocumentReference docRef = mDb.collection("users").document(model.getAuthorUid());
                    docRef.get()
                            .addOnSuccessListener(documentSnapshot -> {
                                if (documentSnapshot.exists()) {
                                    String name = documentSnapshot.getString("name");
                                    holder.nameTv.setText(name);
                                }
                            });
                    holder.likeCount.setText(String.valueOf(model.getLikes()));
                    if (model.getComments() != null) {
                        //holder.commentCount.setText(String.valueOf(model.getComments().size()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

        return v;
    }

    private void initInterface(View v) {
        mProfilePic = v.findViewById(R.id.profilePic);
        mRecyclerView = v.findViewById(R.id.recyclerView);
        mProfilePic.setOnClickListener(v1 -> startActivity(new Intent(getContext(), ProfileActivity.class)));
        fetchProfilePic();
    }

    private void getUserObj() {
        DocumentReference docRef = mDb.collection("users").document(mAuth.getUid());
        docRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                    }
                });
    }

    private void fetchProfilePic() {
        mStorageRef.child("images/" + mAuth.getCurrentUser().getUid()).getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            mProfilePic.setImageBitmap(bmp);
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

    private class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView nameTv, descriptionTv, postTimeTv, likeCount, commentCount;
        private CircleImageView profilePic;
        private ImageView postImage;
        private ImageButton likeBtn, commentBtn, shareButton;

        private Post post;
        private int position;
        private Boolean liked;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTv = itemView.findViewById(R.id.nameTv);
            descriptionTv = itemView.findViewById(R.id.descriptionTv);
            postTimeTv = itemView.findViewById(R.id.postTimeTv);
            profilePic = itemView.findViewById(R.id.profilePic);
            postImage = itemView.findViewById(R.id.postImage);
            likeBtn = itemView.findViewById(R.id.likeBtn);
            likeBtn.setOnClickListener(this);
            commentBtn = itemView.findViewById(R.id.commentBtn);
            commentBtn.setOnClickListener(this);
            shareButton = itemView.findViewById(R.id.shareButton);
            likeCount = itemView.findViewById(R.id.likeCountTv);
            commentCount = itemView.findViewById(R.id.commentCountTv);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.likeBtn:
                    updateLikeCount();
                    break;
                case R.id.commentBtn:
                    startActivity(new Intent(getContext().getApplicationContext(), CommentsActivity.class).putExtra("Post", post));
                    break;
            }
        }

        public void updateLikeCount() {
            int likes = Integer.parseInt((String) likeCount.getText());
            mDb.collection("posts").document(post.getPostUid())
                    .update("likes", likes+1)
                    .addOnSuccessListener(aVoid -> likeCount.setText(String.valueOf(likes+1)));
        }
    }
}
