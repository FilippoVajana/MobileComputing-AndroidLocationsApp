package filippovajana.mcproject.rest;

import android.os.AsyncTask;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import filippovajana.mcproject.activity.LoginActivity;
import filippovajana.mcproject.helper.SystemHelper;
import filippovajana.mcproject.model.AppFriend;
import filippovajana.mcproject.model.UserProfile;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RESTService
{
    private static final String BASE_URL = "https://ewserver.di.unimi.it/mobicomp/geopost/";
    private static String SESSION_TOKEN;
    private static Retrofit retrofit;
    private static EverywareLabAPI apiService;

    public RESTService()
    {
        //init retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //init api service
        apiService = retrofit.create(EverywareLabAPI.class);
    }

    public static String getSessionToken()
    {
        return SESSION_TOKEN;
    }


    //TODO: on response trigger a popup in main activity


    //Login Task
    public class LoginTask extends AsyncTask<String, Void, Boolean>
    {

        private LoginActivity _activity;
        public LoginTask(LoginActivity activity)
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
            //build rest call
            Call<String> call = RESTService.apiService.getSessionId(username, password);

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


    //Friends Task
    public List<AppFriend> getFriendsList()
    {
        //get session id
        String sessioId = getSessionToken();

        //build rest call
        Call<FriendsListResponse> call = apiService.getFollowedFriends(sessioId);

        //execute call
        try
        {
            Response<FriendsListResponse> response = call.execute();

            if (response.isSuccessful())
            {
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "getUsersList Successful");
                return response.body().getFriendsList();
            }
            else
            {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "getUsersList Failed");
                return new ArrayList<>();
            }

        }catch (Exception e)
        {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "getUsersList Exception ");
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    //Users Task
    public UsersListRespose getUsers(String prefix, int limit)
    {
        //TODO: remove
        String token = getSessionToken();

        //build rest call
        Call<UsersListRespose> call = apiService.getUsers(
                getSessionToken(),
                prefix,
                limit);

        //execute call
        try
        {
            Response<UsersListRespose> response = call.execute();

            if (response.isSuccessful())
            {
                SystemHelper.logWarning(this.getClass(), "Retrieve users successful");
                return response.body();
            }
            else
            {
                SystemHelper.logWarning(this.getClass(), "Retrieve users failed");
                return new UsersListRespose();
            }

        }catch (Exception e)
        {
            SystemHelper.logError(this.getClass(), String.format("Logout %s", e.getMessage()));
            return null;
        }
    }

    //Profile Task
    @Nullable
    public UserProfile getUserProfile()
    {
        //get session id
        String sessioId = getSessionToken();

        //build rest call
        Call<UserProfile> call = apiService.getUserProfile(sessioId);

        //execute call
        try
        {
            Response<UserProfile> response = call.execute();

            if (response.isSuccessful())
            {
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "getUserProfile Successful");
                return response.body();
            }
            else
            {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "getUserProfile Failed");
                return null;
            }

        }catch (Exception e)
        {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "getUserProfile Exception ");
            e.printStackTrace();
            return null;
        }
    }


    //Status Task
    public void updateUserStatus(UserProfile profile)
    {
        //get query parameters
        String sessioId = getSessionToken();
        String message = profile.get_stateMessage();
        String latitude = String.valueOf(profile.get_latitude());
        String longitude = String.valueOf(profile.get_longitude());

        //build rest call
        Call<Void> call = apiService.updateUserStatus(
                sessioId,
                message,
                latitude,
                longitude);

        //execute call
        try
        {
            Response<Void> response = call.execute();

            if (response.isSuccessful())
            {
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "updateUserStatus Successful");
                return;
            }
            else
            {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "updateUserStatus Failed");
                return;
            }

        }catch (Exception e)
        {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "updateUserStatus Exception ");
            e.printStackTrace();
            return;
        }
    }


    //Logout
    public boolean logoutUser()
    {
        //build rest call
        Call<Void> call = apiService.logoutUser(getSessionToken());

        //execute call
        try
        {
            Response<Void> response = call.execute();

            if (response.isSuccessful())
            {
                SystemHelper.logWarning(this.getClass(), "Logout successful");
                return true;
            }
            else
            {
                SystemHelper.logWarning(this.getClass(), "Logout failed");
                return false;
            }

        }catch (Exception e)
        {
            SystemHelper.logError(this.getClass(), String.format("Logout %s", e.getMessage()));
            return false;
        }
    }
}
