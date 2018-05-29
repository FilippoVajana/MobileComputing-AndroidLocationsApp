package filippovajana.mcproject.model;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import filippovajana.mcproject.rest.RESTService;

public class AppDataModel
{
    //services
    private RESTService _rest;

    private ArrayList<AppFriend> _friendsList;
    private UserProfile _userProfile;

    //SINGLETON
    private static AppDataModel _instance;
    private AppDataModel()
    {
        //init services
        _rest = new RESTService();

        //init fields
        _friendsList = new ArrayList<>();
        _userProfile = new UserProfile();
    }

    public static AppDataModel getInstance()
    {
        if (_instance == null)
            _instance = new AppDataModel();
        return _instance;
    }


    //Profile
    public UserProfile get_userProfile()
    {
        synchronized (_userProfile)
        {
            //update with remote information
            _userProfile = _rest.getUserProfile();
            return _userProfile;
        }
    }

    public void set_userProfile(UserProfile profile)
    {
        synchronized (_userProfile)
        {
            //update remote profile
            _rest.updateUserStatus(profile);

            //update local profile
            _userProfile = profile;
        }
    }


    //Friends
    public ArrayList<AppFriend> get_friendsList()
    {
        synchronized (_friendsList)
        {
            return _friendsList;
        }
    }
    public void updateFriendsList()
    {
        //call rest API
        List<AppFriend> list = _rest.getFriendsList();

        //update list
        synchronized (_friendsList)
        {
            //empty list
            _friendsList.removeAll(new ArrayList<>(_friendsList));

            //fill list
            for (AppFriend f : list)
            {
                _friendsList.add(f);

                //log
                Logger.getLogger(getClass().getName()).log(Level.INFO, String.format("Added friend %s", f.getUsername()));
            }
        }
    }
    public AppFriend getItem(int position)
    {
        synchronized (_friendsList)
        {
            return _friendsList.get(position);
        }
    }


}
