package de.unituebingen.streamapp.categoryManagement;

import android.app.Activity;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import de.unituebingen.streamapp.UserData;
import de.unituebingen.streamapp.tools.RESTRequest;
import de.unituebingen.streamapp.tools.VideoCategory;

public class CategoriesViewModel extends ViewModel {

    // Livedata for Categories
    private MutableLiveData<ArrayList<VideoCategory>> categoriesData;

    MutableLiveData<ArrayList<VideoCategory>> getCategoriesData(int count) {

        // init if null
        if (categoriesData == null) {
            categoriesData = new MutableLiveData<>();
            //  return categoriesData;
        }

        // RESTRequest instance
        RESTRequest rr = new RESTRequest();

        // do request
        try {
            categoriesData.setValue(rr.getVideoCategories(20, count));
        } catch (Exception e) {
            categoriesData.setValue(null);
        }

        // return Categories
        return categoriesData;
    }

    public boolean updateCategoriesData(String name, Activity activity) {

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
            rr.createVideoCategory(name);
        } catch (Exception e) {
            Log.e("Error, submit category not working", e.getMessage());
        }
        return true;
    }

    public boolean deleteCategoriesData(int id, Activity activity) {

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
            rr.deleteVideoCategoryById(id);
        } catch (Exception e) {
            Log.e("Error, delete category not working", e.getMessage());
        }
        return true;
    }
}