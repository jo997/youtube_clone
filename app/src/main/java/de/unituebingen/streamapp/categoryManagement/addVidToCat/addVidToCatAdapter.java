package de.unituebingen.streamapp.categoryManagement.addVidToCat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.unituebingen.streamapp.R;

public class addVidToCatAdapter extends RecyclerView.Adapter<addVidToCatAdapter.MyViewHolder> {
    ArrayList<String> catData = new ArrayList<String>();
    Context context;
    private WhichCatListener mwhichCatListener;

    public addVidToCatAdapter(Context ct, ArrayList llCat, WhichCatListener whichCatListener) {
        context = ct;
        catData = llCat;
        this.mwhichCatListener = whichCatListener;
    }

    @NonNull
    @Override
    public addVidToCatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.category_row, parent, false);
        return new MyViewHolder(view, mwhichCatListener);
    }

    @Override
    public void onBindViewHolder(@NonNull addVidToCatAdapter.MyViewHolder holder, int position) {
        holder.category.setText(catData.get(position));
    }

    @Override
    public int getItemCount() {
        return catData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView category;
        WhichCatListener whichCatListener;

        public MyViewHolder(@NonNull View itemView, WhichCatListener whichCatListener) {
            super(itemView);
            this.whichCatListener = whichCatListener;
            category = itemView.findViewById(R.id.pl_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            whichCatListener.onWCatClick(getAdapterPosition());
        }
    }

    // interface to send Adapterposition to Model
    public interface WhichCatListener {
        void onWCatClick(int position);
    }
}
