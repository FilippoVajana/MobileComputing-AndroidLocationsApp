package filippovajana.mcproject.rest;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface EverywareLabAPI
{
    @POST("login/")
    @FormUrlEncoded
    Call<String> getSessionId(@Field("username") String username,
                              @Field("password") String password);
}
