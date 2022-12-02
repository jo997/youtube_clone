package de.unituebingen.streamapp.categoryManagement.VideosFromCat;


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
import java.util.HashSet;
import java.util.Locale;

import de.unituebingen.streamapp.R;
import de.unituebingen.streamapp.tools.Video;

public class VidsByCat extends Fragment implements VidCatAdapter.OnVidListener {

    private static final String TAG = "degub msg";
    private RecyclerView rvListCatVids;
    ArrayList<String> listVid = new ArrayList<String>();
    Fragment context = this;
    int globalCount = 0;

    private VidsByCatViewModel mViewModel;

    public static VidsByCat newInstance() {
        return new VidsByCat();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vids_by_cat_fragment, container, false);
        rvListCatVids = view.findViewById(R.id.VidsFromCat);
        VidCatAdapter myVidCatAdapter = new VidCatAdapter(getActivity(), listVid, this::onVidClick);
        rvListCatVids.setAdapter(myVidCatAdapter);
        rvListCatVids.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(VidsByCatViewModel.class);
        Fragment context = this;
        int id = getArguments().getInt("1234567");
        Log.d(TAG, "onActivityCreated: " + id);

        // set observer
        Observer<ArrayList<Video>> VidObserver = new Observer<ArrayList<Video>>() {
            int count = 20;

            @Override
            public void onChanged(ArrayList<Video> videosOfCat) {
                ArrayList<String> listCat = new ArrayList<>();
                if (videosOfCat != null) {
                    for (Video vid : videosOfCat)
                        listCat.add(vid.getTitle());
                    VidCatAdapter myAdapter = new VidCatAdapter(getActivity(), listCat, VidsByCat.this::onVidClick);
                    myAdapter.notifyDataSetChanged();
                    rvListCatVids.setAdapter((myAdapter));
                    Log.d(TAG, "onChanged: " + listCat);
                }

                // load more Videos at the end of RecyclerList and update rvlist
                LinearLayoutManager layoutManager = ((LinearLayoutManager) rvListCatVids.getLayoutManager());
                rvListCatVids.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        int lastPos = layoutManager.findLastVisibleItemPosition();
                        //load new videos
                        if (lastPos == 19) {
                            count += 10;
                            mViewModel.getCategoriesVids(count, id);
                            layoutManager.scrollToPosition(1);
                            globalCount = count;
                        }
                        // load old videos
                        if (layoutManager.findFirstCompletelyVisibleItemPosition() == 0 && count != 0) {
                            count -= 10;
                            mViewModel.getCategoriesVids(count, id);
                            layoutManager.scrollToPosition(2);
                            globalCount = count;
                        }
                    }
                });
            }
        };
        mViewModel.getCategoriesVids(globalCount, id).observe(context.getActivity(), VidObserver);

        ArrayList<Video> cat = mViewModel.getCategory(774).getValue();
        ArrayList<Video> all = mViewModel.getCategoriesVids(globalCount, 829).getValue();
        Log.d(TAG, "onActivityCreated: " + all + "   " + cat + "   ");
    }

    @Override
    public void onVidClick(int position) {
        int id = getArguments().getInt("1234567");
        ArrayList<Video> videoList = mViewModel.getCategoriesVids(globalCount, id).getValue();
        Bundle bundle = new Bundle();
        Video vid = videoList.get(position);
        int ID = vid.getId();
        bundle.putInt("123", ID);
        NavHostFragment.findNavController(context).navigate(R.id.action_vidsByCat_to_nav_myVideoView, bundle);
        // show selected Video
    }

}