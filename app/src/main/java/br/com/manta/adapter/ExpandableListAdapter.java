package br.com.manta.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.manta.credit.Contributor;
import br.com.manta.credit.Link;
import br.com.manta.mantaray.R;

/**
 * Created by JGabrielFreitas on 12/01/2015 - 19:45.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    // TODO delete this class and create my own class

    private final SparseArray<Contributor> groups;
    public LayoutInflater inflater;
    public Activity activity;

    public ExpandableListAdapter(Activity act, SparseArray<Contributor> groups) {
        activity = act;
        this.groups = groups;
        inflater = act.getLayoutInflater();
    }

    public Object getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).children.get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final Link children = (Link) getChild(groupPosition, childPosition);
        ImageView image = null;
        TextView text   = null;

        if (convertView == null)
            convertView = inflater.inflate(R.layout.listrow_details, null);

        text = (TextView) convertView.findViewById(R.id.details_name_text_view);
        text.setText(children.getLinkTitle());

        image = (ImageView) convertView.findViewById(R.id.link_type_image_view);
        if(image != null)
            image.setImageDrawable(children.getImage());

        convertView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // open link where
                if(children.getLink() != null) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(children.getLink()));
                    activity.startActivity(browserIntent);
                }
            }
        });

        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).children.size();
    }

    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    public int getGroupCount() {
        return groups.size();
    }

    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    public long getGroupId(int groupPosition) {
        return 0;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null)
            convertView = inflater.inflate(R.layout.listrow_group, null);

        Contributor group = (Contributor) getGroup(groupPosition);
        ((CheckedTextView) convertView).setText(group.string);
        ((CheckedTextView) convertView).setChecked(isExpanded);

        return convertView;
    }

    public boolean hasStableIds() {
        return false;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
