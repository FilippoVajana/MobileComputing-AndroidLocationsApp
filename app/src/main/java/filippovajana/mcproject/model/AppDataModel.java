package filippovajana.mcproject.model;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import filippovajana.mcproject.rest.RESTService;

public class AppDataModel
{
    private ArrayList<AppFriend> _friendsList;

    //SINGLETON
    private static AppDataModel _instance;
    private AppDataModel()
    {
        _friendsList = new ArrayList<>();
    }

    public static AppDataModel getInstance()
    {
        if (_instance == null)
            _instance = new AppDataModel();
        return _instance;
    }



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
        RESTService rest = new RESTService();
        List<AppFriend> list = rest.getFriendsList();

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
