package filippovajana.mcproject.rest;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UsersListRespose
{
    @SerializedName("usernames")
    private List<String> usersList;

    public List<String> getUsersList()
    {
        return usersList;
    }

    public void setUsersList(List<String> usersList)
    {
        this.usersList = usersList;
    }
}
