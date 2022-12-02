package de.unituebingen.streamapp.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.unituebingen.streamapp.R;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

    ArrayList<String> homeData = new ArrayList<String>();
    Context context;
    private OnHomeListener mOnHomeListener;


    public HomeAdapter(Context ct, ArrayList llHome, OnHomeListener onHomeListener) {
        context = ct;
        homeData = llHome;
        this.mOnHomeListener = onHomeListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.home_row, parent, false);
        return new MyViewHolder(view, mOnHomeListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.homeText.setText(homeData.get(position));
    }

    @Override
    public int getItemCount() {
        return homeData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView homeText;
        OnHomeListener onHomeListener;

        public MyViewHolder(@NonNull View itemView, OnHomeListener onHomeListener) {
            super(itemView);
            this.onHomeListener = onHomeListener;
            homeText = itemView.findViewById(R.id.home_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onHomeListener.OnHomeVidClick(getAdapterPosition());
        }
    }

    // interface to send Adapterposition to Model
    public interface OnHomeListener {
        void OnHomeVidClick(int position);
    }

}
