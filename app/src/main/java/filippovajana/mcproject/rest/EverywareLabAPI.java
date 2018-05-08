package filippovajana.mcproject.rest;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.POST;

public interface EverywareLabAPI
{
    @POST("login/")
    Call<EverywareLabSessionToken> getSessionId(@Field("username") String username,
                                                @Field("password") String password);
}
