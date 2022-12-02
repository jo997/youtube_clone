package de.unituebingen.streamapp.VideoUpload;


import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.File;

import de.unituebingen.streamapp.tools.RESTRequest;
import de.unituebingen.streamapp.tools.Video;

public class VideoUploadViewModel extends ViewModel {

    // LiveData for REST Request
    private MutableLiveData<Video> videoData;

    /**
     * Upload local Video to Streaming Plattform
     * @param apikey ApiKey for Authentication
     * @param title title of Video
     * @param description Description of Video
     * @param file Path object of local file
     * @return Video object of serverside information ie. videoID
     */
    public MutableLiveData<Video> uploadVideo(
            String apikey,
            String title, String description, File file) {

        // Init if null
        if (videoData == null) {
            videoData = new MutableLiveData<>();
        }

        // RESTRequest instance
        RESTRequest rr = new RESTRequest(apikey);

        // Do Request (TODO: move away from Main Thread
        try {
            Log.d("VideoUpload", "title:\t" + title
                    + "\ndesc:\t" + description
                    + "\nfile:\t" + file.toString()
                    + "\nfileLength:\t" + file.length());
            Video videoInfo = rr.uploadVideo(file, title, description);
            videoData.setValue(videoInfo);
        } catch (Exception e) {
            Log.e("VideoUpload", e.getMessage());
            Log.e("VideoUpload", "");
            videoData.setValue(null);
        }

        // Return Video Data
        return videoData;
    }


}
