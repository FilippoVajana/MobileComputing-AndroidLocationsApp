package filippovajana.mcproject.rest;

import android.app.Activity;
import android.os.AsyncTask;

import filippovajana.mcproject.activity.LoginActivity;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RESTService
{
    private static final String BASE_URL = "https://ewserver.di.unimi.it/mobicomp/geopost/";
    private static String SESSION_TOKEN; //fv:fv
    private static Retrofit retrofit;

    public RESTService()
    {
        //init retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();


    }

    public static String getSessionToken()
    {
        return SESSION_TOKEN;
    }


    public class LoginCheckTask extends AsyncTask<String, Void, Boolean >
    {

        private LoginActivity _activity;
        public LoginCheckTask(LoginActivity activity)
        {
            _activity = activity;
        }

        @Override
        protected Boolean doInBackground(String... params)
        {
            return checkLoginCredentials(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(Boolean result)
        {
            _activity.LoginCheckHandler(result);
        }


        private boolean checkLoginCredentials(String username, String password)
        {
            //build api service
            EverywareLabAPI apiService = retrofit.create(EverywareLabAPI.class);

            //build rest call
            Call<String> call = apiService.getSessionId(username, password);

            //execute call
            try
            {
                //TODO: run network operations on dedicated thread
                Response<String> response = call.execute();

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
}
