package br.com.manta.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.manta.mantaray.MenuItem;
import br.com.manta.mantaray.R;

/**
 * Created by JGabrielFreitas on 02/01/2015 - 15:38.
 */
public class ItemAdapter extends BaseAdapter {

    List<MenuItem> listMenuItem;
    Context context;

    public ItemAdapter(List<MenuItem> listMenuItem, Context context) {
        this.listMenuItem = listMenuItem;
        this.context = context;
    }

    public int getCount() {
        return listMenuItem.size();
    }

    public Object getItem(int position) {
        return listMenuItem.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        MenuItem item = this.listMenuItem.get(position);

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.row_menu_item, parent, false);

        ImageView image    = (ImageView) view.findViewById(R.id.item_image_view);
        TextView title     = (TextView)  view.findViewById(R.id.item_title);
        TextView  subtitle = (TextView)  view.findViewById(R.id.item_sub_title);

        image.setImageDrawable(item.getImage());
        title.setText(item.getTitle());
        subtitle.setText(item.getSubText());

        return view;
    }
}
