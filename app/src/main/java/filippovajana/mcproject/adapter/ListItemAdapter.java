package filippovajana.mcproject.adapter;

import android.content.Context;
import android.graphics.Color;
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
import filippovajana.mcproject.model.ListItemInterface;

public class ListItemAdapter extends ArrayAdapter<ListItemInterface>
{
    private Context _context;
    private List<ListItemInterface> _itemsList;

    public ListItemAdapter(@NonNull Context context, @NonNull List<ListItemInterface> list)
    {
        super(context, 0, list);

        _context = context;
        _itemsList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(_context).inflate(R.layout.user_info_list_item,parent,false);

        //get item
        AppDataModel _model = AppDataModel.getInstance();
        ListItemInterface item = _model.getMergedList().get(position);

        if (item == null)
            return listItem;

        //set name
        TextView nameText = (TextView) listItem.findViewById(R.id.textView_name);
        nameText.setText(item.getName());

        //set distance
        float distanceKm = item.getDistanceToUser() / 1000;
        TextView distanceText = (TextView) listItem.findViewById(R.id.textView_distance);
        distanceText.setText(String.format("%.1f Km", distanceKm));

        //set message
        TextView messageText = (TextView) listItem.findViewById(R.id.textView_message);
        if (item.isUser())
        {
            messageText.setText(item.getMessage());
        }
        else
        {
            messageText.setText(String.format("solo per oggi sconto del %s%%", item.getMessage()));
        }

        //set background
        if (item.isUser() == false)
        {
            //change background color
            listItem.setBackgroundColor(R.style.AppTheme); //TODO:check
        }

        return listItem;
    }
}
