package filippovajana.mcproject.model;

import com.google.gson.annotations.SerializedName;

public class RestaurantProfile implements ListItemInterface
{
    @SerializedName("name")
    private String name;

    @SerializedName("discount")
    private float discount;

    @SerializedName("lat")
    private float latitude;

    @SerializedName("lon")
    private float longitude;

    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }


    public String getMessage()
    {
        return String.valueOf(discount);
    }
    public void setMessage(float discount)
    {
        this.discount = discount;
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
        return false;
    }
}
