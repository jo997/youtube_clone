package de.unituebingen.streamapp.Videos;

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

import de.unituebingen.streamapp.R;


import de.unituebingen.streamapp.UserData;
import de.unituebingen.streamapp.tools.Video;
import de.unituebingen.streamapp.tools.wrapper.Wrapper;

public class Videos extends Fragment implements VidAdapter.OnVidListener, VidAdapter.Option {


    private static final String TAG = "debug msg";
    private SwipeRefreshLayout refreshVids;
    private RecyclerView rvListVids;
    private SearchView videoSearch;
    private Button myVidButton;
    Fragment context = this;
    private VideosViewModel mViewModel;
    int globalCount = 0;

    public static Videos newInstance() {
        return new Videos();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.videos_fragment, container, false);

        refreshVids = view.findViewById(R.id.refreshVids);
        rvListVids = view.findViewById(R.id.recyclerVideos);
        videoSearch = view.findViewById(R.id.VideoSearch);
        myVidButton = view.findViewById(R.id.myVids_btn);
        rvListVids.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(VideosViewModel.class);
        Fragment context = this;


        // set observer
        Observer<ArrayList<Video>> VidObserver = new Observer<ArrayList<Video>>() {
            int count = 20;

            @Override
            public void onChanged(ArrayList<Video> videoList) {
                //get video title
                ArrayList<String> listVidsT = new ArrayList<>();
                if (videoList != null) {
                    for (Video vid : videoList)
                        listVidsT.add(vid.getTitle());
                    // get video Preview but not working
                    ArrayList<Wrapper<Video.Thumbnail>> listVidsPrev = new ArrayList<>();
                    for (Video vid : videoList)
                        listVidsPrev.add(vid.getPreviews());
                    // get Uri for preview picture but not working
                    ArrayList<String> listVids = new ArrayList<String>();
                    for (Wrapper<Video.Thumbnail> t : listVidsPrev)
                        listVids.add(t.getEndpointUrl());
                    Log.d(TAG, "onChangedPreview: " + listVids);
                } else {
                    Toast.makeText(context.getActivity().getApplicationContext(), "no videos available", Toast.LENGTH_SHORT).show();
                }

                VidAdapter vidAdapter = new VidAdapter(getActivity(), listVidsT, Videos.this::OnVidClick, Videos.this::menuOption);
                vidAdapter.notifyDataSetChanged();
                rvListVids.setAdapter((vidAdapter));

                // set item touch helper
                new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                    UserData ud = new UserData(getActivity());
                    String auth = ud.getAuthentication();

                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }


                    // swipe to add video to a playlist
                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        if (TextUtils.isEmpty(auth)) {
                            Log.d(TAG, "onClickMyVideos: " + auth);
                            Toast.makeText(context.getActivity().getApplicationContext(), "You need to login to edit a playlist", Toast.LENGTH_SHORT).show();
                            vidAdapter.notifyDataSetChanged();
                            mViewModel.getVideosData(count);
                        } else {
                            ArrayList<Video> videoList = mViewModel.getVideosData(count).getValue();
                            Bundle bundle = new Bundle();
                            Video vid = videoList.get(viewHolder.getLayoutPosition());
                            int ID_Swap = vid.getId();
                            bundle.putInt("1234", ID_Swap);
                            Log.d(TAG, "onSwiped: " + ID_Swap);
                            NavHostFragment.findNavController(context).navigate(R.id.action_nav_videos_to_addVidToPlaylist, bundle);
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
                                p.setColor(Color.GRAY);
                                // Draw Rect with varying right side, equal to displacement dX
                                c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX,
                                        (float) itemView.getBottom(), p);
                            }
                            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                        }
                    }
                }).attachToRecyclerView(rvListVids);

                // load more Videos at the end of RecyclerList and update rvlist
                LinearLayoutManager layoutManager = ((LinearLayoutManager) rvListVids.getLayoutManager());
                rvListVids.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        int lastPos = layoutManager.findLastVisibleItemPosition();
                        //load new videos
                        if (lastPos == 19) {
                            count += 10;
                            mViewModel.getVideosData(count);
                            layoutManager.scrollToPosition(1);
                            globalCount = count;
                        }
                        // load old videos
                        if (layoutManager.findFirstCompletelyVisibleItemPosition() == 0 && count != 0) {
                            count -= 10;
                            mViewModel.getVideosData(count);
                            layoutManager.scrollToPosition(2);
                            globalCount = count;
                        }
                    }
                });


            }


        };

        // make observer observe the data
        mViewModel.getVideosData(globalCount).observe(context.getActivity(), VidObserver);

        // video search not working, waiting for global search to use
        videoSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                callSearch(query);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }

            public void callSearch(String query) {
                //load LiveData
                ArrayList<String> vidTitle = new ArrayList<>();
                ArrayList<Video> videoList = mViewModel.getVideosData(globalCount).getValue();
                for (Video vid : videoList)
                    vidTitle.add(vid.getTitle());

                CharSequence searchFor = videoSearch.getQuery();

                int pos = 0;
                for (Object element : videoList) {
                    if (videoList.contains(searchFor)) {
                        rvListVids.findViewHolderForLayoutPosition(pos);
                        rvListVids.getLayoutManager().scrollToPosition(pos);
                    } else {
                        final Toast noVids = Toast.makeText(getActivity(), "No Video found!", Toast.LENGTH_SHORT);  // search not working, only to test
                        noVids.show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                noVids.cancel();
                            }
                        }, 500);
                    }
                }
            }
        });


        // go to myVideos fragment if button clicked
        myVidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserData ud = new UserData(getActivity());
                String auth = ud.getAuthentication();
                int a = ud.getTypeId();

                if (TextUtils.isEmpty(auth)) {
                    Toast.makeText(context.getActivity().getApplicationContext(), "You need to login to see your Videos", Toast.LENGTH_SHORT).show();
                } else if (a < 2) {
                    Toast.makeText(context.getActivity().getApplicationContext(), "You dont't have the permission to upload videos", Toast.LENGTH_SHORT).show();
                } else {
                    NavHostFragment.findNavController(context).navigate(R.id.action_nav_videos_to_myVideos);
                }
            }
        });


        refreshVids.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                // do after 1 sec
                                refreshVids.setRefreshing(false);
                            }
                        }, 1000);   // for satisfaction


                    }
                }
        );

    }

    // send position to VideoView
    @Override
    public void OnVidClick(int position) {

        ArrayList<Video> videoList = mViewModel.getVideosData(globalCount).getValue();
        Bundle bundle = new Bundle();
        Video vid = videoList.get(position);
        ArrayList<String> wuu = new ArrayList<>();
        for (Video a : videoList) {
            wuu.add(a.getTitle());
        }
        Log.d(TAG, "OnVidClick: " + vid.getTitle() + " " + position + wuu);
        int ID = vid.getId();
        bundle.putInt("123", ID);
        NavHostFragment.findNavController(context).navigate(R.id.action_nav_videos_to_myVideoView, bundle);

    }

    @Override
    public void menuOption(String option, int position) {
        Log.d(TAG, "menuOption: " + option);
        ArrayList<Video> videoList = mViewModel.getVideosData(globalCount).getValue();
        Bundle bundle = new Bundle();
        Video vid = videoList.get(position);
        UserData ud = new UserData(getActivity());
        int a = ud.getTypeId();
        int VID = vid.getId();
        int uploadID = vid.getUploaderID();
        int userID = ud.getUserId();
        bundle.putInt("123", VID);
        if (option.equals("Pl")) {
            if (a > 1) {
                NavHostFragment.findNavController(context).navigate(R.id.action_nav_videos_to_addVidToPlaylist, bundle);
                Log.d(TAG, "onMenuItemClick: " + "Pl");
            } else {
                Toast.makeText(context.getActivity().getApplicationContext(), "Please login first", Toast.LENGTH_SHORT).show();
            }
        } else if (option.equals("Cat")) {
            if (a > 2) {
                NavHostFragment.findNavController(context).navigate(R.id.action_nav_videos_to_addVidToCat, bundle);
                Log.d(TAG, "onMenuItemClick: " + "Cat");
            } else {
                if (a < 2) {
                    Toast.makeText(context.getActivity().getApplicationContext(), "Please login first", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(context.getActivity().getApplicationContext(), "You don't have permission to do that", Toast.LENGTH_SHORT).show();
            }

        } else {
            Log.d(TAG, "onMenuItemClick: " + "Del");
            if (a > 3 || userID == uploadID) {
                mViewModel.deleteVid(VID, getActivity());
            } else {
                if (a < 2) {
                    Toast.makeText(context.getActivity().getApplicationContext(), "Please login first", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context.getActivity().getApplicationContext(), "You don't have permission to delete this Video", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}


