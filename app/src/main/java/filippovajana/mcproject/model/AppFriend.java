package filippovajana.mcproject.model;

import com.google.gson.annotations.SerializedName;

public class AppFriend
{
    @SerializedName("username")
    private String username;

    @SerializedName("msg")
    private String message;

    @SerializedName("lat")
    private float latitude;

    @SerializedName("lon")
    private float longitude;

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getMessage()
    {
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
}
