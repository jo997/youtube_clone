package de.unituebingen.streamapp.tools;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

import de.unituebingen.streamapp.tools.wrapper.SearchWrapper;
import de.unituebingen.streamapp.tools.wrapper.Wrapper;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.fastjson.FastJsonConverterFactory;
import de.unituebingen.streamapp.tools.requestBodies.*;

public class RESTRequest {
    private final String API = "https://ketos.informatik.uni-tuebingen.de/api/";

    private String apikey;

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(this.API)
            .addConverterFactory(FastJsonConverterFactory.create())
            .build();

    private KetosService service = retrofit.create(KetosService.class);

    public RESTRequest() {}

    /**
     * Construct Request object using authorization
     * @param apikey Authorization
     */
    public RESTRequest(String apikey) {
        this.apikey = apikey;
    }


    /**
     * Set API key for Requests that require authorization
     * @param apikey API authorization key
     */
    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    /**
     * Register a new user
     * @param username desired username
     * @param password chosen password
     * @param realname clear name
     * @param mail email address
     * @return User data
     * @throws Exception
     */
    public User register(final String username,
                         final String password,
                         final String realname,
                         final String mail) throws Exception {
        Response<User> call = service.registerUser(new RegisterBody(username, password, realname, mail)).execute();

        if (!call.isSuccessful()) throw new Exception("Request unsuccessful");

        if (call.code() == 201){
            return call.body();
        } else {
            throw new Exception(call.errorBody().string());
        }
    }

    /**
     * Login user
     * @param username required Usernam
     * @param password required password
     * @return API Key
     * @throws Exception
     */
    public String login(final String username, final String password) throws Exception {
        Response<ApiKey> call = service.login(new LoginBody(username, password)).execute();

        if (!call.isSuccessful()) throw new Exception("Request unsuccessful");

        if (call.code() == 200) {
            this.setApikey(call.body().getApikey());
            return this.apikey;
        } else {
            throw new Exception(call.errorBody().string());
        }
    }

    /**
     * Request user data of logged in User
     * @return logged-in user
     * @throws Exception
     */
    public User getCurrentUser() throws Exception {
        Response<User> call = service.getMe(apikey).execute();

        if (!call.isSuccessful()) throw new Exception("Request unsuccessfull");

        if(call.code() == 200) {
            return call.body();
        } else {
            throw new Exception(call.errorBody().string());
        }
    }

    /**
     * Get a List of Users
     * @param limit amount of Users to receive
     * @param offset offset in Request
     * @return ArrayList of Users
     * @throws Exception No negative limit or offset
     */
    public ArrayList<User> getUsers(int limit, int offset) throws Exception {
        HashMap<String, Integer> settings = new HashMap<String, Integer>();
        if (limit > 0 )  settings.put("limit", limit);
        if (offset > 0) settings.put("offset", offset);

        if (limit < 0 || offset < 0) throw new Exception("Use positive limit and offset!");

        Response<Wrapper<User>> call = service.getUsers(this.apikey, settings).execute();

        if (call.code() == 200) {
            return call.body().getItems();
        }
        throw new Exception(call.errorBody().string());
    }

    /**
     * Get a list of Users
     * @return ArrayList of Users
     * @throws Exception Something went wrong
     */
    public ArrayList<User> getUsers() throws Exception {
        return this.getUsers(0,0);
    }

    /**
     * Get a specific User
     * @param userId ID of respected User
     * @return User
     * @throws Exception User not found
     */
    public User getUserById(int userId) throws Exception {
        Response<User> call = service.getUser(this.apikey, userId).execute();
        if (call.code() == 200) {
            return call.body();
        } else if (call.code() == 404) {
            throw new Exception("User not found");
        }
        throw new Exception(call.errorBody().string());
    }

