package filippovajana.mcproject.model;

import java.util.ArrayList;
import java.util.List;

import filippovajana.mcproject.helper.SystemHelper;
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
        _mergedList = new ArrayList<>();
        _friendsList = new ArrayList<>();
        _restaurantsList = new ArrayList<>();
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
            UserProfile result = _rest.getUserProfileCall();

            //check for null result
            if (result == null)
                return null;
            else
                _userProfile = result; //update local reference

            return _userProfile;
        }
    }

    public void set_userProfile(UserProfile profile)
    {
        synchronized (_userProfile)
        {
            //update remote profile
            _rest.updateUserStatusCall(profile);

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
        List<AppFriend> list = _rest.getFriendsListCall();

        //update list
        synchronized (_friendsList)
        {
            //empty list
            _friendsList.clear();

            //fill list
            for (AppFriend f : list)
            {
                _friendsList.add(f);

                //log
                SystemHelper.logWarning(this.getClass(), String.format("Added %s", f.getName()));
            }
        }
    }
    public AppFriend getFriend(int position)
    {
        synchronized (_friendsList)
        {
            return _friendsList.get(position);
        }
    }



    //TODO: ESAME
    //Restaurants
    private ArrayList<RestaurantProfile> _restaurantsList;
    public ArrayList<RestaurantProfile> get_restaurantsList()
    {
        synchronized (_restaurantsList)
        {
            return _restaurantsList;
        }
    }
    public void updateRestaurantsList()
    {
        //call rest API
        List<RestaurantProfile> list = _rest.getRestaurantsCall();

        synchronized (_restaurantsList)
        {
            //empty list
            _restaurantsList.clear();

            //fill list
            for (RestaurantProfile r : list)
            {
                _restaurantsList.add(r);

                //log
                SystemHelper.logWarning(this.getClass(), String.format("Added %s", r.getName()));
            }
        }

    }

    private ArrayList<ListItemInterface> _mergedList;
    public ArrayList<ListItemInterface> mergeLists(ArrayList<AppFriend> friendsList, ArrayList<RestaurantProfile> restaurantsList)
    {
        ArrayList<ListItemInterface> list = new ArrayList<ListItemInterface>();

        synchronized (_friendsList)
        {
            list.addAll(friendsList);
        }

        synchronized (_restaurantsList)
        {
            list.addAll(restaurantsList);
        }

        //set list
        setMergedList(list);

        return list;
    }

    public ArrayList<ListItemInterface> getMergedList()
    {
        synchronized (_mergedList)
        {
            return _mergedList;
        }
    }

    public void setMergedList(ArrayList<ListItemInterface> list)
    {
        synchronized (list)
        {
            this._mergedList = list;
        }
    }

    public boolean adsAllowed = false;
}
