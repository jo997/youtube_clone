package de.unituebingen.streamapp.playlistManagement.playlist;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import de.unituebingen.streamapp.R;
import de.unituebingen.streamapp.UserData;
import de.unituebingen.streamapp.tools.Video;

public class playlist extends Fragment implements playlistAdapter.OnVideoListener {

    private static final String TAG = "degub msg";
    private RecyclerView rvListPlaylistVids;
    private PlaylistViewModel mViewModel;
    private TextView plName;
    ArrayList<String> listVid = new ArrayList<String>();
    Fragment context = this;
    int globalCount = 0;
    private Button Vid_up;
    private Button Vid_down;

    public static playlist newInstance() {
        return new playlist();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.playlist_fragment, container, false);
        rvListPlaylistVids = view.findViewById(R.id.rvVideosOfPl);
        plName = view.findViewById(R.id.plName);
        Fragment context = this;
        playlistAdapter myAdapter = new playlistAdapter(getActivity(), listVid, this::OnVideoClick);
        rvListPlaylistVids.setAdapter(myAdapter);
        rvListPlaylistVids.setLayoutManager(new LinearLayoutManager(getActivity()));
        Vid_down = view.findViewById(R.id.down_btn);
        Vid_up = view.findViewById(R.id.up_btn);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PlaylistViewModel.class);
        UserData ud = new UserData(this.getActivity());
        int id = ud.getUserId();

        int PlID = getArguments().getInt("12345");
        ArrayList<Video> a = mViewModel.getPlaylistData(PlID, globalCount).getValue();
        Log.d(TAG, "onActivityCreated: " + PlID + a);

        // set observer
        Observer<ArrayList<Video>> PlayListObserver = new Observer<ArrayList<Video>>() {
            int count = 20;

            @Override
            public void onChanged(ArrayList<Video> videos) {
                ArrayList<String> listVid = new ArrayList<>();
                if (videos != null) {
                    for (Video vid : videos)
                        listVid.add(vid.getTitle());
                    playlistAdapter plAdapter = new playlistAdapter(getActivity(), listVid, playlist.this::OnVideoClick);
                    plAdapter.notifyDataSetChanged();
                    rvListPlaylistVids.setAdapter(plAdapter);
                    Log.d(TAG, "onChanged: " + listVid);
                }
                // load more Videos at the end of RecyclerList and update rvlist
                LinearLayoutManager layoutManager = ((LinearLayoutManager) rvListPlaylistVids.getLayoutManager());
                rvListPlaylistVids.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        int lastPos = layoutManager.findLastVisibleItemPosition();
                        //load new videos
                        if (lastPos == 19) {
                            count += 10;
                            mViewModel.getPlaylistData(PlID, count);
                            layoutManager.scrollToPosition(1);
                            globalCount = count;
                        }
                        // load old videos
                        if (layoutManager.findFirstCompletelyVisibleItemPosition() == 0 && count != 0) {
                            count -= 10;
                            mViewModel.getPlaylistData(PlID, count);
                            layoutManager.scrollToPosition(2);
                            globalCount = count;
                        }
                    }
                });
            }
        };
        mViewModel.getPlaylistData(PlID, globalCount).observe(context.getActivity(), PlayListObserver);
        ArrayList<Video> b = mViewModel.getPlaylistData(PlID, globalCount).getValue();
        Log.d(TAG, "onActivityCreated: " + b);

/*
        Vid_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //logik and rr still missing

            }
        });

        Vid_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //logik and rr still missing

            }
        });*/

    }


    @Override
    public void OnVideoClick(int position) {
        int PlID = getArguments().getInt("12345");
        ArrayList<Video> videoList = mViewModel.getPlaylistData(PlID, globalCount).getValue();
        Bundle bundle = new Bundle();
        Video vid = videoList.get(position);
        ArrayList<String> wuu = new ArrayList<>();
        int ID = vid.getId();
        bundle.putInt("123", ID);
        NavHostFragment.findNavController(context).navigate(R.id.action_nav_playlist_to_nav_myVideoView, bundle);
        // show selected Video
    }
}
