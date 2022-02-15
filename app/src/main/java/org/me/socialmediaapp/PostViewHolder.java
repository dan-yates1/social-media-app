package org.me.socialmediaapp;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostViewHolder extends RecyclerView.ViewHolder {

    TextView nameTv, descriptionTv, postTimeTv;
    CircleImageView profilePic;
    ImageView postImage;
    ImageButton likeBtn, commentBtn, shareButton;

    public PostViewHolder(@NonNull View itemView) {
        super(itemView);

        nameTv = itemView.findViewById(R.id.nameTv);
        descriptionTv = itemView.findViewById(R.id.descriptionTv);
        postTimeTv = itemView.findViewById(R.id.postTimeTv);
        profilePic = itemView.findViewById(R.id.profilePic);
        postImage = itemView.findViewById(R.id.postImage);
        likeBtn = itemView.findViewById(R.id.likeBtn);
        commentBtn = itemView.findViewById(R.id.commentBtn);
        shareButton = itemView.findViewById(R.id.shareButton);
    }
}
