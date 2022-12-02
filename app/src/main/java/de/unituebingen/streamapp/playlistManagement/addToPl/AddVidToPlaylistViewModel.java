package de.unituebingen.streamapp.playlistManagement.addToPl;

import android.app.Activity;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import de.unituebingen.streamapp.UserData;
import de.unituebingen.streamapp.tools.RESTRequest;
import de.unituebingen.streamapp.tools.Video;

public class AddVidToPlaylistViewModel extends ViewModel {

    // Livedata for Playlists
    private MutableLiveData<ArrayList<Video>> playlistData;

    MutableLiveData<ArrayList<Video>> getPlaylistData() {

        // init if null
        if (playlistData == null) {
            playlistData = new MutableLiveData<>();
            //  return categoriesData;
        }

        // RESTRequest instance
        RESTRequest rr = new RESTRequest();

        // do request
        try {
            playlistData.setValue(rr.getVideos());
        } catch (Exception e) {
            playlistData.setValue(null);
        }

        // return Videos
        return playlistData;
    }


    private MutableLiveData<Boolean> updatePlaylist;

    public Boolean updatePlaylist(int PlaylistID, int VideoID, Activity activity) {

        // get user manager
        UserData ud = new UserData(activity);
        // get apikey
        String apikey = ud.getAuthentication();
        int me = ud.getUserId();

        // check if apikey is valid
        if (apikey == null) {
            Log.e("Error", "apikey null");
            return false;
        }

        // RESTRequest instance
        RESTRequest rr = new RESTRequest(apikey);

        // do request
        try {
            rr.addVideoToPlaylist(me, PlaylistID, VideoID);
        } catch (Exception e) {
            Log.e("Error, add to playlist not working", e.getMessage());
        }
        return true;

    }


}