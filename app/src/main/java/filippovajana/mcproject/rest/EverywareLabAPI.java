package filippovajana.mcproject.rest;

import filippovajana.mcproject.model.UserProfile;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface EverywareLabAPI
{
    @POST("login/")
    @FormUrlEncoded
    Call<String> getSessionId(@Field("username") String username,
                              @Field("password") String password);


    @GET("followed/")
    Call<FriendsListResponse> getFollowedFriends(@Query("session_id") String sessionId);


    @GET("profile/")
    Call<UserProfile> getUserProfile(@Query("session_id") String sessionId);


    @GET("status_update/")
    Call<Void> updateUserStatus(@Query("session_id") String sessionId,
                                @Query("message") String message,
                                @Query("lat") String latitude,
                                @Query("lon") String longitude);


    @GET("logout/")
    Call<Void> logoutUser(@Query("session_id") String sessionId);


    @GET("users/")
    Call<UsersListResponse> getUsers(@Query("session_id") String sessionId,
                                     @Query("usernamestart") String namePrefix,
                                     @Query("limit") Integer resultLimit);

    @GET("follow/")
    Call<String> followUser(@Query("session_id") String sessioId,
                            @Query("username") String username);
}
