package filippovajana.mcproject.model;

import java.util.ArrayList;
import java.util.List;

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
            updateFriendsList();
            return _friendsList;
        }
    }

    private void updateFriendsList()
    {
        //call rest API
        RESTService service = new RESTService();
        List<AppFriend> list = service.getFriendsList();

        //empty list
        _friendsList.removeAll(new ArrayList<>(_friendsList));

        //fill list
        for (AppFriend f : list)
        {
            _friendsList.add(f);
        }

    }

    private void addItem(AppFriend item)
    {}
    private void removeItem(AppFriend item)
    {}
    public AppFriend getItem(int position)
    {
        return null;
    }


}
