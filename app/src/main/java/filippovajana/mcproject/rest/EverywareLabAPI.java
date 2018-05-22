package filippovajana.mcproject.rest;

import java.util.List;

import filippovajana.mcproject.model.AppFriend;
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
}
