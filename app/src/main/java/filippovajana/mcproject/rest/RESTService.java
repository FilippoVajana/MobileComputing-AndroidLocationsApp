package filippovajana.mcproject.rest;

import android.os.AsyncTask;

import java.util.List;

import filippovajana.mcproject.activity.LoginActivity;
import filippovajana.mcproject.model.AppFriend;
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
                .addConverterFactory(GsonConverterFactory.create())
                .build();


    }

    public static String getSessionToken()
    {
        return SESSION_TOKEN;
    }

    //Login Activity
    public class loginCheckTask extends AsyncTask<String, Void, Boolean>
    {

        private LoginActivity _activity;
        public loginCheckTask(LoginActivity activity)
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
            _activity.loginCheckHandler(result);
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


    //Friends Activity
    public static List<AppFriend> getFollowedFriends()
    {
        return null;
    }

    private class getFollowedFriendTask extends AsyncTask<Void, Void, List<AppFriend>>
    {

        @Override
        protected List<AppFriend> doInBackground(Void... voids)
        {
            //call rest api

            return null;
        }

        @Override
        protected void onPostExecute(List<AppFriend> result)
        {
            //add items to app model
        }
    }
}
