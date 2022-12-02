package de.unituebingen.streamapp.register;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import de.unituebingen.streamapp.tools.RESTRequest;
import de.unituebingen.streamapp.tools.User;

public class RegisterViewModel extends ViewModel {

    // LiveDate for Register Request
    private MutableLiveData<User> registerUser;

    // Getter for Register Data
    public MutableLiveData<User> getRegisterUser(String realName,
                                                 String userName,
                                                 String email,
                                                 String password) {
        // Init if null
        if (registerUser == null) {
            registerUser = new MutableLiveData<User>();
        }

        RESTRequest rr = new RESTRequest();

        try {
            registerUser.setValue(rr.register(userName, password, realName, email));
            Log.println(Log.DEBUG, "Registration", userName + "\t" + realName);
        } catch(Exception e) {
            registerUser.setValue(null);
            Log.println(Log.DEBUG, "Registration failed", userName + "\t" + realName);
        }

        return registerUser;
    }
}