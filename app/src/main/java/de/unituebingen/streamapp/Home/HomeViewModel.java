package de.unituebingen.streamapp.Home;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import de.unituebingen.streamapp.tools.RESTRequest;
import de.unituebingen.streamapp.tools.Video;


public class HomeViewModel extends ViewModel {

    // Livedata for most popular Videos
    private MutableLiveData<ArrayList<Video>> VideosDataHome;

    MutableLiveData<ArrayList<Video>> getVideosDataHome(int count) {

        // init if null
        if (VideosDataHome == null) {
            VideosDataHome = new MutableLiveData<>();
            //  return VideosData;
        }

        // RESTRequest instance
        RESTRequest rr = new RESTRequest();

        // do request
        try {
            VideosDataHome.setValue(rr.getPopularVideos(20, count));
        } catch (Exception e) {
            VideosDataHome.setValue(null);
        }

        // return Categories
        return VideosDataHome;
    }

}