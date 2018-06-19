package filippovajana.mcproject.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class AppFriend implements ListItemInterface
{
    @SerializedName("username")
    private String username;

    @SerializedName("msg")
    @Nullable
    private String message;

    @SerializedName("lat")
    private float latitude;

    @SerializedName("lon")
    private float longitude;

    private float _distanceToUser;



    public float getDistanceToUser()
    {
        return _distanceToUser;
    }

    public void setDistanceToUser(float _distanceToUser)
    {
        this._distanceToUser = _distanceToUser;
    }

    public String getName()
    {
        return username;
    }

    public void setName(String username)
    {
        this.username = username;
    }

    public String getMessage()
    {
        if (message == null)
            message = "";
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public float getLatitude()
    {
        return latitude;
    }

    public void setLatitude(float latitude)
    {
        this.latitude = latitude;
    }

    public float getLongitude()
    {
        return longitude;
    }

    public void setLongitude(float longitude)
    {
        this.longitude = longitude;
    }

    @Override
    public boolean isUser()
    {
        return true;
    }
}
