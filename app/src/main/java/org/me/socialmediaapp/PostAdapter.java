package org.me.socialmediaapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {

    private ArrayList<Post> posts;
    private Context mContext;

    public PostAdapter(ArrayList<Post> contactsList, Context context) {
        this.posts = contactsList;
        this.mContext = context;
    }

    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.post_item, parent, false);
        return new PostHolder(view);
    }

    @Override
    public int getItemCount() {
        return posts == null? 0: posts.size();
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, final int position) {
        final Post post = posts.get(position);

        holder.nameTv.setText(post.getTitle());
        holder.descriptionTv.setText(post.getDesc());
    }

    public class PostHolder extends RecyclerView.ViewHolder {

        private TextView nameTv, descriptionTv, postTimeTv;
        CircleImageView profilePic;
        ImageView postImage;
        ImageButton likeBtn, commentBtn, shareButton;

        public PostHolder(View itemView) {
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
}