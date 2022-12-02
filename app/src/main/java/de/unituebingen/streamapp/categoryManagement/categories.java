package de.unituebingen.streamapp.categoryManagement;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import de.unituebingen.streamapp.R;
import de.unituebingen.streamapp.UserData;
import de.unituebingen.streamapp.tools.VideoCategory;

import java.util.ArrayList;


public class categories extends Fragment implements CatAdapter.OnCatListener {

    private static final String TAG = "degub msg";
    private EditText new_category_name;
    private EditText deleteCat_name;
    private Button addCat_bt;
    private Button deleteCat_bt;
    private SwipeRefreshLayout refreshCat;
    private RecyclerView rvListCat;
    ArrayList<String> listCat = new ArrayList<String>();
    Fragment context = this;
    int globalCount = 0;
    private CategoriesViewModel mViewModel;

    public static de.unituebingen.streamapp.categoryManagement.categories newInstance() {
        return new categories();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.categories_fragment, container, false);

        new_category_name = (EditText) view.findViewById(R.id.catName);
        deleteCat_name = (EditText) view.findViewById(R.id.deleteCat);
        addCat_bt = view.findViewById(R.id.addCat_bt);
        deleteCat_bt = view.findViewById(R.id.deleteCat_bt);
        refreshCat = view.findViewById(R.id.refreshVids);
        rvListCat = view.findViewById(R.id.recyclerViewCat);
        CatAdapter myAdapter = new CatAdapter(getActivity(), listCat, this::onCatClick);
        rvListCat.setAdapter(myAdapter);
        rvListCat.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CategoriesViewModel.class);
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
                    CatAdapter catAdapter = new CatAdapter(getActivity(), listCat, categories.this::onCatClick);
                    catAdapter.notifyDataSetChanged();
                    rvListCat.setAdapter((catAdapter));
                    Log.d(TAG, "onChanged: " + listCat);
                } else {
                    Toast.makeText(context.getActivity().getApplicationContext(), "no categories available", Toast.LENGTH_SHORT).show();
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


        //add button
        addCat_bt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addCat();
            }
        });

        //delete button
        deleteCat_bt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                deleteCat();
            }
        });

        refreshCat.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                // do after 1 sec
                                refreshCat.setRefreshing(false);
                            }
                        }, 1000); // for satisfaction

                    }
                }
        );
    }


    //add a category
    private void addCat() {
        String new_category_name_str = new_category_name.getText().toString();
        if (listCat.contains(new_category_name_str)) {
            new_category_name.setError("This category already exist. Choose another name!");
        } else {

            //add to list
            listCat.add(new_category_name_str);

            //add to server
            CatAdapter catAdapter = new CatAdapter(getActivity(), listCat, this::onCatClick);
            catAdapter.notifyDataSetChanged();

            mViewModel.updateCategoriesData(new_category_name_str, getActivity());
        }
    }


    //delete a category
    private void deleteCat() {
        UserData ud = new UserData(getActivity());
        String auth = ud.getAuthentication();
        int a = ud.getTypeId();

        Log.d(TAG, "deleteCat: " + a);
        String delete_category_str = deleteCat_name.getText().toString();
        ArrayList<VideoCategory> videoList = mViewModel.getCategoriesData(globalCount).getValue();
        ArrayList<String> videoListStr = new ArrayList<>();
        if (a > 3) {
            for (VideoCategory cat : videoList) {
                videoListStr.add(cat.getName());
            }
            int position = videoListStr.indexOf(delete_category_str);
            //remove from list
            listCat.remove(delete_category_str);
            VideoCategory cat = videoList.get(position);
            int ID = cat.getId();
            Log.d(TAG, "deleteCat: " + position + "  " + ID);
            mViewModel.deleteCategoriesData(ID, getActivity());
        } else {
            Toast.makeText(context.getActivity().getApplicationContext(), "You don't have permission to delete that", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onCatClick(int position) {
        ArrayList<VideoCategory> videoList = mViewModel.getCategoriesData(globalCount).getValue();
        Bundle bundle = new Bundle();
        VideoCategory cat = videoList.get(position);
        int ID = cat.getId();
        Log.d(TAG, "onCatClick: " + ID);
        bundle.putInt("1234567", ID);
        NavHostFragment.findNavController(context).navigate(R.id.action_nav_categories_to_vidsByCat, bundle);
        //show all Videos from selected category
    }
}