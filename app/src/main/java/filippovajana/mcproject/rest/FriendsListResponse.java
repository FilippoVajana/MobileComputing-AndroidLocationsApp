package filippovajana.mcproject.rest;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import filippovajana.mcproject.model.AppFriend;

public class FriendsListResponse
{
    @SerializedName("followed")
    private List<AppFriend> friendsList;

    public List<AppFriend> getFriendsList()
    {
        return friendsList;
    }

    public void setFriendsList(List<AppFriend> friendsList)
    {
        this.friendsList = friendsList;
    }
}
