package de.unituebingen.streamapp.Videos;


import android.app.Activity;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import de.unituebingen.streamapp.UserData;
import de.unituebingen.streamapp.tools.RESTRequest;
import de.unituebingen.streamapp.tools.Video;

public class VideosViewModel extends ViewModel {

    // Livedata for Videos
    private MutableLiveData<ArrayList<Video>> VideosData;

    MutableLiveData<ArrayList<Video>> getVideosData(int count) {

        // init if null
        if (VideosData == null) {
            VideosData = new MutableLiveData<>();
        }

        // RESTRequest instance
        RESTRequest rr = new RESTRequest();
        // do request
        try {
            VideosData.setValue(rr.getVideos(20, count));
        } catch (Exception e) {
            VideosData.setValue(null);
        }
        // return Categories
        return VideosData;
    }


    public boolean deleteVid(int VideoID, Activity activity) {

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
            rr.deleteVideoById(VideoID);
        } catch (Exception e) {
            Log.e("Error, delete Video not working", e.getMessage());
        }
        return true;

    }


}