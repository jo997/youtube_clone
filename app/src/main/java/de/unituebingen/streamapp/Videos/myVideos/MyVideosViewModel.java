package de.unituebingen.streamapp.Videos.myVideos;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import de.unituebingen.streamapp.tools.RESTRequest;
import de.unituebingen.streamapp.tools.Video;

public class MyVideosViewModel extends ViewModel {
    // Livedata for Videos
    private MutableLiveData<ArrayList<Video>> MyVideosData;

    MutableLiveData<ArrayList<Video>> getMyVideosData(int count) {

        // init if null
        if (MyVideosData == null) {
            MyVideosData = new MutableLiveData<>();
            //  return VideosData;
        }

        // RESTRequest instance
        RESTRequest rr = new RESTRequest();

        // do request
        try {
            MyVideosData.setValue(rr.getPopularVideos(20, count));   //still need to load myVideos not popular Videos
        } catch (Exception e) {
            MyVideosData.setValue(null);
        }

        // return Categories
        return MyVideosData;
    }
}