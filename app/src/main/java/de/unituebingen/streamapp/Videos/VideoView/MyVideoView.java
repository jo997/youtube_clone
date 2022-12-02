package de.unituebingen.streamapp.Videos.VideoView;


import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
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
import android.view.View.OnSystemUiVisibilityChangeListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.ArrayList;

import de.unituebingen.streamapp.R;
import de.unituebingen.streamapp.UserData;
import de.unituebingen.streamapp.tools.Comment;
import de.unituebingen.streamapp.tools.Video;

public class MyVideoView extends Fragment {

    Fragment context = this;

    private VideoView vid;
    private Button Play_btn;
    private Button fullscreen_btn;
    private Button Add_Comment_btn;
    private TextView description_text;
    private TextView description_Vid;
    private TextView videoViews;
    private TextView uploadDate_txt;
    private TextView uploadName;
    private EditText comment_txt;
    private EditText rating_txt;
    private Button Add_Rating_btn;
    private static final String TAG = "debug msg";
    private MyVideoViewViewModel mViewModel;
    private ArrayList<Video> videoList = new ArrayList<>();
    private RecyclerView rvList;
    private RatingBar stars;


    public static MyVideoView newInstance() {
        return new MyVideoView();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.myvideo_view_fragment, container, false);

        vid = view.findViewById(R.id.videoView);
        Play_btn = view.findViewById(R.id.play_btn);
        fullscreen_btn = view.findViewById(R.id.fullscreen_bt);
        Add_Comment_btn = view.findViewById(R.id.add_comment_btn);
        rvList = view.findViewById(R.id.rvComments);
        rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        description_text = view.findViewById(R.id.vid_description);
        uploadName = view.findViewById(R.id.uploadName_txt);
        uploadDate_txt = view.findViewById(R.id.uploadDateText);
        description_Vid = view.findViewById(R.id.textViewDescription);
        videoViews = view.findViewById(R.id.textVideoViews);
        comment_txt = view.findViewById(R.id.addComment);
        rating_txt = view.findViewById(R.id.yourRating);
        Add_Rating_btn = view.findViewById(R.id.rating_bt);
        stars = view.findViewById(R.id.ratingBar);
        stars.setNumStars(5);
        return view;

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MyVideoViewViewModel.class);


        // get context
        Fragment context = this;
        int ID = getArguments().getInt("123");
        Video videoToPlay = mViewModel.getVideo(ID).getValue();
        String description = videoToPlay.getDescription();
        Log.d(TAG, "onActivityCreated:" + description);


        // set observer
        Observer<ArrayList<Video>> Observer = new Observer<ArrayList<Video>>() {
            @Override
            public void onChanged(ArrayList<Video> videoList) {
                //lists for comments, author and time stamp
                ArrayList<String> cList = new ArrayList<>();
                ArrayList<String> aList = new ArrayList<>();
                ArrayList<String> tList = new ArrayList<>();
                int ID = getArguments().getInt("123");

                //load LiveData
                Video video = mViewModel.getVideo(ID).getValue();
                ArrayList<Comment> coList = video.getComments().getItems();
                for (Comment comment : coList) {
                    cList.add(comment.getComment());
                }
                for (Comment comment : coList) {
                    aList.add(comment.getUsername());
                }
                for (Comment comment : coList) {
                    tList.add(comment.getTimestamp().toString());
                }
                CommentAdapter cAdapter = new CommentAdapter(getActivity(), cList, aList, tList);
                cAdapter.notifyDataSetChanged();
                rvList.setAdapter((cAdapter));

                Log.d(TAG, "onChanged: " + cList);

                // set Layout manager
                rvList.setLayoutManager(new LinearLayoutManager(getContext()));

                // set item touch helper to enable swiping
                new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                    UserData ud = new UserData(getActivity());
                    String auth = ud.getAuthentication();

                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                        Comment commentToDel = coList.get(viewHolder.getAdapterPosition());
                        int ID_Comm = commentToDel.getId();
                        int UserID = commentToDel.getUserId();
                        UserData ud = new UserData(getActivity());
                        int a = ud.getTypeId();
                        int me = ud.getUserId();

                        if (TextUtils.isEmpty(auth)) {
                            Toast.makeText(context.getActivity().getApplicationContext(), "You need to login to edit comments", Toast.LENGTH_SHORT).show();
                            cAdapter.notifyDataSetChanged();
                        } else if (a > 3 || me == UserID) {
                            int ID_Vid = getArguments().getInt("123");
                            cList.remove(viewHolder.getAdapterPosition());
                            mViewModel.deleteComment(ID_Comm, ID_Vid, getActivity());
                            // update Adapter
                            cAdapter.notifyDataSetChanged();
                            Log.d(TAG, "onSwipedComment: " + "Comm ID:" + ID_Comm + "Vid ID:" + ID_Vid);
                        } else {
                            Toast.makeText(context.getActivity().getApplicationContext(), "You don't have the permissions to edit this comment", Toast.LENGTH_SHORT).show();
                            cAdapter.notifyDataSetChanged();
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
                            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                        }
                    }
                }).attachToRecyclerView(rvList);

            }

        };


        // make observer observe the data
        mViewModel.getVideosData().observe(context.getActivity(), Observer);

        //add rating
        Add_Rating_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitRating();
            }
        });

        // add comment
        Add_Comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitComment();

            }
        });


        // plays video
        Play_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                playVideo(v);
            }
        });

        fullscreen_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                Video video = mViewModel.getVideo(ID).getValue();
                int ID = video.getId();
                bundle.putInt("123", ID);
                NavHostFragment.findNavController(context).navigate(R.id.action_nav_myVideoView_to_fullscreenFragment, bundle);
            }
        });

        // set video Title
        description_text.setText(videoToPlay.getTitle());
        // set video description
        description_Vid.setText(videoToPlay.getDescription());
        // set video views
        String views = String.valueOf(videoToPlay.getViews());
        videoViews.setText(views);
        // set video upload
        String upload = String.valueOf(videoToPlay.getUploadDate());
        uploadDate_txt.setText(upload);
        // set uploadName
        uploadName.setText(videoToPlay.getUploaderName());


        // set video rating
        double numstars;
        numstars = videoToPlay.getRating();
        if (numstars == 0) {
        } else {
            stars.setRating((float) numstars);
        }

        stars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String rating = String.valueOf(stars.getRating());
                Toast.makeText(getContext(), rating, Toast.LENGTH_LONG).show();
            }

        });

    }

    // check if rating
    public void submitRating() {
        String rating = rating_txt.getText().toString();
        UserData ud = new UserData(getActivity());
        int a = ud.getTypeId();
        if (a > 1) {
            if (rating.length() == 0) {
                Toast.makeText(context.getActivity().getApplicationContext(), "Please enter a rating!", Toast.LENGTH_SHORT).show();
            } else {
                double final_rating = Double.parseDouble(rating);
                if (final_rating > 5) {
                    Toast.makeText(context.getActivity().getApplicationContext(), "Please enter a rating from 0 to 5!", Toast.LENGTH_SHORT).show();
                }
                if (final_rating < 5) {
                    //get VideoID
                    int ID = getArguments().getInt("123");
                    //load LiveData
                    Video video = mViewModel.getVideo(ID).getValue();
                    if (mViewModel.updateRating(final_rating, ID, getActivity())) {
                        Toast.makeText(context.getActivity().getApplicationContext(), "Your rating has been saved", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context.getActivity().getApplicationContext(), "Please login to rate a video", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } else {
            Toast.makeText(context.getActivity().getApplicationContext(), "Please login to rate a video", Toast.LENGTH_SHORT).show();
        }
    }

    public void submitComment() {
        UserData ud = new UserData(getActivity());
        int a = ud.getTypeId();
        if (a > 1) {
            String comment = comment_txt.getText().toString();
            if (comment.length() == 0) {
                Toast.makeText(context.getActivity().getApplicationContext(), "Please enter a comment!", Toast.LENGTH_SHORT).show();
            } else {
                //get VideoID
                int ID = getArguments().getInt("123");
                //load LiveData
                Video video = mViewModel.getVideo(ID).getValue();
                mViewModel.updateComments(comment, ID, getActivity());
                Toast.makeText(context.getActivity().getApplicationContext(), "Your comment has been saved", Toast.LENGTH_SHORT).show();

            }
        } else {
            Toast.makeText(context.getActivity().getApplicationContext(), "Please login to write a comment", Toast.LENGTH_SHORT).show();
        }
    }


    public void playVideo(View v) {

        // load video List at get URI from selected video at position pos
        MediaController m = new MediaController(getActivity());
        vid.setMediaController(m);

        int ID = getArguments().getInt("123");
        //load LiveData
        Video videoToPlay = mViewModel.getVideo(ID).getValue();
        int bestQ = videoToPlay.getMaxVideoQuality();
        // get all available uris
        ArrayList<Video.Quality> allUris = videoToPlay.getQualities();
        allUris = videoToPlay.getQualities();

        // get uri for video with best quality
        Video.Quality a = allUris.get(bestQ - 1);
        String uri = a.getUri();

        MediaController m2 = new FullScreenMediaController(getContext());

        vid.setMediaController(m2);

        m2.requestFocus();
        m2.setAnchorView(vid);
        // start the video
        String path = uri;
        Uri u = Uri.parse(path);
        vid.setVideoURI(u);
        vid.start();

    }

}