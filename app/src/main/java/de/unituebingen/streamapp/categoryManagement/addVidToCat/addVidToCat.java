package de.unituebingen.streamapp.categoryManagement.addVidToCat;

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
import de.unituebingen.streamapp.UserData;
import de.unituebingen.streamapp.tools.VideoCategory;

public class addVidToCat extends Fragment implements addVidToCatAdapter.WhichCatListener {

    private AddVidToCatViewModel mViewModel;
    private static final String TAG = "degub msg";
    private RecyclerView rvListCat;
    ArrayList<String> listCat = new ArrayList<String>();
    Fragment context = this;
    int globalCount = 0;

    public static addVidToCat newInstance() {
        return new addVidToCat();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_vid_to_cat_fragment, container, false);
        rvListCat = view.findViewById(R.id.rvAddToCat);
        addVidToCatAdapter myAdapter = new addVidToCatAdapter(getActivity(), listCat, this::onWCatClick);
        rvListCat.setAdapter(myAdapter);
        rvListCat.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AddVidToCatViewModel.class);
        Fragment context = this;
        // set observer
        Observer<ArrayList<VideoCategory>> CatObserver = new Observer<ArrayList<VideoCategory>>() {
            int count = 20;

            @Override
            public void onChanged(ArrayList<VideoCategory> videoCategories) {
                ArrayList<String> listCat = new ArrayList<>();
                if (videoCategories != null) {
                    for (VideoCategory list : videoCategories)
                        listCat.add(list.getName());
                    addVidToCatAdapter catAdapter = new addVidToCatAdapter(getActivity(), listCat, addVidToCat.this::onWCatClick);
                    catAdapter.notifyDataSetChanged();
                    rvListCat.setAdapter((catAdapter));
                    Log.d(TAG, "onChanged: " + listCat);
                }
                // load more Videos at the end of RecyclerList and update rvlist
                LinearLayoutManager layoutManager = ((LinearLayoutManager) rvListCat.getLayoutManager());
                rvListCat.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        int lastPos = layoutManager.findLastVisibleItemPosition();
                        //load new videos
                        if (lastPos == 19) {
                            count += 10;
                            mViewModel.getCategoriesData(count);
                            layoutManager.scrollToPosition(1);
                            globalCount = count;
                        }
                        // load old videos
                        if (layoutManager.findFirstCompletelyVisibleItemPosition() == 0 && count != 0) {
                            count -= 10;
                            mViewModel.getCategoriesData(count);
                            layoutManager.scrollToPosition(2);
                            globalCount = count;
                        }
                    }
                });

            }
        };

        // make observer observe the data
        mViewModel.getCategoriesData(globalCount).observe(context.getActivity(), CatObserver);

    }

    @Override
    public void onWCatClick(int position) {
        // select a category to add Video

        UserData ud = new UserData(getActivity());
        String auth = ud.getAuthentication();
        int a = ud.getTypeId();

        int VidID = getArguments().getInt("123");
        ArrayList<VideoCategory> list = mViewModel.getCategoriesData(globalCount).getValue();
        VideoCategory cat = list.get(position);
        int CatID = cat.getId();
        if (a > 3) {
            if (mViewModel.addVidToCat(CatID, VidID, getActivity())) {
                Log.d(TAG, "onWCatClick: " + "Cat ID:" + CatID + "VidID" + VidID);
                NavHostFragment.findNavController(context).navigate(R.id.action_addVidToCat_to_nav_videos);
            } else {
                Toast.makeText(context.getActivity().getApplicationContext(), "something went wrong!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context.getActivity().getApplicationContext(), "You don't have permission to edit this category", Toast.LENGTH_SHORT).show();
            NavHostFragment.findNavController(context).navigate(R.id.action_addVidToCat_to_nav_videos);
            // go back to videos
        }


    }
}