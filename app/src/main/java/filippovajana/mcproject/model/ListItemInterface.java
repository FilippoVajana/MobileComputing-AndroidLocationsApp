package filippovajana.mcproject.model;

public interface ListItemInterface
{
    public float getLatitude();
    //public void setLatitude(float lat);

    public float getLongitude();
    //public void setLongitude(float lon);

    public String getName();
    //public void setName(String name);

    public String getMessage();
    //public void setMessage(String message);

    public float getDistanceToUser();
    public void setDistanceToUser(float distance);


    public boolean isUser();
}
