package br.com.manta.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.SparseArray;
import android.widget.ExpandableListView;

import br.com.manta.adapter.MyExpandableListAdapter;
import br.com.manta.credit.Group;
import br.com.manta.mantaray.R;

public class CreditActivity extends ActionBarActivity {

    SparseArray<Group> groups = new SparseArray<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);

        createData();
        ExpandableListView listView = (ExpandableListView) findViewById(R.id.creditExpandableListView);
        MyExpandableListAdapter adapter = new MyExpandableListAdapter(this,groups);
        listView.setAdapter(adapter);
        

    }

    public void createData() {
        for (int j = 0; j != 3; j++) {
            Group group = new Group("Test " + j);
            for (int i = 0; i < 99; i++) {
                group.children.add("Sub Item" + i);
            }
            groups.append(j, group);
        }
    }

}
