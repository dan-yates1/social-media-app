package org.me.socialmediaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mPostBtn;
    private ImageButton mBackButton;
    private EditText mCommentEt;
    private RecyclerView mRecyclerView;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mDb;
    private FirebaseStorage mStorage;
    private StorageReference mStorageRef;

    private Post mPost;
    private FirestoreRecyclerAdapter<Comment, CommentViewHolder> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        mPost = (Post) getIntent().getSerializableExtra("Post");

        mAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference();

        initInterface();

        Query query = FirebaseFirestore.getInstance()
                .collection("posts").document(mPost.getPostUid()).collection("comments")
                .orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Comment> options = new FirestoreRecyclerOptions.Builder<Comment>()
                .setQuery(query, Comment.class)
                .build();

        mAdapter = new FirestoreRecyclerAdapter<Comment, CommentsActivity.CommentViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CommentViewHolder holder, int position, @NonNull Comment model) {
                holder.comment = model;
                holder.position = holder.getAdapterPosition();
                holder.commentTv.setText(model.getComment());

                DocumentReference docRef = mDb.collection("users").document(model.getAuthor());
                docRef.get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                String name = documentSnapshot.getString("name");
                                holder.nameTv.setText(name);
                            }
                        });

                mStorageRef.child("images/" + model.getAuthor()).getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    holder.profilePic.setImageBitmap(bmp);
                });
            }

            @NonNull
            @Override
            public CommentsActivity.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
                return new CommentsActivity.CommentViewHolder(view);
            }
        };

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initInterface() {
        mPostBtn = this.findViewById(R.id.postBtn);
        mPostBtn.setOnClickListener(this);
        mCommentEt = this.findViewById(R.id.commentEt);
        mRecyclerView = this.findViewById(R.id.recyclerView);
        mBackButton = this.findViewById(R.id.backBtn);
        mBackButton.setOnClickListener(this);
        mRecyclerView = this.findViewById(R.id.recyclerView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.postBtn:
                if (mCommentEt.getText().toString() != "") {
                    addComment();
                }
                break;
            case R.id.backBtn:
                finish();
                break;
        }
    }

    private void addComment() {
        String commentStr = mCommentEt.getText().toString();
        String commentUid = UUID.randomUUID().toString();
        Comment comment = new Comment(mAuth.getCurrentUser().getUid(), commentStr, mPost.getPostUid(), commentUid);
        Timestamp timestamp = Timestamp.now();
        comment.setTimestamp(timestamp);

        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        fStore.collection("posts").document(mPost.getPostUid())
                .collection("comments").document(commentUid)
                .set(comment).addOnSuccessListener((OnSuccessListener) o -> {
                    mCommentEt.setText("");
                });
    }

    private class CommentViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView profilePic;
        private TextView nameTv, commentTv;
        private Comment comment;
        private int position;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTv = itemView.findViewById(R.id.nameTv);
            commentTv = itemView.findViewById(R.id.commentTv);
            profilePic = itemView.findViewById(R.id.profilePic);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }
}