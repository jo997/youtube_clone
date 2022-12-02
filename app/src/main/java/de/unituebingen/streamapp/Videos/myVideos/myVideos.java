package de.unituebingen.streamapp.Videos.myVideos;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

import de.unituebingen.streamapp.R;
import de.unituebingen.streamapp.Videos.VidAdapter;
import de.unituebingen.streamapp.tools.Video;

public class myVideos extends Fragment {

    private static final String TAG = "debug msg";
    private SwipeRefreshLayout refreshVids;
    private RecyclerView rvListVids;
    private SearchView videoSearch;
    Fragment context = this;
    private MyVideosViewModel mViewModel;
    int globalCount = 0;

    public static myVideos newInstance() {
        return new myVideos();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_videos_fragment, container, false);

        refreshVids = view.findViewById(R.id.refreshMyVids);
        rvListVids = view.findViewById(R.id.recyclerMyVideos);
        videoSearch = view.findViewById(R.id.VideoSearchMyVids);
        rvListVids.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MyVideosViewModel.class);
        Fragment context = this;

        // set observer
        Observer<ArrayList<Video>> VidObserver = new Observer<ArrayList<Video>>() {
            int count = 20;
            @Override
            public void onChanged(ArrayList<Video> videoList) {
                //get video titles
                if (videoList != null) {
                    ArrayList<String> listVidsT = new ArrayList<>();
                    for (Video vid : videoList)
                        listVidsT.add(vid.getTitle());

                    VidAdapter vidAdapter = new VidAdapter(getActivity(), listVidsT, myVideos.this::OnVidClick, myVideos.this::menuOption);
                    vidAdapter.notifyDataSetChanged();
                    rvListVids.setAdapter((vidAdapter));
                }

                // load more Videos at the end of RecyclerList and update rvlist
                LinearLayoutManager layoutManager = ((LinearLayoutManager) rvListVids.getLayoutManager());
                rvListVids.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        int lastPos = layoutManager.findLastVisibleItemPosition();
                        //load new videos
                        if (lastPos == 19) {
                            count += 10;
                            mViewModel.getMyVideosData(count);
                            layoutManager.scrollToPosition(1);
                            globalCount = count;
                        }
                        // load old videos
                        if (layoutManager.findFirstCompletelyVisibleItemPosition() == 0 && count != 0) {
                            count -= 10;
                            mViewModel.getMyVideosData(count);
                            layoutManager.scrollToPosition(2);
                            globalCount = count;
                        }
                    }
                });

            }

        };

        // make observer observe the data
        mViewModel.getMyVideosData(globalCount).observe(context.getActivity(), VidObserver);

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
                ArrayList<Video> videoList = mViewModel.getMyVideosData(globalCount).getValue();
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


        // load more Videos at the end of RecyclerList
        LinearLayoutManager layoutManager = ((LinearLayoutManager) rvListVids.getLayoutManager());
        rvListVids.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();
                if (lastVisiblePosition == recyclerView.getChildCount()) {
                    // load more videos still missing
                    Log.d(TAG, "onScrolled: at the end");
                }
            }
        });
    }

    // send position to VideoView and play video
    public void OnVidClick(int position) {
        ArrayList<Video> videoList = mViewModel.getMyVideosData(globalCount).getValue();
        Bundle bundle = new Bundle();
        Video vid = videoList.get(position);
        int ID = vid.getId();
        bundle.putInt("123", ID);
        NavHostFragment.findNavController(context).navigate(R.id.action_myVideos_to_nav_myVideoView, bundle);
    }

    public void menuOption(String option, int position) {
    }
}