package de.unituebingen.streamapp.categoryManagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.unituebingen.streamapp.R;

public class CatAdapter extends RecyclerView.Adapter<CatAdapter.MyViewHolder> {

    ArrayList<String> catData = new ArrayList<String>();
    Context context;
    private OnCatListener mOnCatListener;

    public CatAdapter(Context ct, ArrayList llCat, OnCatListener onCatListener) {
        context = ct;
        catData = llCat;
        this.mOnCatListener = onCatListener;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.category_row, parent, false);
        return new MyViewHolder(view, mOnCatListener);
    }

    @Override   //load categories and fill rvList
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.category.setText(catData.get(position));
    }

    @Override
    public int getItemCount() {
        return catData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView category;
        OnCatListener onCatListener;

        public MyViewHolder(@NonNull View itemView, OnCatListener onCatListener) {
            super(itemView);
            this.onCatListener = onCatListener;
            category = itemView.findViewById(R.id.pl_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onCatListener.onCatClick(getAdapterPosition());
        }
    }

    // interface to send Adapterposition to Model
    public interface OnCatListener {
        void onCatClick(int position);
    }
}

