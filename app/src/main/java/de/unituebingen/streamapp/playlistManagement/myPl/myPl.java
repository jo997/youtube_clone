package de.unituebingen.streamapp.playlistManagement.myPl;

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

import java.util.ArrayList;

import de.unituebingen.streamapp.R;
import de.unituebingen.streamapp.UserData;
import de.unituebingen.streamapp.tools.Playlist;

public class myPl extends Fragment implements myPlAdapter.OnPlListenerP {


    private static final String TAG = "degub msg";
    private RecyclerView rvListMyPlaylist;
    private MyPlViewModel mViewModel;
    ArrayList<String> listPlP = new ArrayList<String>();

    Fragment context = this;

    public static myPl newInstance() {
        return new myPl();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_pl_fragment, container, false);
        rvListMyPlaylist = view.findViewById(R.id.rvMyPl);
        myPlAdapter myAdapter = new myPlAdapter(getActivity(), listPlP, this::OnPlClick);
        rvListMyPlaylist.setAdapter(myAdapter);
        rvListMyPlaylist.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MyPlViewModel.class);

        Fragment context = this;
        UserData ud = new UserData(this.getActivity());
        int id = ud.getTypeId();

        // set observer
        Observer<ArrayList<Playlist>> PlayListObserver = new Observer<ArrayList<Playlist>>() {
            @Override
            public void onChanged(ArrayList<Playlist> pl) {
                ArrayList<String> listPlP = new ArrayList<>();
                if (pl != null) {
                    for (Playlist playlist : pl)
                        listPlP.add(playlist.getName());
                    myPlAdapter plAdapter = new myPlAdapter(getActivity(), listPlP, myPl.this::OnPlClick);
                    plAdapter.notifyDataSetChanged();
                    rvListMyPlaylist.setAdapter(plAdapter);
                    Log.d(TAG, "onChangeddddddddd: " + listPlP);
                }
            }
        };
        mViewModel.getPlaylistData(id).observe(context.getActivity(), PlayListObserver);

        ArrayList<Playlist> a = mViewModel.getPlaylistData(id).getValue();
        Log.d(TAG, "onActivityCreated: " + a);


    }

    @Override
    public void OnPlClick(int position) {
        UserData ud = new UserData(this.getActivity());
        int id = ud.getTypeId();
        Bundle bundle = new Bundle();
        ArrayList<Playlist> a = mViewModel.getPlaylistData(id).getValue();
        Playlist pl = a.get(position);
        int ID = pl.getId();
        bundle.putInt("12345", ID);
        Log.d(TAG, "OnVidClick: Clicked " + ID);
        NavHostFragment.findNavController(context).navigate(R.id.action_myPl_to_nav_playlist, bundle);
        // show Videos of selected Playlist
    }
}