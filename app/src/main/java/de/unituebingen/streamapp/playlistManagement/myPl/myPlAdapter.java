package de.unituebingen.streamapp.playlistManagement.myPl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.unituebingen.streamapp.R;


public class myPlAdapter extends RecyclerView.Adapter<myPlAdapter.MyViewHolder> {

    ArrayList<String> PlData = new ArrayList<String>();
    private OnPlListenerP mOnPlListenerP;
    Context context;

    public myPlAdapter(Context ct, ArrayList llPlaylist, OnPlListenerP onPlListenerP) {
        context = ct;
        PlData = llPlaylist;
        this.mOnPlListenerP = onPlListenerP;
    }

    @NonNull
    @Override
    public myPlAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.playlist_row, parent, false);
        return new myPlAdapter.MyViewHolder(view, mOnPlListenerP);
    }

    @Override
    public void onBindViewHolder(@NonNull myPlAdapter.MyViewHolder holder, int position) {
        holder.playlist.setText(PlData.get(position));
    }

    @Override
    public int getItemCount() {
        return PlData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView playlist;
        OnPlListenerP onPlListenerP;

        public MyViewHolder(@NonNull View itemView, OnPlListenerP onPlListenerP) {
            super(itemView);
            this.onPlListenerP = onPlListenerP;
            playlist = itemView.findViewById(R.id.pl_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }

    // interface to send Adapterposition to Model
    public interface OnPlListenerP {
        void OnPlClick(int position);
    }

}
