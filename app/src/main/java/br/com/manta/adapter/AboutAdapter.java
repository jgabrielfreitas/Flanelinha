package br.com.manta.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.manta.mantaray.AboutItem;
import br.com.manta.mantaray.R;

/**
 * Created by JGabrielFreitas on 05/01/15.
 */
public class AboutAdapter extends BaseAdapter {

    List<AboutItem> listAboutItem;
    Context context;

    public AboutAdapter(List<AboutItem> listAboutItem, Context context) {
        this.listAboutItem = listAboutItem;
        this.context = context;
    }

    public int getCount() {
        return listAboutItem.size();
    }

    public Object getItem(int position) {
        return listAboutItem.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        AboutItem item = this.listAboutItem.get(position);

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = null;

        if (item.getImage() != null) {
            view = layoutInflater.inflate(R.layout.row_about_item_with_image, parent, false);

            ImageView image   = (ImageView) view.findViewById(R.id.about_image_view);
            TextView title    = (TextView)  view.findViewById(R.id.about_title);
            TextView subtitle = (TextView)  view.findViewById(R.id.about_sub_title);

            image.setImageDrawable(item.getImage());
            title.setText(item.getTitle());
            subtitle.setText(item.getSubText());

        } else {
            view = layoutInflater.inflate(R.layout.row_about_item_without_image, parent, false);

            TextView title    = (TextView) view.findViewById(R.id.about_without_title);
            TextView subtitle = (TextView) view.findViewById(R.id.about_without_sub_title);

            title.setText(item.getTitle());
            subtitle.setText(item.getSubText());

        }

        return view;
    }
}
