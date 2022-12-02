package de.unituebingen.streamapp.playlistManagement.addToPl;

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
import android.widget.Toast;

import java.util.ArrayList;

import de.unituebingen.streamapp.R;
import de.unituebingen.streamapp.playlistManagement.PlaylistAdapter;
import de.unituebingen.streamapp.tools.Video;

public class addVidToPlaylist extends Fragment {

    private static final String TAG = "degub msg";
    private RecyclerView rvListPlaylistAdd;
    ArrayList<String> listPl = new ArrayList<String>();
    Fragment context = this;

    private AddVidToPlaylistViewModel mViewModel;

    public static addVidToPlaylist newInstance() {
        return new addVidToPlaylist();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_vid_to_playlist_fragment, container, false);

        rvListPlaylistAdd = view.findViewById(R.id.recyclerViewPlaylistAdd);
        rvListPlaylistAdd.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AddVidToPlaylistViewModel.class);
        Fragment context = this;

        // set observer
        Observer<ArrayList<Video>> PlayListObserver = new Observer<ArrayList<Video>>() {
            @Override
            public void onChanged(ArrayList<Video> videos) {
                ArrayList<String> listPl = new ArrayList<>();
                for (Video vid : videos)
                    listPl.add(vid.getTitle());
                PlaylistAdapter plAdapter = new PlaylistAdapter(getActivity(), listPl, addVidToPlaylist.this::OnPlClick);
                plAdapter.notifyDataSetChanged();
                rvListPlaylistAdd.setAdapter((plAdapter));
            }
        };

        mViewModel.getPlaylistData().observe(context.getActivity(), PlayListObserver);

    }

    public void OnPlClick(int position) {
        int ID = getArguments().getInt("1234");
        Log.d(TAG, "OnPlClick: " + "Video with ID" + ID + "to Playlist" + position);
        // get Playlist ID from position
        // add video to selected playlist
        if (mViewModel.updatePlaylist(position, ID, getActivity())) {
            Toast.makeText(context.getActivity().getApplicationContext(), "video added to playlist", Toast.LENGTH_SHORT).show();
            NavHostFragment.findNavController(context).navigate(R.id.action_addVidToPlaylist_to_nav_home);
        } else {
            Toast.makeText(context.getActivity().getApplicationContext(), "could not update playlist", Toast.LENGTH_SHORT).show();
        }
    }

}