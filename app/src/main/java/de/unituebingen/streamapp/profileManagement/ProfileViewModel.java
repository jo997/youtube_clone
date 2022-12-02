package de.unituebingen.streamapp.profileManagement;

import android.app.Activity;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import de.unituebingen.streamapp.UserData;
import de.unituebingen.streamapp.tools.RESTRequest;
import de.unituebingen.streamapp.tools.User;

public class ProfileViewModel extends ViewModel {

    // Livedata for profile Request
    private MutableLiveData<User> profileData;

    // Livedata for profile edit
    private MutableLiveData<Boolean> updateUser;


    // Getter for profile data
    public MutableLiveData<User> getProfileData(Activity activity) {

        // get user manager
        UserData ud = new UserData(activity);

        // init if null
        if (profileData == null) {
            profileData = new MutableLiveData<>();
        }

        // get apikey
        String apikey = ud.getAuthentication();

        // check if apikey is valid
        if (apikey == null) {
            profileData.setValue(null);
            return profileData;
        }

        // RESTRequest instance
        RESTRequest rr = new RESTRequest(apikey);

        // do request
        try {
            profileData.setValue(rr.getCurrentUser());
        } catch (Exception e) {
            profileData.setValue(null);
        }

        // return profile data
        return profileData;
    }


    //Getter for update user
    public MutableLiveData<Boolean> getUpdateUser(String realname,
                                                  String mail,
                                                  String password, Activity activity) {

        // get user manager
        UserData ud = new UserData(activity);

        // init if null
        if (updateUser == null) {
            updateUser = new MutableLiveData<>();
        }

        // get apikey
        String apikey = ud.getAuthentication();

        // check if apikey is valid
        if (apikey == null) {
            Log.e("Error", "apikey null");
            updateUser.setValue(false);
            return updateUser;
        }

        // RESTRequest instance
        RESTRequest rr = new RESTRequest(apikey);

        // do request
        try {
            updateUser.setValue(rr.updateUser(ud.getUserId(), realname, mail, password));
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            updateUser.setValue(false);
        }

        // return profile data
        return updateUser;
    }
}