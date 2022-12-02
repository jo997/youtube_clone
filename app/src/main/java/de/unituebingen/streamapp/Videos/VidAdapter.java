package de.unituebingen.streamapp.Videos;

import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.unituebingen.streamapp.R;


public class VidAdapter extends RecyclerView.Adapter<VidAdapter.MyViewHolder> {

    private VideosViewModel mViewModel;
    private static final String TAG = "";
    ArrayList<String> vidData = new ArrayList<String>();
    Context context;
    private OnVidListener mOnVidListener;
    private Option mOption;


    public VidAdapter(Context ct, ArrayList llVid, OnVidListener onVidListener, Option option) {
        context = ct;
        vidData = llVid;
        this.mOnVidListener = onVidListener;
        this.mOption = option;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.video_row, parent, false);
        return new de.unituebingen.streamapp.Videos.VidAdapter.MyViewHolder(view, mOnVidListener, mOption);
    }

    @Override
    public void onBindViewHolder(@NonNull VidAdapter.MyViewHolder holder, int position) {
        holder.vids.setText(vidData.get(position));
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(holder.getLayoutPosition());
                return false;
            }
        });
    }

    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public int getItemCount() {

        return vidData.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

        TextView vids;
        OnVidListener onVidListener;
        Option option;
        String opt;

        public MyViewHolder(@NonNull View itemView, OnVidListener onVidListener, Option option) {
            super(itemView);
            this.onVidListener = onVidListener;
            this.option = option;
            vids = itemView.findViewById(R.id.video_text);
            itemView.setOnClickListener(this::onClick);
            itemView.setOnCreateContextMenuListener(this::onCreateContextMenu);

        }


        @Override
        public void onClick(View view) {
            onVidListener.OnVidClick(getAdapterPosition());
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Select your action");
            MenuItem cat = contextMenu.add(contextMenu.NONE, 1, 1, "add Video to category");
            MenuItem pl = contextMenu.add(contextMenu.NONE, 2, 2, "add Video to playlist");
            MenuItem dl = contextMenu.add(contextMenu.NONE, 3, 3, "delete Video");
            cat.setOnMenuItemClickListener(onChange);
            pl.setOnMenuItemClickListener(onChange);
            dl.setOnMenuItemClickListener(onChange);
        }


        final MenuItem.OnMenuItemClickListener onChange = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case 1:
                        Log.d(TAG, "onMenuItemClick: " + "Cat");
                        opt = "Cat";
                        option.menuOption(opt, position);
                        return true;
                    case 2:
                        Log.d(TAG, "onMenuItemClick: " + "Pl");
                        opt = "Pl";
                        option.menuOption(opt, position);
                        return true;
                    case 3:
                        Log.d(TAG, "onMenuItemClick: " + "Del");
                        opt = "Del";
                        option.menuOption(opt, position);
                        return true;
                }
                return false;
            }
        };

    }

    // interface to send Adapterposition to Model
    public interface OnVidListener {
        void OnVidClick(int position);
    }

    // interface to send selected optionsmenu to Model
    public interface Option {
        void menuOption(String option, int position);
    }
}

