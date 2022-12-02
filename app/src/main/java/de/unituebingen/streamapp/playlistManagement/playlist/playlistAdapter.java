package de.unituebingen.streamapp.playlistManagement.playlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.unituebingen.streamapp.R;

public class playlistAdapter extends RecyclerView.Adapter<playlistAdapter.MyViewHolder> {

    ArrayList<String> PlData = new ArrayList<String>();
    private OnVideoListener mOnVideoListener;
    Context context;

    public playlistAdapter(Context ct, ArrayList llPlaylist, OnVideoListener onVideoListener) {
        context = ct;
        PlData = llPlaylist;
        this.mOnVideoListener = onVideoListener;
    }

    @NonNull
    @Override
    public playlistAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.playlist_row2, parent, false);
        return new playlistAdapter.MyViewHolder(view, mOnVideoListener);
    }

    @Override
    public void onBindViewHolder(@NonNull playlistAdapter.MyViewHolder holder, int position) {
        holder.playlist.setText(PlData.get(position));
    }

    @Override
    public int getItemCount() {
        return PlData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView playlist;
        OnVideoListener onVideoListener;

        public MyViewHolder(@NonNull View itemView, OnVideoListener onVideoListener) {
            super(itemView);
            this.onVideoListener = mOnVideoListener;
            playlist = itemView.findViewById(R.id.pl_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onVideoListener.OnVideoClick(getAdapterPosition());
        }
    }

    // interface to send Adapterposition to Model
    public interface OnVideoListener {
        void OnVideoClick(int position);
    }

}
