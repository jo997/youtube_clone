package de.unituebingen.streamapp.Home;

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


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import de.unituebingen.streamapp.R;
import de.unituebingen.streamapp.tools.Video;

public class Home extends Fragment implements HomeAdapter.OnHomeListener {

    private static final String TAG = "debug msg";
    private SwipeRefreshLayout refreshVidsHome;
    private RecyclerView rvListVidsHome;
    ArrayList<String> listVids = new ArrayList<String>();
    Fragment context = this;
    private HomeViewModel mViewModel;
    int b;


    public static Home newInstance() {
        return new Home();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        rvListVidsHome = view.findViewById(R.id.recyclerVideosHome);
        HomeAdapter myAdapter = new HomeAdapter(getActivity(), listVids, this::OnHomeVidClick);
        rvListVidsHome.setAdapter(myAdapter);
        rvListVidsHome.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        Fragment context = this;


        // set observer
        Observer<ArrayList<Video>> HomeObserver = new Observer<ArrayList<Video>>() {
            int count = 20;

            @Override
            public void onChanged(ArrayList<Video> videoList) {
                ArrayList<String> listVids = new ArrayList<>();
                if (videoList != null) {
                    for (Video vid : videoList)
                        listVids.add(vid.getTitle());
                } else {
                    Toast.makeText(context.getActivity().getApplicationContext(), "no videos available", Toast.LENGTH_SHORT).show();
                }
                HomeAdapter homeAdapter = new HomeAdapter(getActivity(), listVids, Home.this::OnHomeVidClick);
                homeAdapter.notifyDataSetChanged();
                rvListVidsHome.setAdapter(homeAdapter);
                Log.d(TAG, "onChanged: " + listVids);


                // load more Videos at the end of RecyclerList and update rvlist
                LinearLayoutManager layoutManager = ((LinearLayoutManager) rvListVidsHome.getLayoutManager());
                rvListVidsHome.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        int lastPos = layoutManager.findLastVisibleItemPosition();
                        Log.d(TAG, "onScrolled: " + count);
                        Log.d(TAG, "onScrolled: " + lastPos);
                        //load new videos
                        if (lastPos == 19) {
                            count += 10;
                            mViewModel.getVideosDataHome(count);
                            layoutManager.scrollToPosition(1);
                            b = count;
                        }
                        // load old videos
                        if (layoutManager.findFirstCompletelyVisibleItemPosition() == 0 && count != 0) {
                            count -= 10;
                            mViewModel.getVideosDataHome(count);
                            layoutManager.scrollToPosition(2);
                            b = count;
                        }
                    }
                });
            }
        };

        // make observer observe the data
        mViewModel.getVideosDataHome(b).observe(context.getActivity(), HomeObserver);
    }


    //play video with ViewView fragment
    @Override
    public void OnHomeVidClick(int position) {
        ArrayList<Video> videoList = mViewModel.getVideosDataHome(b).getValue();
        Bundle bundle = new Bundle();
        Video vid = videoList.get(position);
        int ID = vid.getId();
        bundle.putInt("123", ID);
        NavHostFragment.findNavController(context).navigate(R.id.action_nav_home_to_nav_myVideoView, bundle);
        // show selected Video
    }
}