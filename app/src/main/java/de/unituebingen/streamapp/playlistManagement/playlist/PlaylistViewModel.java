package de.unituebingen.streamapp.playlistManagement.playlist;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import de.unituebingen.streamapp.tools.RESTRequest;
import de.unituebingen.streamapp.tools.Video;

public class PlaylistViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Video>> playlistData;

    MutableLiveData<ArrayList<Video>> getPlaylistData(int id, int count) {

        // init if null
        if (playlistData == null) {
            playlistData = new MutableLiveData<>();
            //  return categoriesData;
        }

        // RESTRequest instance
        RESTRequest rr = new RESTRequest();
        // do request
        try {
            playlistData.setValue(rr.getVideosOfPlaylistById(id, 20, count));
        } catch (Exception e) {
            playlistData.setValue(null);
        }

        // return Videos
        return playlistData;
    }

    void VideoDown(int ID_Vid, int ID_Pl) {

        // RESTRequest instance
        RESTRequest rr = new RESTRequest();
        // do request
        try {
            // rr.changeSequenceDown(VID_ID,PL_ID);
        } catch (Exception e) {
            playlistData.setValue(null);
        }

    }

    void VideoUp(int ID_Vid, int ID_Pl) {

        // RESTRequest instance
        RESTRequest rr = new RESTRequest();
        // do request
        try {
            // rr.changeSequenceUP(VID_ID,PL_ID);
        } catch (Exception e) {
            playlistData.setValue(null);
        }

    }

}
