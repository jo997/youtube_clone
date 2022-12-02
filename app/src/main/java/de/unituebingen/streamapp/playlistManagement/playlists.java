package de.unituebingen.streamapp.playlistManagement;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import de.unituebingen.streamapp.R;
import de.unituebingen.streamapp.UserData;
import de.unituebingen.streamapp.categoryManagement.CatAdapter;
import de.unituebingen.streamapp.categoryManagement.categories;
import de.unituebingen.streamapp.tools.Comment;
import de.unituebingen.streamapp.tools.Playlist;
import de.unituebingen.streamapp.tools.Video;
import de.unituebingen.streamapp.tools.VideoCategory;

public class playlists extends Fragment implements PlaylistAdapter.OnPlListener {

    private static final String TAG = "degub msg";
    private EditText plName;
    private Button addPlaylist;
    private Button myPlaylist;
    private RecyclerView rvListPublicPlaylist;
    ArrayList<String> listPl = new ArrayList<String>();
    Fragment context = this;
    int globalCount = 0;

    private PlaylistsViewModel mViewModel;

    public static playlists newInstance() {
        return new playlists();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.playlists_fragment, container, false);

        addPlaylist = view.findViewById(R.id.addPlaylist_bt);
        myPlaylist = view.findViewById(R.id.myPl_btn);
        plName = view.findViewById(R.id.playlistName);
        rvListPublicPlaylist = view.findViewById(R.id.recyclerViewPublicPlaylists);
        PlaylistAdapter plAdapter = new PlaylistAdapter(getActivity(), listPl, this::OnPlClick);
        rvListPublicPlaylist.setAdapter(plAdapter);
        rvListPublicPlaylist.setLayoutManager(new LinearLayoutManager(getActivity()));
        Fragment context = this;

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PlaylistsViewModel.class);
        UserData ud = new UserData(this.getActivity());
        int id = ud.getUserId();


        // set observer
        Observer<ArrayList<Playlist>> PlaylistObs = new Observer<ArrayList<Playlist>>() {
            int count = 20;

            @Override
            public void onChanged(ArrayList<Playlist> playlists) {

                ArrayList<String> listPl = new ArrayList<>();
                if (playlists != null) {
                    for (Playlist playlist : playlists)
                        listPl.add(playlist.getName());

                    PlaylistAdapter plAdapter = new PlaylistAdapter(getActivity(), listPl, playlists.this::OnPlClick);
                    plAdapter.notifyDataSetChanged();
                    rvListPublicPlaylist.setAdapter(plAdapter);

                    // set item touch helper to enable swiping
                    new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {

                        @Override
                        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                            return false;
                        }

                        @Override
                        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                            UserData ud = new UserData(getActivity());
                            String auth = ud.getAuthentication();
                            int a = ud.getTypeId();
                            Playlist playlistToDel = playlists.get(viewHolder.getAdapterPosition());
                            int ID_Pl = playlistToDel.getId();
                            int ID_Owner = playlistToDel.getOwnerId();
                            int me = ud.getUserId();
                            if (direction == ItemTouchHelper.RIGHT) {
                                if (TextUtils.isEmpty(auth)) {
                                    Toast.makeText(context.getActivity().getApplicationContext(), "You need to login to delete a playlist", Toast.LENGTH_SHORT).show();
                                    plAdapter.notifyDataSetChanged();
                                } else if (a > 3 || ID_Pl == ID_Owner) {
                                    listPl.remove(viewHolder.getAdapterPosition());
                                    mViewModel.deletePlaylist(me, ID_Pl, getActivity());
                                    // update Adapter
                                    plAdapter.notifyDataSetChanged();
                                    Toast.makeText(context.getActivity().getApplicationContext(), "playlist deleted", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context.getActivity().getApplicationContext(), "You don't have the permissions to delete a playlist", Toast.LENGTH_SHORT).show();
                                    plAdapter.notifyDataSetChanged();
                                }
                            }
                            if (direction == ItemTouchHelper.LEFT) {
                                if (TextUtils.isEmpty(auth)) {
                                    Toast.makeText(context.getActivity().getApplicationContext(), "You need to login to subscribe to a playlist", Toast.LENGTH_SHORT).show();
                                    plAdapter.notifyDataSetChanged();
                                } else {
                                    listPl.remove(viewHolder.getAdapterPosition());
                                    mViewModel.subscribe(me, ID_Pl, getActivity());
                                    // update Adapter
                                    plAdapter.notifyDataSetChanged();
                                    Toast.makeText(context.getActivity().getApplicationContext(), "successfully subscribed to playlist", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                                // Get RecyclerView item from the ViewHolder
                                View itemView = viewHolder.itemView;

                                // make background red when swiping
                                Paint p = new Paint();
                                if (dX > 0) {
                                    p.setColor(Color.RED);
                                    // Draw Rect with varying right side, equal to displacement dX
                                    c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX,
                                            (float) itemView.getBottom(), p);
                                }
                                if (dX < 0) {
                                    p.setColor(Color.GRAY);
                                    // Draw Rect with varying right side, equal to displacement dX
                                    c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX,
                                            (float) itemView.getBottom(), p);
                                }
                                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                            }
                        }
                    }).attachToRecyclerView(rvListPublicPlaylist);

                    // load more Videos at the end of RecyclerList and update rvlist
                    LinearLayoutManager layoutManager = ((LinearLayoutManager) rvListPublicPlaylist.getLayoutManager());
                    rvListPublicPlaylist.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            int lastPos = layoutManager.findLastVisibleItemPosition();
                            //load new videos
                            if (lastPos == 19) {
                                count += 10;
                                mViewModel.getPublicPlaylistData(count);
                                layoutManager.scrollToPosition(1);
                                globalCount = count;
                            }
                            // load old videos
                            if (layoutManager.findFirstCompletelyVisibleItemPosition() == 0 && count != 0) {
                                count -= 10;
                                mViewModel.getPublicPlaylistData(count);
                                layoutManager.scrollToPosition(2);
                                globalCount = count;
                            }
                        }
                    });
                }

                if (playlists == null) {
                    Toast.makeText(context.getActivity().getApplicationContext(), "no Playlists available", Toast.LENGTH_SHORT).show();
                }

            }
        };


        mViewModel.getPublicPlaylistData(globalCount).observe(context.getActivity(), PlaylistObs);


        ArrayList<Playlist> publicAll = mViewModel.getPublicPlaylistData(globalCount).getValue();
        Log.d(TAG, "onActivityCreated: " + "Public" + publicAll);

        ArrayList<Playlist> pAll = mViewModel.getPlaylistData(id).getValue();
        Log.d(TAG, "onActivityCreated: " + "   " + id + " " + "private" + pAll);


        myPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Playlist> c = mViewModel.getPlaylistData(id).getValue();
                Log.d(TAG, "onClick: " + c);
                if (mViewModel.getPlaylistData(id).getValue() == null) {
                    Toast.makeText(context.getActivity().getApplicationContext(), "you need to create a playlist first", Toast.LENGTH_SHORT).show();
                } else {
                    NavHostFragment.findNavController(context).navigate(R.id.action_nav_playlists_to_myPl);
                }
            }
        });


        addPlaylist.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String newName = plName.getText().toString();
                mViewModel.updatePlaylist(newName, getActivity());
                Log.d(TAG, "onChanged: ");
            }
        });

    }


    //show videos of playlist in fragment, check if logged in or not?
    @Override
    public void OnPlClick(int position) {
        Bundle bundle = new Bundle();
        ArrayList<Playlist> a = mViewModel.getPublicPlaylistData(globalCount).getValue();
        Playlist pl = a.get(position);
        int ID = pl.getId();
        bundle.putInt("12345", ID);
        Log.d(TAG, "OnVidClick: Clicked " + ID);
        NavHostFragment.findNavController(context).navigate(R.id.action_nav_playlists_to_nav_playlist, bundle);
        //show Videos of selected playlist
    }
}
