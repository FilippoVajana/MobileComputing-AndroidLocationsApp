package filippovajana.mcproject.model;

public class UserProfile
{
    String _username;
    String _stateMessage;
    GeoPosition _position;

    private static UserProfile _instance;
    public static UserProfile getInstance()
    {
        if (_instance == null)
            _instance = new UserProfile();
        return _instance;
    }
    private UserProfile()
    {
        _instance = this;
    }


    public String get_username()
    {
        return _username;
    }

    public void set_username(String _username)
    {
        this._username = _username;
    }

    public String get_stateMessage()
    {
        return _stateMessage;
    }

    public void set_stateMessage(String _stateMessage)
    {
        this._stateMessage = _stateMessage;
    }

    public GeoPosition get_position()
    {
        return _position;
    }

    public void set_position(GeoPosition _position)
    {
        this._position = _position;
    }
}
