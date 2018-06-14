package filippovajana.mcproject.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import filippovajana.mcproject.R;
import filippovajana.mcproject.model.AppDataModel;
import filippovajana.mcproject.model.AppFriend;

public class AppFriendAdapter extends ArrayAdapter<AppFriend>
{
    private Context _context;
    private List<AppFriend> _friendsList;

    public AppFriendAdapter(@NonNull Context context, @NonNull List<AppFriend> list)
    {
        super(context, 0, list);

        _context = context;
        _friendsList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(_context).inflate(R.layout.friend_list_item,parent,false);

        //get item
        AppDataModel model = AppDataModel.getInstance();
        AppFriend friend = model.getItem(position);

        if (friend == null)
            return listItem;

        //set name
        TextView nameText = (TextView) listItem.findViewById(R.id.textView_name);
        nameText.setText(friend.getUsername());

        //set distance
        float distanceKm = friend.getDistanceToUser() / 1000;
        TextView distanceText = (TextView) listItem.findViewById(R.id.textView_distance);
        distanceText.setText(String.format("%.1f Km", distanceKm));

        //set message
        TextView messageText = (TextView) listItem.findViewById(R.id.textView_message);
        messageText.setText(friend.getMessage());

        return listItem;
    }
}
