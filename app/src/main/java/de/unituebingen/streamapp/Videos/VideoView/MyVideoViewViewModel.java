package de.unituebingen.streamapp.Videos.VideoView;

import android.app.Activity;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import de.unituebingen.streamapp.UserData;
import de.unituebingen.streamapp.tools.RESTRequest;
import de.unituebingen.streamapp.tools.Video;

public class MyVideoViewViewModel extends ViewModel {

    private static final String TAG = "Tag";
    // Livedata for Videos
    private MutableLiveData<ArrayList<Video>> VideosData;
    // Livedata for Comments change
    private MutableLiveData<Boolean> updateComments;
    // LiveData for Video
    private MutableLiveData<Video> Video;

    MutableLiveData<ArrayList<Video>> getVideosData() {

        // init if null
        if (VideosData == null) {
            VideosData = new MutableLiveData<>();
            //  return VideosData;
        }

        // RESTRequest instance
        RESTRequest rr = new RESTRequest();

        // do request (BEST TO DO THIS NOT ON THE MAIN THREAD!)
        try {
            VideosData.setValue(rr.getVideos());
        } catch (Exception e) {
            VideosData.setValue(null);
        }

        // return Categories
        return VideosData;
    }

    MutableLiveData<Video> getVideo(int ID) {

        // init if null
        if (Video == null) {
            Video = new MutableLiveData<Video>();
            //  return VideosData;
        }

        // RESTRequest instance
        RESTRequest rr = new RESTRequest();

        // do request (BEST TO DO THIS NOT ON THE MAIN THREAD!)
        try {
            Video.setValue(rr.getVideoById(ID));   //Request still missing
        } catch (Exception e) {
            Video.setValue(null);
        }

        // return Categories
        return Video;

    }

    public Boolean updateRating(double rating, int VideoID, Activity activity) {

        // get user manager
        UserData ud = new UserData(activity);
        // get apikey
        String apikey = ud.getAuthentication();

        // check if apikey is valid
        if (apikey == null) {
            Log.e("Error", "apikey null");
            return false;
        }

        // RESTRequest instance
        RESTRequest rr = new RESTRequest(apikey);
        // do request
        // still need to save Rating ID
        try {
            rr.createRating(VideoID, rating);
        } catch (Exception e) {
            Log.e("Error, submit rating not working", e.getMessage());
        }
        return true;

    }


    public boolean updateComments(String comment, int VideoID, Activity activity) {

        // get user manager
        UserData ud = new UserData(activity);
        // get apikey
        String apikey = ud.getAuthentication();

        // check if apikey is valid
        if (apikey == null) {
            Log.e("Error", "apikey null");
            return false;
        }

        // RESTRequest instance
        RESTRequest rr = new RESTRequest(apikey);

        // do request
        try {
            rr.createComment(VideoID, comment);
        } catch (Exception e) {
            Log.e("Error, submit comment not working", e.getMessage());
        }
        return true;

    }

    public Boolean deleteComment(int commentID, int VideoID, Activity activity) {

        // get user manager
        UserData ud = new UserData(activity);
        // get apikey
        String apikey = ud.getAuthentication();

        // check if apikey is valid
        if (apikey == null) {
            Log.e("Error", "apikey null");
            return false;
        }

        // check for admin/own comments still missing

        // RESTRequest instance
        RESTRequest rr = new RESTRequest(apikey);

        // do request
        try {
            rr.deleteComment(VideoID, commentID);
        } catch (Exception e) {
            Log.e("Error, deleting comment not working", e.getMessage());
        }
        return true;

    }

}