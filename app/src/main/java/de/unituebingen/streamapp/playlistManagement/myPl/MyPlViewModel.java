package de.unituebingen.streamapp.playlistManagement.myPl;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import de.unituebingen.streamapp.tools.Playlist;
import de.unituebingen.streamapp.tools.RESTRequest;

public class MyPlViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Playlist>> playlistData;

    MutableLiveData<ArrayList<Playlist>> getPlaylistData(int ud) {

        // init if null
        if (playlistData == null) {
            playlistData = new MutableLiveData<>();
            //  return categoriesData;
        }


        // RESTRequest instance
        RESTRequest rr = new RESTRequest();
        // do request
        try {
            playlistData.setValue(rr.getUserPlaylists(ud));
        } catch (Exception e) {
            playlistData.setValue(null);
        }

        // return Videos
        return playlistData;

    }
}