package org.me.socialmediaapp;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView nameTv, descriptionTv, postTimeTv, likeCount, commentCount;
    CircleImageView profilePic;
    ImageView postImage;
    ImageButton likeBtn, commentBtn, shareButton;

    FirebaseStorage mStorage;
    StorageReference mStorageRef;

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
                int likes = Integer.parseInt((String) likeCount.getText());
                likeCount.setText(String.valueOf(likes)+1);
                updateLikeCount(likes, getAdapterPosition());
                break;
            case R.id.commentBtn:
                // Open comment activity
                break;
        }
    }

    public void updateLikeCount(int likes, int position) {
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference();
    }
}
