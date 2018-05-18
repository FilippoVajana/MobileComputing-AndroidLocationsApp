package filippovajana.mcproject.model;

import java.util.ArrayList;

public class AppDataModel
{
    //singleton
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


    private ArrayList<AppFriend> _friendsList;

    //list operations
    private void buildModel(){}
    private void addItem(AppFriend item){}
    private void removeItem(AppFriend item){}
    public AppFriend getItem(int position)
    {
        return _friendsList.get(position);
    }
}
