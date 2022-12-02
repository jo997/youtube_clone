package de.unituebingen.streamapp.Videos.VideoView;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.unituebingen.streamapp.R;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {


    ArrayList<String> commentData = new ArrayList<String>();
    ArrayList<String> commentDataAut = new ArrayList<String>();
    ArrayList<String> commentDataTime = new ArrayList<String>();
    Context context;


    public CommentAdapter(Context ct, ArrayList llComm, ArrayList llCommAuthor, ArrayList llCommTime) {
        context = ct;
        commentData = llComm;
        commentDataAut = llCommAuthor;
        commentDataTime = llCommTime;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.comment_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // fill rv list with data from comments
        holder.comment.setText(commentData.get(position));
        holder.commentAuthor.setText(commentDataAut.get(position));
        holder.commentTimeStamp.setText(commentDataTime.get(position));

    }


    @Override
    public int getItemCount() {
        return commentData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView comment;
        TextView commentAuthor;
        TextView commentTimeStamp;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            comment = itemView.findViewById(R.id.text_comments);
            commentAuthor = itemView.findViewById(R.id.commentAuthor);
            commentTimeStamp = itemView.findViewById(R.id.commentTimeStamp);
        }
    }
}
