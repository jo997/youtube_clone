package de.unituebingen.streamapp.categoryManagement.VideosFromCat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.unituebingen.streamapp.R;

public class VidCatAdapter extends RecyclerView.Adapter<VidCatAdapter.MyViewHolder> {

    ArrayList<String> catDataVids = new ArrayList<String>();
    Context context;
    private VidCatAdapter.OnVidListener mOnVidListener;

    public VidCatAdapter(Context ct, ArrayList llCatV, OnVidListener onVidListener) {
        context = ct;
        catDataVids = llCatV;
        this.mOnVidListener = onVidListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.category_row, parent, false);
        return new MyViewHolder(view, mOnVidListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.vid.setText(catDataVids.get(position));
    }

    @Override
    public int getItemCount() {
        return catDataVids.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView vid;
        OnVidListener onVidListener;

        public MyViewHolder(@NonNull View itemView, OnVidListener onVidListener) {
            super(itemView);
            vid = itemView.findViewById(R.id.pl_text);
            this.onVidListener = onVidListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onVidListener.onVidClick(getAdapterPosition());
        }
    }

    // interface to send Adapterposition to Model
    public interface OnVidListener {
        void onVidClick(int position);
    }

}
