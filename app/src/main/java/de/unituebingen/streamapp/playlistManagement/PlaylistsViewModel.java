package de.unituebingen.streamapp.playlistManagement;

import android.app.Activity;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import de.unituebingen.streamapp.UserData;
import de.unituebingen.streamapp.tools.Playlist;
import de.unituebingen.streamapp.tools.RESTRequest;


public class PlaylistsViewModel extends ViewModel {

    // Livedata for Playlists
    private MutableLiveData<ArrayList<Playlist>> playlistData;

    MutableLiveData<ArrayList<Playlist>> getPublicPlaylistData(int count) {

        // init if null
        if (playlistData == null) {
            playlistData = new MutableLiveData<>();
            //  return categoriesData;
        }

        // RESTRequest instance
        RESTRequest rr = new RESTRequest();
        // do request (BEST TO DO THIS NOT ON THE MAIN THREAD!)
        try {
            playlistData.setValue(rr.getPublicPlaylists(20, count));
        } catch (Exception e) {
            playlistData.setValue(null);
        }

        // return Videos
        return playlistData;

    }

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


    public Boolean updatePlaylist(String name, Activity activity) {

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

        Playlist newP = new Playlist();


        // do request
        try {
            rr.createPlaylist(ud.getUserId(), newP);
        } catch (Exception e) {
            Log.e("Error, create Pl working", e.getMessage());
        }
        return true;
    }

    public Boolean deletePlaylist(int ID_User, int ID_pl, Activity activity) {

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
            rr.deletePlaylistByID(ID_User, ID_pl);
        } catch (Exception e) {
            Log.e("Error, deleting Playlist not working", e.getMessage());
        }
        return true;
    }

    public Boolean subscribe(int ID_User, int ID_pl, Activity activity) {

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
            rr.followPlaylistById(ID_User, ID_pl);
        } catch (Exception e) {
            Log.e("Error, deleting Playlist not working", e.getMessage());
        }
        return true;

    }
}
