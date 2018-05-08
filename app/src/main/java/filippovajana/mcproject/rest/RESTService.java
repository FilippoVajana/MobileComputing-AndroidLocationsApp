package filippovajana.mcproject.rest;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RESTService
{
    private static final String BASE_URL = "https://ewserver.di.unimi.it/mobicomp/geopost/";
    private static EverywareLabSessionToken SESSION_TOKEN; //fv:fv
    private static Retrofit retrofit;

    public RESTService()
    {
        //init retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static EverywareLabSessionToken getSessionToken()
    {
        return SESSION_TOKEN;
    }


    public boolean checkLoginCredentials(String username, String password)
    {
        //build api service
        EverywareLabAPI apiService = retrofit.create(EverywareLabAPI.class);

        //build rest call
        Call<EverywareLabSessionToken> call = apiService.getSessionId(username, password);

        //execute call
        try
        {
            Response<EverywareLabSessionToken> response = call.execute();

            if (response.isSuccessful())
            {
                SESSION_TOKEN = response.body();
                return true;
            }
            return false;
        }
        catch (Exception ex)
        {
            return false;
        }
    }
}