    /**
     * Update Userdata
     * @param userId required
     * @param realname nullable
     * @param mail nullable
     * @param password nullable
     * @return State of update
     */
    public boolean updateUser(int userId,
                              String realname,
                              String mail,
                              String password) throws Exception {
        ArrayList<PatchBody> patches = new ArrayList<PatchBody>();
        if (realname != null) {
            patches.add(new PatchBody("replace", "/realname", realname));
        }

        if (mail != null) {
            patches.add(new PatchBody("replace", "/mail", mail));
        }

        if (password != null) {
            patches.add(new PatchBody("replace", "/password", password));
        }

        if (patches.isEmpty()){
            System.out.println("Nothing to update");
            return true;
        }

        try {
            Response<String> call = service.updateUser(apikey, userId, patches).execute();
            if (call.code() == 204) return true;
            else throw new Exception(call.errorBody().string());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Get a List of all Categories
     * @return ArrayList of VideoCategory
     * @throws IOException Request failed
     */
    public ArrayList<VideoCategory> getVideoCategories(int limit, int offset) throws Exception {
        HashMap<String,Integer> settings = new HashMap<String, Integer>();
        if (limit > 0) settings.put("limit", limit);
        if (offset > 0) settings.put("offset", offset);

        if (limit < 0 || offset < 0) throw new Exception("Use positive limit and offset!");

        Response<Wrapper<VideoCategory>> call = service.listCategories(settings).execute();
        return call.body().getItems();
    }

    public ArrayList<VideoCategory> getVideoCategories() throws Exception {
        return this.getVideoCategories(0,0);
    }

    /**
     * Get a specific Category
     * @param id Category id
     * @return VideoCategory
     */
    public VideoCategory getVideoCategoryById(int id) throws IOException {
        Response<VideoCategory> call = service.getCategory(id).execute();
        return call.body();
    }

    /**
     * Create a new Category
     * @param name The name of the new Category
     * @return the newly created Category
     */
    public VideoCategory createVideoCategory(String name) throws Exception {
        Response<VideoCategory> call = service.createCategory(apikey, new CategoryBody(name)).execute();
        if (call.code() == 201) return call.body();
        else if (call.code() == 409) throw new Exception("Name already in use");
        else if (call.code() == 403) throw new Exception("User is not allowed to create Categories");
        else throw new Exception(call.errorBody().string());
    }

    /**
     * Delete a video category (Admin user action only)
     * @param id To be deleted Category Id
     * @return true if Successful
     * @throws Exception Either missing permission or category not ḱnown or something completely wacky
     */
    public boolean deleteVideoCategoryById(int id) throws Exception {
        Response<String> call = service.deleteCategoryById(this.apikey,id).execute();
        if (call.code() == 204) return true;
        else throw new Exception(call.errorBody().string());
    }

    /**
     * Get a list of Videos by given Category Id
     * @param limit response limit
     * @param offset response offset
     * @param cid categoryId
     * @return ArrayList of Video
     * @throws Exception If something went wrong
     */
    public ArrayList<Video> getVideosByCategory(int limit, int offset, int cid) throws Exception{
        HashMap<String, Integer> settings = new HashMap<String, Integer>();
        if (limit > 0 )  settings.put("limit", limit);
        if (offset > 0) settings.put("offset", offset);

        if (limit < 0 || offset < 0) throw new Exception("Use positive limit and offset!");

        Response<Wrapper<Video>> call = service.getVideoByCategory(cid, settings).execute();

        if(call.code() == 200) {
            return call.body().getItems();
        } else {
            throw new Exception(call.errorBody().string());
        }

    }

    /**
     * Get a list of Videos by given CategoryId
     * @param cid category id
     * @return ArrayList of Video
     * @throws Exception If something went wrong
     */
    public ArrayList<Video> getVideosByCategory(int cid) throws Exception {
        return getVideosByCategory(0,0,cid);
    }


    /**
     * Add a video to a category
     * @param cid category Id
     * @param vid video Id
     * @return true if successfull
     * @throws Exception If video or category not found
     */
    public boolean addVideo2Category(int cid, int vid) throws Exception {
        Response<String> call = service.addVideo2Category(this.apikey, cid, new VidCatBody(vid)).execute();

        if (call.code() == 200) {
            return true;
        } else if (call.code() == 404){
            throw new Exception("Invalid Video or Category Id");
        } else {
            throw new Exception(call.errorBody().string());
        }
    }


    /**
     * Delete a Video from a Category
     * @param cid category Id
     * @param vid video Id
     * @return true if successfull
     * @throws Exception If Something went wrong
     */
    public boolean deleteVideoFromCategory(int cid, int vid) throws Exception {
        Response<String> call = service.deleteVideoFromCategory(this.apikey, cid, new VidCatBody(vid)).execute();

        if (call.code() == 200) {
            return true;
        } else if (call.code() == 404){
            throw new Exception("Invalid Video or Category Id");
        } else if (call.code() == 403) {
            throw new Exception("User can only delete its own video from a category");
        } else {
            throw new Exception(call.errorBody().string());
        }
    }

    /* ===== VIDEOS ===== */

    /**
     * Get a Video by Id
     * @param videoId Integer ID for video
     * @return Video
     * @throws Exception Video not Found
     */
    public Video getVideoById(int videoId) throws Exception{
        Response<Video> call = service.getVideo(videoId).execute();
        if (call.code() == 200) {
            return call.body();
        }
        if (call.code() == 404) throw new Exception("Video ID not found");
        throw new Exception("Something went wrong!");
    }

    /**
     * Get Videos
     * @param limit limit amount of videos, Default is 20
     * @param offset offset of respoinse, default is 0
     * @return ArrayList of Videos
     * @throws Exception for negative input
     */
    public ArrayList<Video> getVideos(final int limit, final int offset) throws Exception {
        HashMap<String, Integer> settings = new HashMap<String, Integer>();
        if (limit > 0 )  settings.put("limit", limit);
        if (offset > 0) settings.put("offset", offset);

        if (limit < 0 || offset < 0) throw new Exception("Use positive limit and offset!");

        Response<Wrapper<Video>> call = service.getVideos(settings).execute();

        if (call.code() == 200) {
            return call.body().getItems();
        }
        throw new Exception("Something went wrong!");
    }

    /**
     * Get Videos
     * @return ArrayList of Videos
     * @throws Exception Something went wrong
     */
    public ArrayList<Video> getVideos() throws Exception {
        return this.getVideos(0,0);
    }


    /* --- Video Data with stubs --- */

    // TODO
    //public boolean updateVideo(int videoId, .... Patch List ....){}

    /**
     * Delete a video
     * @param videoId ID of to delete video
     * @return true if successfull
     * @throws Exception if something went terribly wrong
     */
    public boolean deleteVideoById(int videoId) throws Exception {
        Response<String> call = service.deleteVideo(this.apikey, videoId).execute();
        return call.code() == 200;
    }


    /* ===== COMMENTS ===== */

    /**
     * Get the Comments of a video
     * @param videoId ID of the respected video
     * @param limit amount of comments
     * @param offset offset of comments
     * @return ArrayList of Comments
     * @throws Exception For negative limit or offset
     */
    public ArrayList<Comment> getCommentsOfVideoById(int videoId, int limit, int offset) throws Exception {
        HashMap<String,Integer> settings = new HashMap<String, Integer>();
        if (limit > 0) settings.put("limit", limit);
        if (offset > 0) settings.put("offset", offset);

        if (limit < 0 || offset < 0) throw new Exception("Use positive limit and offset!");

        Response<Wrapper<Comment>> call = service.getComments(videoId, settings).execute();

        if (call.code() == 200) {
            return call.body().getItems();
        } else if (call.code() == 404) throw new Exception("Video not found");
        throw new Exception(call.errorBody().string());
    }

    /**
     * Get the Comments of a Video
     * @param videoId ID of the respected video
     * @return ArrayList of Comments
     * @throws Exception something went wrong
     */
    public ArrayList<Comment> getCommentsOfVideoById(int videoId) throws Exception {
        return this.getCommentsOfVideoById(videoId, 0, 0);
    }

    /**
     * Get a specific comment of a video
     * @param videoId ID of respected video
     * @param commentId ID of the comment
     * @return Comment
     * @throws Exception Video or Comment does not exist
     */
    public Comment getCommentById(int videoId, int commentId) throws Exception {
        Response<Comment> call = service.getComment(videoId, commentId).execute();
        if (call.code() == 200){
            return call.body();
        } else if (call.code() == 404) throw new Exception("Video or Comment not found");
        throw new Exception(call.errorBody().string());
    }

    /**
     * Create a comment for a video
     * @param videoId ID of respected video
     * @param comment Comment to be created
     * @return Successfully created Comment
     * @throws Exception Something went wrong
     */
    public Comment createComment(int videoId, Comment comment) throws Exception {
        Response<Comment> call = service.createComment(this.apikey,
                videoId,
                comment.getRequestBody()).execute();
        if (call.code() == 201) {
            return call.body();
        }
        throw new Exception(call.errorBody().string());
    }

    /**
     * Create a comment for a video
     * @param videoId ID of respected video
     * @param comment Comment to be created
     * @return Successfully created comment
     * @throws Exception Something went wrong
     */
    public Comment createComment(int videoId, String comment) throws Exception{
        return this.createComment(videoId, new Comment(comment));
    }


    /**
     * Update a comment
     * @param videoId ID of respected video
     * @param commentId ID of respected comment
     * @param comment Updated Comment
     * @return true id successfull
     * @throws Exception Something went wrong
     */
    public boolean updateComment(int videoId, int commentId, Comment comment) throws Exception{
        Response<String> call = service.changeComment(
                this.apikey,
                videoId,
                commentId,
                comment.getRequestBody()).execute();
        if (call.code() == 200) {
            return true;
        } else if (call.code() == 404){
            throw new Exception("Video or Comment not found");
        }
        throw new Exception(call.errorBody().string());
    }

    /**
     * Delete a comment of a video
     * @param videoId ID of respected video
     * @param commentId ID of respected comment
     * @return true if successfull
     * @throws Exception Comment or Video not found
     */
    public boolean deleteComment(int videoId, int commentId) throws Exception{
        Response<String> call = service.deleteComment(this.apikey, videoId, commentId).execute();
        if (call.code() == 200) {
            return true;
        } else if (call.code() == 404) {
            throw new Exception("Video or Comment not found");
        }
        throw new Exception(call.errorBody().string());
    }


    /* RATINGS */

    /**
     * Get a List of Ratings for a Video
     * @param videoId ID of respected Video
     * @param limit how many ratings are requested
     * @param offset offset of reponse
     * @return ArrayList of Ratings
     * @throws Exception For negative limit or offset
     */
    public ArrayList<Rating> getRatingsByVideoId(int videoId, int limit, int offset) throws Exception {
        HashMap<String,Integer> settings = new HashMap<String, Integer>();
        if (limit > 0) settings.put("limit", limit);
        if (offset > 0) settings.put("offset", offset);

        if (limit < 0 || offset < 0) throw new Exception("Use positive limit and offset!");

        Response<Wrapper<Rating>> call = service.getRatings(videoId, settings).execute();

        if (call.code() == 200) {
            return call.body().getItems();
        } else if (call.code() == 404) {
            throw new Exception("Video not found");
        }
        throw new Exception(call.errorBody().string());
    }

    /**
     * Get a List of Ratings for a video
     * @param videoId ID of respected video
     * @return ArrayList of Ratungs
     * @throws Exception Something went wrong
     */
    public ArrayList<Rating> getRatingsByVideoId(int videoId) throws Exception {
        return this.getRatingsByVideoId(videoId, 0,0);
    }

    /**
     * Get a specific Rating of a video
     * @param videoId ID of respected video
     * @param ratingId ID of respected rating
     * @return Rating
     * @throws Exception Video or Rating not found
     */
    public Rating getRatingById(int videoId, int ratingId) throws Exception {
        Response<Rating> call = service.getRating(videoId,ratingId).execute();
        if (call.code() == 201) {
            return call.body();
        } else if (call.code() == 404) {
            throw new Exception("Video or Rating not found");
        }
        throw new Exception(call.errorBody().string());
    }

    /**
     * Create a Rating
     * @param videoId ID of respected video
     * @param rating The new rating
     * @return successfully created Rating
     * @throws Exception Video not found
     */
    public Rating createRating(int videoId, Rating rating) throws Exception {
        Response<Rating> call = service.createRating(
                this.apikey,
                videoId,
                rating.getRequestBody()).execute();
        if (call.code() == 201) {
            return call.body();
        } else if (call.code() == 404) {
            throw new Exception("Video not found");
        }
        throw new Exception(call.errorBody().string());
    }

    /**
     * Create a Rating
     * @param videoId ID of respected video
     * @param rating the rating
     * @return Successfully created Rating
     * @throws Exception Something went wrong
     */
    public Rating createRating(int videoId, double rating) throws Exception {
        return this.createRating(videoId, new Rating(rating));
    }

    /**
     * Update Rating of a video
     * @param videoId ID of respected Video
     * @param ratingId ID of respected Rating
     * @param rating The changed Rating
     * @return true if successfull
     * @throws Exception Video or Rating not found
     */
    public boolean updateRating(int videoId, int ratingId, Rating rating) throws Exception {
        Response<String> call = service.changeRating(
                this.apikey,
                videoId,
                ratingId,
                rating.getRequestBody()).execute();
        if (call.code() == 200) {
            return true;
        } else if (call.code() == 404) {
            throw new Exception("Video or Rating not found");
        }
        throw new Exception(call.errorBody().string());
    }

    /**
     * Delete Rating of a video
     * @param videoId ID of respected Video
     * @param ratingId ID of respected Rating
     * @return true if successfull
     * @throws Exception Video or Rating not found
     */
    public boolean deleteRating(int videoId, int ratingId) throws Exception {
        Response<String> call = service.deleteRating(this.apikey, videoId, ratingId).execute();
        if (call.code() == 200) {
            return true;
        } else if (call.code() == 404) {
            throw new Exception("Video or Rating not found");
        }
        throw new Exception(call.errorBody().string());
    }



    /* ===== SEARCH ===== */

    /**
     * Search for a category by its name
     * @param name category name to be searched
     * @param limit amount of fitting categories
     * @param offset search offset
     * @return ArrayList of VideoCategories
     * @throws Exception No negative limit and offset
     */
    public ArrayList<VideoCategory> getCategoryByName(String name, int limit, int offset) throws Exception {
        HashMap<String, Integer> settings = new HashMap<String, Integer>();
        if (limit > 0 )  settings.put("limit", limit);
        if (offset > 0) settings.put("offset", offset);

        if (limit < 0 || offset < 0) throw new Exception("Use positive limit and offset!");
        if (name == null) return new ArrayList<VideoCategory>();

        Response<SearchWrapper<VideoCategory>> call = service.searchCategory(settings, name).execute();
        if(call.code() == 200) {
            return call.body().getItems();
        }
        throw new Exception(call.errorBody().string());
    }

    /**
     * Search for a category by its name
     * @param name search term
     * @return ArrayList of VideoCategories
     * @throws Exception Something went wrong
     */
    public ArrayList<VideoCategory> getCategoryByName(String name) throws Exception {
        return this.getCategoryByName(name, 0,0);
    }

    /**
     * Search for a video with title and desctiption
     * @param title title of video
     * @param description description of a video
     * @param limit amount of responses
     * @param offset search offset
     * @return ArrayList of Videos
     * @throws Exception No negative limit or offset
     */
    public ArrayList<Video> getVideoByTitleAndDescription(String title, String description,
                                                          int limit, int offset) throws Exception {
        HashMap<String, Integer> settings = new HashMap<String, Integer>();
        if (limit > 0 )  settings.put("limit", limit);
        if (offset > 0) settings.put("offset", offset);

        if (limit < 0 || offset < 0) throw new Exception("Use positive limit and offset!");

        HashMap<String,String> queries = new HashMap<String, String>();
        if (title != null)       queries.put("title", title);
        if (description != null) queries.put("description", description);

        if (title == null && description == null) return new ArrayList<Video>();

        Response<SearchWrapper<Video>> call = service.searchVideo(settings, queries).execute();

        if (call.code() == 200) {
            return call.body().getItems();
        }
        throw new Exception(call.errorBody().string());
    }

    /**
     * Search for a video with title and description
     * @param title title of a video
     * @param description description of a video
     * @return ArrayList of videos
     * @throws Exception Something went wrong
     */
    public ArrayList<Video> getVideoByTitleAndDescription(String title, String description) throws Exception{
        return this.getVideoByTitleAndDescription(title,description,0,0);
    }

    /**
     * Search for a video with a title
     * @param title title of a video
     * @param limit amount of responses
     * @param offset search offset
     * @return ArrayList of videos
     * @throws Exception Something went wrong
     */
    public ArrayList<Video> getVideoByTitle(String title, int limit, int offset) throws Exception {
        return this.getVideoByTitleAndDescription(title, null, limit, offset);
    }

    /**
     * Search for a video with a title
     * @param title title of a video
     * @return ArrayList of videos
     * @throws Exception Something went wrong
     */
    public ArrayList<Video> getVideoByTitle(String title) throws Exception {
        return this.getVideoByTitleAndDescription(title, null, 0,0);
    }

    /**
     * Search for a video with a description
     * @param description description of a video
     * @param limit amount of responses
     * @param offset search offset
     * @return ArrayList of videos
     * @throws Exception Something went wrong
     */
    public ArrayList<Video> getVideoByDescription(String description, int limit, int offset) throws Exception {
        return this.getVideoByTitleAndDescription(null, description, limit, offset);
    }

    /**
     * Search for a video with a description
     * @param description description of a video
     * @return ArrayList of videos
     * @throws Exception Something went wrong
     */
    public ArrayList<Video> getVideoByDescription(String description) throws Exception {
        return this.getVideoByTitleAndDescription(null, description, 0,0);
    }

    /**
     * Search for a playlist by name
     * @param name name of the playlist
     * @param limit amount of responses
     * @param offset search offset
     * @return ArrayList of Playlists
     * @throws Exception Something went wrong
     */
    public ArrayList<Playlist> getPlaylistByName(String name, int limit, int offset) throws Exception {
        HashMap<String, Integer> settings = new HashMap<String, Integer>();
        if (limit > 0 )  settings.put("limit", limit);
        if (offset > 0) settings.put("offset", offset);

        if (limit < 0 || offset < 0) throw new Exception("Use positive limit and offset!");
        if (name == null) return new ArrayList<Playlist>();

        Response<SearchWrapper<Playlist>> call = service.searchPlaylist(settings, name).execute();

        if (call.code() == 200) {
            return call.body().getItems();
        }
        throw new Exception(call.errorBody().string());
    }

    /**
     * Search for a playlist by name
     * @param name name of the playlist
     * @return ArrayList of Playlist
     * @throws Exception Something went wrong
     */
    public ArrayList<Playlist> getPlaylistByName(String name) throws Exception {
        return this.getPlaylistByName(name, 0, 0);
    }


    /**
     * Get popular Videos
     * @param limit limit amount of videos, Default is 20
     * @param offset offset of respoinse, default is 0
     * @return ArrayList of Videos
     * @throws Exception for negative input
     */
    public ArrayList<Video> getPopularVideos(final int limit, final int offset) throws Exception {
        HashMap<String, Integer> settings = new HashMap<String, Integer>();
        if (limit > 0 )  settings.put("limit", limit);
        if (offset > 0) settings.put("offset", offset);

        if (limit < 0 || offset < 0) throw new Exception("Use positive limit and offset!");

        Response<SearchWrapper<Video>> call = service.getPopularVideos(settings).execute();

        if (call.code() == 200) {
            return call.body().getItems();
        }
        throw new Exception("Something went wrong!");

    }

    /**
     * Get popular Videos
     * @return ArrayList of popular Videos
     * @throws Exception Something went wrong
     */
    public ArrayList<Video> getPopularVideos() throws Exception {
        return this.getPopularVideos(0,0);
    }

    /**
     * Get popular Videos
     * @param limit limit amount of videos, Default is 20
     * @param offset offset of respoinse, default is 0
     * @return ArrayList of Videos
     * @throws Exception for negative input
     */
    public ArrayList<Video> getNewVideos(final int limit, final int offset) throws Exception {
        HashMap<String, Integer> settings = new HashMap<String, Integer>();
        if (limit > 0 )  settings.put("limit", limit);
        if (offset > 0) settings.put("offset", offset);

        if (limit < 0 || offset < 0) throw new Exception("Use positive limit and offset!");

        Response<SearchWrapper<Video>> call = service.getNewVideos(settings).execute();

        if (call.code() == 200) {
            return call.body().getItems();
        }
        throw new Exception("Something went wrong!");

    }

    /**
     * Get popular Videos
     * @return ArrayList of popular Videos
     * @throws Exception Something went wrong
     */
    public ArrayList<Video> getNewVideos() throws Exception {
        return this.getNewVideos(0,0);
    }

    /* PLAYLISTS */

    /**
     * Get a Users Playlist
     * @param uid playlist owner Id
     * @param limit response limit
     * @param offset response offset
     * @return ArrayList of Playlists
     * @throws Exception Something went wrong
     */
    public ArrayList<Playlist> getUserPlaylists(int uid, int limit, int offset) throws Exception{
        HashMap<String, Integer> settings = new HashMap<String, Integer>();
        if (limit > 0 )  settings.put("limit", limit);
        if (offset > 0) settings.put("offset", offset);

        if (limit < 0 || offset < 0) throw new Exception("Use positive limit and offset!");

        Response<Wrapper<Playlist>> call = service.getUserPlaylists(this.apikey, uid, settings).execute();
        if (call.code() == 201) {
            return call.body().getItems();
        } else throw new Exception(call.errorBody().string());
    }

    /**
     * Get a users Playlists
     * @param uid playlists owner Id
     * @return ArrayList of Playlist
     * @throws Exception Something went wrong
     */
    public ArrayList<Playlist> getUserPlaylists(int uid) throws Exception {
        return this.getUserPlaylists(uid, 0,0);
    }

    /**
     * Create a Playlist
     * @param uid
     * @param p
     * @return
     * @throws Exception
     */
    public Playlist createPlaylist(int uid, Playlist p) throws Exception {
        Response<Playlist> call = service.createPlaylist(this.apikey, uid, p.getRequestBody()).execute();
        if (call.code() == 201) {
            return call.body();
        } else throw new Exception(call.errorBody().string());
    }

    /**
     * Update Playlist name and/or public status
     * @param uid userId
     * @param pid Playlist ID
     * @param playlistName New name
     * @param playlistPublic Is public or not
     * @return True if 200
     * @throws Exception Something went wrong
     */
    public boolean updatePlaylist(int uid, int pid, String playlistName, Integer playlistPublic) throws Exception {
        ArrayList<PatchBody> patches = new ArrayList<PatchBody>();
        if (playlistName != null) {
            patches.add(new PatchBody("replace", "/name", playlistName));
        }
        if (playlistPublic != null) {
            patches.add(new PatchBody("replace", "/public", playlistPublic.toString()));
        }
        Response<String> call= service.changePlaylist(this.apikey, uid, pid, patches).execute();
        if (call.code() == 200) {
            return true;
        } else throw new Exception(call.errorBody().string());
    }

    /**
     * Get a specific Playlist
     * @param uid UserId of Playlist owner
     * @param pid Playlist Id
     * @return Playlist object
     * @throws Exception Something went wrong
     */
    public Playlist getPlaylistById(int uid, int pid) throws Exception {
        Response<Playlist> call = service.getPlaylistById(uid, pid).execute();
        if (call.code() == 200) {
            return call.body();
        } else throw new Exception(call.errorBody().string());
    }

    /**
     * Delete a specific Playlist of a User
     * @param uid User Id
     * @param pid Playlist Id
     * @return True if successful
     * @throws Exception Something went wrong
     */
    public boolean deletePlaylistByID(int uid, int pid) throws Exception {
        Response<String> call = service.deletePlaylistById(this.apikey, uid, pid).execute();
        if (call.code() == 200) {
            return true;
        } else throw new Exception(call.errorBody().string());
    }


    /**
     * Get the Followers of a Playlist
     * @param uid Playlist owner id
     * @param pid playlist Id
     * @param limit result limit
     * @param offset result offset
     * @return ArrayList of Users
     * @throws Exception Something went wrong
     */
    public ArrayList<User> getFollowersByPlaylistId(int uid, int pid,
                                                    int limit, int offset) throws Exception {
        HashMap<String, Integer> settings = new HashMap<String, Integer>();
        if (limit > 0 )  settings.put("limit", limit);
        if (offset > 0) settings.put("offset", offset);

        if (limit < 0 || offset < 0) throw new Exception("Use positive limit and offset!");

        Response<Wrapper<User>> call = service.getFollowersByPlaylistId(
                this.apikey, uid, pid, settings).execute();
        if (call.code() == 200) {
            return call.body().getItems();
        } else throw new Exception(call.errorBody().string());
    }

    /**
     * Get the Followers of a Playlist
     * @param uid Playlist Owners Id
     * @param pid Playlist Id
     * @return ArrayList of Users
     * @throws Exception Something went wrong
     */
    public ArrayList<User> getFollowersByPlaylistId(int uid, int pid) throws Exception {
        return this.getFollowersByPlaylistId(uid, pid, 0,0);
    }

    /**
     * Follow a Playlist (May not be ones own Playlist)
     * @param uid Playlist Owner Id
     * @param pid Playlist Id
     * @return True if successfull
     * @throws Exception Something went Wrong
     */
    public boolean followPlaylistById(int uid, int pid) throws Exception {
        Response<String> call = service.followPlaylistById(this.apikey, uid, pid).execute();
        if (call.code() == 204) {
            return true;
        } else throw new Exception(call.errorBody().string());
    }

    /**
     * Unfollow a Playlist (May not be ones own Playlist)
     * @param uid Playlist Owner Id
     * @param pid Playlist Id
     * @return True if Unfollow successful
     * @throws Exception Something went wrong
     */
    public boolean unfollowPlaylistById(int uid, int pid) throws Exception {
        Response<String> call = service.unfollowPlaylistById(this.apikey, uid, pid).execute();
        if (call.code() == 204) {
            return true;
        } else throw new Exception(call.errorBody().string());
    }

    /**
     * Get Videos associated with a Playlist
     * @param uid Playlist Owners Id
     * @param pid Playlist Id
     * @param limit result limit
     * @param offset result offset
     * @return ArrayList of Videos
     * @throws Exception Something went wrong
     */
    public ArrayList<Video> getVideosByPlaylistId(int uid, int pid, int limit, int offset) throws Exception {
        HashMap<String, Integer> settings = new HashMap<String, Integer>();
        if (limit > 0 )  settings.put("limit", limit);
        if (offset > 0) settings.put("offset", offset);

        if (limit < 0 || offset < 0) throw new Exception("Use positive limit and offset!");

        Response<Wrapper<Video>> call = service.getVideosByPlaylistId(uid, pid, settings).execute();
        if (call.code() == 200) {
            return call.body().getItems();
        } else throw new Exception(call.errorBody().string());
    }

    /**
     * Get Videos associated with a Playlist
     * @param uid Playlist Owners Id
     * @param pid Playlist Id
     * @return ArrayList of Videos
     * @throws Exception Something went wrong
     */
    public ArrayList<Video> getVideosByPlaylistId(int uid, int pid) throws Exception {
        return  getVideosByPlaylistId(uid, pid, 0,0);
    }

    /**
     * Add a Video to a playlist
     * @param uid Playlist owner Id
     * @param pid Playlist id
     * @param vid video id
     * @return True if successfull
     * @throws Exception Something went wrong
     */
    public boolean addVideoToPlaylist(int uid, int pid, int vid) throws Exception {
        Response<String> call = service.addVideoToPlaylist(
                this.apikey,
                uid,
                pid,
                new VidBody(vid)).execute();
        if (call.code() == 204) {
            return true;
        } else throw new Exception(call.errorBody().string());
    }

    /**
     * Reorder a Playlist
     * @param uid playlist owner
     * @param pid playlist id
     * @param rangeStart Start of Video Range that is moved
     * @param rangeLength Length of block
     * @param insertBefore insertion point for block of videos
     * @return true if successfull
     * @throws Exception Something went wrong
     */
    public boolean reorderPlaylist(int uid, int pid,
                                   int rangeStart, int rangeLength, int insertBefore) throws Exception {
        ReorderBody rb = new ReorderBody(rangeStart, rangeLength, insertBefore);

        Response<String> call = service.reorderPlaylist(this.apikey, uid, pid, rb).execute();

        if (call.code() == 204) return true;
        else throw new Exception(call.errorBody().string());
    }

    /**
     * Delete videos via their IDs from a playlist
     * @param uid playlist owner
     * @param pid playlist id
     * @param videoIDs ArrayList of VideoIDs
     * @return true if successful
     * @throws Exception Something went wrong along the way
     */
    public boolean deleteVideosFromPlaylist(int uid, int pid,
                                            ArrayList<Integer> videoIDs) throws Exception {

        Response<String> call = service.deleteVideosFromPlaylist(this.apikey, uid, pid,
                new DeleteVideosFromPlaylistBody(videoIDs)).execute();

        if (call.code() == 204)
            return true;
        else throw new Exception(call.errorBody().string());
    }

    public boolean deleteVideosFromPlaylist(int uid, int pid, int[] videoIds) throws Exception {

        Response<String> call = service.deleteVideosFromPlaylist(this.apikey, uid, pid,
                new DeleteVideosFromPlaylistBody(videoIds)).execute();

        if (call.code() == 204) 
            return true;
        else throw new Exception(call.errorBody().string());
    }

    /**
     * Retrieve all public playlists
     * @param limit Number of desired results
     * @param offset offset for retrival
     * @return ArrayList of Playlists
     * @throws Exception Something went wrong
     */
    public ArrayList<Playlist> getPublicPlaylists(int limit, int offset) throws Exception{
        HashMap<String, Integer> settings = new HashMap<String, Integer>();
        if (limit > 0 )  settings.put("limit", limit);
        if (offset > 0) settings.put("offset", offset);

        if (limit < 0 || offset < 0) throw new Exception("Use positive limit and offset!");

        Response<Wrapper<Playlist>> call = service.getPublicPlaylists(settings).execute();

        if(call.code() == 200) {
            return call.body().getItems();
        } else {
            throw new Exception(call.errorBody().string());
        }
    }

    /**
     * Retrieve first 20 Playlists
     * @return ArrayList of Playlists
     * @throws Exception Something went wrong
     */
    public ArrayList<Playlist> getPublicPlaylists() throws Exception {
        return this.getPublicPlaylists(0,0); //default
    }

    /**
     * Retrieve a public Playlist by its ID
     * @param pid Playlist id
     * @return Playlist object
     * @throws Exception Something went wrong
     */
    public Playlist getPublicPlaylist(int pid) throws Exception {
        Response<Playlist> call = service.getPublicPlaylist(pid).execute();
        if (call.code() == 200) {
            return call.body();
        } else throw new Exception(call.errorBody().string());
    }

    /**
     * Retrieve Videos of a Playlist by Id
     * @param pid Playlist Id
     * @param limit How many videos should be retrived
     * @param offset offset of results
     * @return ArrayList of Videos
     * @throws Exception Something went Wrong
     */
    public ArrayList<Video> getVideosOfPlaylistById(int pid, int limit, int offset) throws Exception {
        HashMap<String,Integer> settings = new HashMap<String, Integer>();
        if (limit > 0) settings.put("limit", limit);
        if (offset > 0) settings.put("offset", limit);

        if (limit < 0 || offset < 0) throw new Exception("Use positive limit and offset!");

        Response<Wrapper<Video>> call = service.getVideosOfPlaylistById(pid, settings).execute();

        if (call.code() == 200) {
            return call.body().getItems();
        } else throw new Exception(call.errorBody().string());
    }

    /**
     * Retrieve first 20 videos of a Playlist by its Id
     * @param pid Playlist Id
     * @return ArrayList of Videos
     * @throws Exception Something went wrong
     */
    public ArrayList<Video> getVideosOfPlaylistById(int pid) throws Exception {
        return getVideosOfPlaylistById(pid, 0,0);
    }


    /* VIDEO UPLOAD */

    /**
     * upload a video file to Ketos
     * @param file the video File
     * @param title Title of the video
     * @param describtion The video description
     * @return The Video object created
     * @throws Exception Either no video selected, something was wrong with the video,
     *                   the server had problems or a nuclear meltdown
     */
    public Video uploadVideo(File file, String title, String describtion) throws Exception {
        /**
         * Video Type	    Extension	MIME Type
         * Flash    	    .flv	    video/x-flv
         * MPEG-4	        .mp4	    video/mp4       <-- Most likely this one
         * iPhone Segment	.ts	        video/MP2T
         * 3GP Mobile	    .3gp	    video/3gpp
         * QuickTime	    .mov	    video/quicktime <-- Appearantly WA videos are those
         * A/V Interleave	.avi	    video/x-msvideo
         * Windows Media	.wmv	    video/x-ms-wmv
         */
        String mimeType = Files.probeContentType(file.toPath());
        if (mimeType == null) {
            throw new Exception("No file Path");
        } else if (!mimeType.startsWith("video/")) {
            throw new Exception("No Video selected");
        }

        // Make the file Multipart
        MultipartBody.Part video = MultipartBody.Part.createFormData(
                "file",
                file.getName(),
                RequestBody.create(MediaType.parse(mimeType), file));

        RequestBody titleBody = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody describtionBody = RequestBody.create(MediaType.parse("text/plain"), describtion);

        Response<Video> call = service.uploadVideo(
                this.apikey,
                video,
                titleBody,
                describtionBody).execute();

        if (call.code() == 201) {
            return call.body();
        } else
            // There is a problem with the file in question
            if (call.code() == 400) {
            throw new Exception(call.errorBody().string());
        } else
            // The Server has a problem
            if (call.code() >= 500) {
            throw new Exception(call.errorBody().string());
        } else {
            throw new Exception(call.errorBody().string());
        }
    }

}
