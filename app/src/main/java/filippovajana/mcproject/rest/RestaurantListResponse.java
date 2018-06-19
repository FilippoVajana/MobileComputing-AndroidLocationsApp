package filippovajana.mcproject.rest;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import filippovajana.mcproject.model.RestaurantProfile;

public class RestaurantListResponse
{
    @SerializedName("mrdonaldo")
    private List<RestaurantProfile> restaurantList;

    public List<RestaurantProfile> getRestaurantList()
    {
        return restaurantList;
    }

    public void setRestaurantList(List<RestaurantProfile> restaurantList)
    {
        this.restaurantList = restaurantList;
    }
}
