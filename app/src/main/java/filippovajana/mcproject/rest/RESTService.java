package filippovajana.mcproject.rest;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import filippovajana.mcproject.activity.LoginActivity;
import filippovajana.mcproject.helper.FragmentHelper;
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

    private static Retrofit RETROFIT;
    private static EverywareLabAPI API_SERVICE;

    public RESTService()
    {
        //init RETROFIT
        RETROFIT = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //init api service
        API_SERVICE = RETROFIT.create(EverywareLabAPI.class);
    }

    public static String getSessionToken()
    {
        return SESSION_TOKEN;
    }
    public static void setSessionToken(String sessionToken)
    {
        SESSION_TOKEN = sessionToken;
    }


    //Login Task
    public String doLogin(String username, String password)
    {
        Call<String> call = API_SERVICE.getSessionId(username, password);

        //execute call
        try
        {
            SystemHelper.logWarning(this.getClass(), "Try login");

            Response<String> response = call.execute();

            if (response.isSuccessful())
            {
                SystemHelper.logWarning(this.getClass(), "Login successful");

                return response.body();
            }
            else
            {
                SystemHelper.logWarning(this.getClass(), "Login failed");

                return null;
            }
        }catch (Exception e)
        {
            SystemHelper.logError(this.getClass(), e.getMessage());

            return null;
        }
    }


    //Friends Task
    public List<AppFriend> getFriendsListCall()
    {
        //get session id
        String sessioId = getSessionToken();

        //build rest call
        Call<FriendsListResponse> call = API_SERVICE.getFollowedFriends(sessioId);

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
    public UsersListResponse getUsersCall(String prefix, int limit)
    {
        //build rest call
        Call<UsersListResponse> call = API_SERVICE.getUsers(
                getSessionToken(),
                prefix,
                limit);

        //execute call
        try
        {
            Response<UsersListResponse> response = call.execute();

            if (response.isSuccessful())
            {
                SystemHelper.logWarning(this.getClass(), "Retrieve users successful");
                return response.body();
            }
            else
            {
                SystemHelper.logWarning(this.getClass(), "Retrieve users failed");
                return new UsersListResponse();
            }

        }catch (Exception e)
        {
            SystemHelper.logError(this.getClass(), String.format("Logout %s", e.getMessage()));
            return null;
        }
    }

    //Profile Task
    @Nullable
    public UserProfile getUserProfileCall()
    {
        //get session id
        String sessioId = getSessionToken();

        //build rest call
        Call<UserProfile> call = API_SERVICE.getUserProfile(sessioId);

        //execute call
        try
        {
            Response<UserProfile> response = call.execute();

            if (response.isSuccessful())
            {
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "getUserProfileCall Successful");
                return response.body();
            }
            else
            {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "getUserProfileCall Failed");
                return null;
            }

        }catch (Exception e)
        {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "getUserProfileCall Exception ");
            e.printStackTrace();
            return null;
        }
    }


    //Status Task
    public void updateUserStatusCall(UserProfile profile)
    {
        //get query parameters
        String sessioId = getSessionToken();
        String message = profile.get_stateMessage();
        String latitude = String.valueOf(profile.get_latitude());
        String longitude = String.valueOf(profile.get_longitude());

        //build rest call
        Call<Void> call = API_SERVICE.updateUserStatus(
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
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "updateUserStatusCall Successful");
                return;
            }
            else
            {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "updateUserStatusCall Failed");
                return;
            }

        }catch (Exception e)
        {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "updateUserStatusCall Exception ");
            e.printStackTrace();
            return;
        }
    }


    //Logout Task
    public boolean logoutUserCall()
    {
        //build rest call
        Call<Void> call = API_SERVICE.logoutUser(getSessionToken());

        //execute call
        try
        {
            Response<Void> response = call.execute();

            if (response.isSuccessful())
            {
                //clear session token
                SESSION_TOKEN = new String();
                LoginActivity.clearSharedPreferences();

                //clear navigation stack
                FragmentHelper.clearNavigationStack();

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

    //Follow User Task
    public FollowUserResponse followUserCall(@NonNull String username)
    {
        //build rest call
        Call<String> call = API_SERVICE.followUser(getSessionToken(), username);

        //execute call
        try
        {
            Response<String> response = call.execute();

            if (response.isSuccessful())
            {
                SystemHelper.logWarning(this.getClass(), "Follow user successful");
                return new FollowUserResponse("Success");
            }
            else
            {
                SystemHelper.logWarning(this.getClass(), "Follow user failed");
                return new FollowUserResponse(response.errorBody().string());
            }

        }catch (Exception e)
        {
            SystemHelper.logError(this.getClass(), String.format("Follow user %s", e.getMessage()));
            return null;
        }
    }
}
