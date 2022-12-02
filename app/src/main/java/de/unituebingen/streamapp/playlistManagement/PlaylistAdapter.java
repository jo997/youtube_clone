package de.unituebingen.streamapp.playlistManagement;

import android.content.Context;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.unituebingen.streamapp.R;


public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.MyViewHolder> {

    ArrayList<String> PlData = new ArrayList<String>();
    private OnPlListener mOnPlListener;
    Context context;


    public PlaylistAdapter(Context ct, ArrayList llPlaylist, OnPlListener onPlListener) {
        context = ct;
        PlData = llPlaylist;
        this.mOnPlListener = onPlListener;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.playlist_row, parent, false);

        return new PlaylistAdapter.MyViewHolder(view, mOnPlListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.playlist.setText(PlData.get(position));
    }

    @Override
    public int getItemCount() {
        return PlData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView playlist;
        OnPlListener onPlListener;

        public MyViewHolder(@NonNull View itemView, OnPlListener onPlListener) {
            super(itemView);
            this.onPlListener = onPlListener;
            playlist = itemView.findViewById(R.id.playlist_text);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onPlListener.OnPlClick(getAdapterPosition());
        }

    }

    // interface to send Adapterposition to Model
    public interface OnPlListener {
        void OnPlClick(int position);
    }

}
