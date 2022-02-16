package org.me.socialmediaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mPostBtn;
    private ImageButton mBackButton;
    private EditText mCommentEt;
    private RecyclerView mRecyclerView;

    private FirebaseAuth mAuth;

    private Post mPost;
    private FirestoreRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        mPost = (Post) getIntent().getSerializableExtra("Post");

        mAuth = FirebaseAuth.getInstance();

        initInterface();

        Query query = FirebaseFirestore.getInstance()
                .collection("comments")
                .whereEqualTo("postUid", mPost.getPostUid())
                .orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Post> options = new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(query, Post.class)
                .build();

        mAdapter = new FirestoreRecyclerAdapter<Post, CommentsActivity.CommentViewHolder>(options) {
            @NonNull
            @Override
            public CommentsActivity.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
                return new CommentsActivity.CommentViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull CommentsActivity.CommentViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Post model) {
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
                addComment();
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
        fStore.collection("comments").document(commentUid)
                .set(comment).addOnSuccessListener(new OnSuccessListener(){
            @Override
            public void onSuccess(Object o) {
                //Snackbar.make(getActivity().findViewById(android.R.id.content), "Post Uploaded.", Snackbar.LENGTH_LONG).show();
            }
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

    private class CommentViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView profilePic;
        private TextView nameTv, commentTv;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTv = itemView.findViewById(R.id.nameTv);
            commentTv = itemView.findViewById(R.id.commentTv);
            profilePic = itemView.findViewById(R.id.profilePic);
        }
    }
}