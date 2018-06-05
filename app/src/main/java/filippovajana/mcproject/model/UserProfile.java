package filippovajana.mcproject.model;

import com.google.gson.annotations.SerializedName;

public class UserProfile
{
    @SerializedName("username")
    String _username;
    @SerializedName("msg")
    String _stateMessage;
    @SerializedName("lat")
    float _latitude;
    @SerializedName("log")
    float _longitude;

    public UserProfile()
    {
        _username = "Marco Tullio Cicerone";
        _stateMessage = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam dui diam, semper a turpis eget, dictum fermentum risus. Praesent vel.";
        _latitude = 45.464211f;
        _longitude = 9.191383f;
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

    public float get_latitude()
    {
        return _latitude;
    }
    public void set_latitude(float _latitude)
    {
        this._latitude = _latitude;
    }

    public float get_longitude()
    {
        return _longitude;
    }
    public void set_longitude(float _longitude)
    {
        this._longitude = _longitude;
    }
}
