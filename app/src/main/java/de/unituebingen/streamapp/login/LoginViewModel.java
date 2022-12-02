package de.unituebingen.streamapp.login;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import de.unituebingen.streamapp.tools.RESTRequest;
import de.unituebingen.streamapp.tools.User;

public class LoginViewModel extends ViewModel {


    // Livedata for Login Request
    private MutableLiveData<String> loginData;
    private MutableLiveData<User> loginUserMe;

    // Getter for login data
    public MutableLiveData<String> getLoginData(String username, String password) {

        // init if null
        if (loginData == null) {
            loginData = new MutableLiveData<>();
            return loginData;
        }

        // RESTRequest instance
        RESTRequest rr = new RESTRequest();

        // do request
        try {
            String auth = rr.login(username, password);
            loginData.setValue(auth);
        } catch (Exception e) {
            loginData.setValue(null);
        }

        // return login data
        return loginData;
    }

    // Getter for current User
    public MutableLiveData<User> getMe(String apikey) {

        // init if null
        if (loginUserMe == null) {
            loginUserMe = new MutableLiveData<>();
        }

        // RESTRequest instance
        RESTRequest rr = new RESTRequest(apikey);

        // do request
        try {
            User me = rr.getCurrentUser();
            loginUserMe.setValue(me);
        } catch (Exception e) {
            loginUserMe.setValue(null);
        }

        // return login data
        return loginUserMe;
    }

}