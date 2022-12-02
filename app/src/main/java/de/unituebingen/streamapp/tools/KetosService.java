package de.unituebingen.streamapp.tools;

import java.util.ArrayList;
import java.util.HashMap;

import de.unituebingen.streamapp.tools.requestBodies.LoginBody;
import de.unituebingen.streamapp.tools.wrapper.SearchWrapper;
import de.unituebingen.streamapp.tools.wrapper.Wrapper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import de.unituebingen.streamapp.tools.requestBodies.*;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface KetosService {

    /* CATEGORIES */
    @GET("v1/categories")
    Call<Wrapper<VideoCategory>> listCategories(@QueryMap HashMap<String,Integer> settingsMap);

    @GET("v1/categories/{id}")
    Call<VideoCategory> getCategory(@Path("id") int categoryId);

    @POST("v1/categories")
    Call<VideoCategory> createCategory(@Header("Authorization") String apikey,
                                       @Body CategoryBody category);

    @DELETE("v1/categories/{cid}")
    Call<String> deleteCategoryById(@Header("Authorization") String apikey,
                                    @Path("cid") int cid);

    @GET("v1/categories/{cid}/videos")
    Call<Wrapper<Video>> getVideoByCategory(@Path("cid") int catId,
                                            @QueryMap HashMap<String,Integer> settingsMap);

    @PUT("v1/categories/{cid}/videos")
    Call<String> addVideo2Category(@Header("Authorization") String apikey,
                                   @Path("cid") int catId,
                                   @Body VidCatBody vid);

    @DELETE("v1/categories/{cid}/videos")
    Call<String> deleteVideoFromCategory(@Header("Authorization") String apikey,
                                         @Path("cid") int catId,
                                         @Body VidCatBody vid);

    /* USER */
    @POST("v1/register")
    Call<User> registerUser(@Body RegisterBody newuser);

    @POST("v1/login")
    Call<ApiKey> login(@Body LoginBody loginBody);

    @GET("v1/users")
    Call<Wrapper<User>> getUsers(@Header("Authorization") String apikey,
                                 @QueryMap HashMap<String,Integer> settings);

    @GET("v1/me")
    Call<User> getMe(@Header("Authorization") String apikey);

    @GET("v1/users/{uid}")
    Call<User> getUser(@Header("Authorization") String apikey,
                       @Path("uid") int userId);

    @PATCH("v1/users/{uid}")
    @Headers("Content-Type: application/json-patch+json")
    Call<String> updateUser(@Header("Authorization") String apikey, @Path("uid") int userId,
                            @Body ArrayList<PatchBody> patchList);

    /* PLAYLIST */
    @GET("v1/users/{uid}/playlists")
    Call<Wrapper<Playlist>> getUserPlaylists(@Header("Authorization") String apikey,
                                               @Path("uid") int userId,
                                             @QueryMap HashMap<String, Integer> settingsMap);

    @POST("v1/users/{uid}/playlists")
    Call<Playlist> createPlaylist(@Header("Authorization") String apikey,
                                  @Path("uid") int userId,
                                  @Body PlaylistBody playlist);

    @PUT("v1/users/{uid}/playlists/{pid}")
    Call<String> changePlaylist(@Header("Authorization") String apikey,
                                @Path("uid") int userId,
                                @Path("pid") int playlistId,
                                @Body Playlist playlist);

    @PATCH("v1/users/{uid]/playlists/{pid}")
    Call<String> changePlaylist(@Header("Authorization") String apikey,
                                @Path("uid") int userId,
                                @Path("pid") int playlistId,
                                @Body ArrayList<PatchBody> playlistPatch);

    @GET("v1/users/{uid}/playlists/{pid}")
    Call<Playlist> getPlaylistById(@Path("uid") int userId,
                                   @Path("pid") int playlistId);


    @DELETE("v1/users/{uid]/playlists/{pid}")
    Call<String> deletePlaylistById(@Header("Authorization") String apikey,
                                @Path("uid") int userId,
                                @Path("pid") int playlistId);

    @GET("v1/users/{uid}/playlists/{pid}/followers")
    Call<Wrapper<User>> getFollowersByPlaylistId(@Header("Authorization") String apikey,
                                                 @Path("uid") int userId,
                                                 @Path("pid") int playlistId,
                                                 @QueryMap HashMap<String, Integer> settingsMap);

    @GET("v1/users/{uid}/playlists/{pid}/followers")
    Call<String> followPlaylistById(@Header("Authorization") String apikey,
                                    @Path("uid") int userId,
                                    @Path("pid") int playlistId);

    @DELETE("v1/users/{uid}/playlists/{pid]/followers")
    Call<String> unfollowPlaylistById(@Header("Authorization") String apikey,
                                      @Path("uid") int userId,
                                      @Path("pid") int playlistId);

    @GET("v1/users/{uid}/playlists/{pid}/videos")
    Call<Wrapper<Video>> getVideosByPlaylistId(@Path("uid") int uid,
                                               @Path("pid") int pid,
                                               @QueryMap HashMap<String, Integer> settingsMap);

    @POST("v1/users/{uid}/playlists/{pid}/videos")
    Call<String> addVideoToPlaylist(@Header("Authorization") String apikey,
                                    @Path("uid") int userId,
                                    @Path("pid") int playlistId,
                                    @Body VidBody video);

    @POST("v1/users/{uid}/playlists/{pid}/videos")
    Call<String> reorderPlaylist(@Header("Authorization") String apikey,
                                 @Path("uid") int userId,
                                 @Path("pid") int pid,
                                 @Body ReorderBody rb);

    @DELETE("v1/users/{uid}/playlists/{pid}/videos")
    Call<String> deleteVideosFromPlaylist(@Header("Authorization") String apikey,
                                          @Path("uid") int userId,
                                          @Path("pid") int pid,
                                          @Body DeleteVideosFromPlaylistBody videoIds);

    @GET("v1/playlists")
    Call<Wrapper<Playlist>> getPublicPlaylists(@QueryMap HashMap<String,Integer> settings);

    @GET("v1/playlists/{pid}")
    Call<Playlist> getPublicPlaylist(@Path("{pid}") int pid);

    @GET("v1/playlists/{pid}/videos")
    Call<Wrapper<Video>> getVideosOfPlaylistById(@Path("pid") int playlistId,
                                                @QueryMap HashMap<String,Integer> settings);


    /* VIDEOS */
    @GET("v1/videos")
    Call<Wrapper<Video>> getVideos(@QueryMap HashMap<String, Integer> settingsMap);

    @GET("v1/videos/{vid}")
    Call<Video> getVideo(@Path("vid") int videoId);

    @PATCH("v1/videos/{vid}")
    @Headers("Content-Type: application/json-patch+json")
    Call<String> updateVideo(@Header("Authorization") String apikey,
                             @Path("vid") int videoId,
                             @Body ArrayList<PatchBody> patchList);

    @DELETE("v1/videos/{vid}")
    Call<String> deleteVideo(@Header("Authorization") String apikey,
                             @Path("vid") int videoId);




    /* COMMENTS */
    @GET("v1/videos/{vid}/comments/")
    Call<Wrapper<Comment>> getComments(@Path("vid") int videoId,
                                        @QueryMap HashMap<String, Integer> settingsMap);

    @POST("v1/videos/{vid}/comments")
    Call<Comment> createComment(@Header("Authorization") String apikey,
                                @Path("vid") int videoId,
                                @Body CommentBody newComment);

    @GET("v1/videos/{vid}/comments/{cid}")
    Call<Comment> getComment(@Path("vid") int videoId,
                             @Path("cid") int commentId);

    @PUT("v1/videos/{vid}/comments/{cid}")
    Call<String> changeComment(@Header("Authorization") String apikey,
                               @Path("vid") int videoId,
                               @Path("cid") int commentId,
                               @Body CommentBody comment);

    @DELETE("v1/videos/{vid}/comments/{cid}")
    Call<String> deleteComment(@Header("Authorization") String apikey,
                               @Path("vid") int videoId,
                               @Path("cid") int commentId);


    /* RATINGS */
    @GET("v1/videos/{vid}/ratings")
    Call<Wrapper<Rating>> getRatings(@Path("vid") int videoId,
                                    @QueryMap HashMap<String, Integer> settingsMap);

    @POST("v1/videos/{vid}/ratings")
    Call<Rating> createRating(@Header("Authorization") String apikey,
                              @Path("vid") int videoId,
                              @Body RatingBody rating);

    @GET("v1/videos/{vid}/ratings/{rid}")
    Call<Rating> getRating(@Path("vid") int videoId,
                           @Path("rid") int ratingId);

    @PUT("vi/videos/{vid}/ratings/{rid}")
    Call<String> changeRating(@Header("Authorization") String apikey,
                              @Path("vid") int videoId,
                              @Path("rid") int ratingId,
                              @Body RatingBody updatedRating);

    @DELETE("vi/videos/{vid}/ratings/{rid}")
    Call<String> deleteRating(@Header("Authorization") String apikey,
                              @Path("vid") int videoId,
                              @Path("rid") int ratingId);


    /* SEARCH */
    @GET("v1/search?type=category")
    Call<SearchWrapper<VideoCategory>> searchCategory(@QueryMap HashMap<String, Integer> settings,
                                                      @Query("name") String name);

    @GET("v1/search?type=video")
    Call<SearchWrapper<Video>> searchVideo(@QueryMap HashMap<String, Integer> settings,
                                           @QueryMap HashMap<String, String> queries);

    @GET("v1/search?type=playlist")
    Call<SearchWrapper<Playlist>> searchPlaylist(@QueryMap HashMap<String, Integer> settings,
                                                 @Query("name") String name);

    @GET("v1/search?type=popularvideos")
    Call<SearchWrapper<Video>> getPopularVideos(@QueryMap HashMap<String, Integer> settings);

    @GET("v1/search?type=newvideos")
    Call<SearchWrapper<Video>> getNewVideos(@QueryMap HashMap<String, Integer> settings);


    /* VIDEO UPLOAD */
    @POST("v1/videos")
    @Multipart
    //@Headers("Content-Type: multipart/form-data")
    Call<Video> uploadVideo(@Header("Authorization") String apikey,
                            @Part MultipartBody.Part filePart,
                            @Part("title") RequestBody title,
                            @Part("describtion") RequestBody description);

}
