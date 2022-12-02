package de.unituebingen.streamapp.categoryManagement.VideosFromCat;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import de.unituebingen.streamapp.tools.RESTRequest;
import de.unituebingen.streamapp.tools.Video;

public class VidsByCatViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Video>> categoriesDataVids;
    private MutableLiveData<ArrayList<Video>> categorie;

    MutableLiveData<ArrayList<Video>> getCategoriesVids(int count, int id) {

        // init if null
        if (categoriesDataVids == null) {
            categoriesDataVids = new MutableLiveData<>();
            //  return categoriesData;
        }

        // RESTRequest instance
        RESTRequest rr = new RESTRequest();

        // do request
        try {
            categoriesDataVids.setValue(rr.getVideosByCategory(20, count, id));
        } catch (Exception e) {
            categoriesDataVids.setValue(null);
        }

        // return Categories
        return categoriesDataVids;
    }

    MutableLiveData<ArrayList<Video>> getCategory(int id) {

        // init if null
        if (categorie == null) {
            categorie = new MutableLiveData<>();
            //  return categoriesData;
        }

        // RESTRequest instance
        RESTRequest rr = new RESTRequest();

        // do request
        try {
            categorie.setValue(rr.getVideosByCategory(id));
        } catch (Exception e) {
            categorie.setValue(null);
        }

        // return Categories
        return categorie;
    }
}