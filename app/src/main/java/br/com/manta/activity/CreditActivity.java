package br.com.manta.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.util.SparseArray;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import br.com.manta.adapter.ExpandableListAdapter;
import br.com.manta.credit.Contributor;
import br.com.manta.credit.Link;
import br.com.manta.mantaray.R;

public class CreditActivity extends ActionBarActivity {

    SparseArray<Contributor> groups = new SparseArray<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);

        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        createData();
        ExpandableListView listView = (ExpandableListView) findViewById(R.id.creditExpandableListView);
        ExpandableListAdapter adapter = new ExpandableListAdapter(this,groups);
        listView.setAdapter(adapter);
    }

    public void createData() {

        // create contributors
        Contributor design_and_code = new Contributor(getString(R.string.jgabrielfreitas));
        design_and_code.children.add(new Link(null, null, getString(R.string.design_and_code)));
        design_and_code.children.add(new Link(getResources().getDrawable(R.drawable.google_plus), "https://plus.google.com/u/0/+Jo%C3%A3oGabrielDeAndradeFreitas/posts", "+ João Gabriel"));
        design_and_code.children.add(new Link(getResources().getDrawable(R.drawable.github_logo), "https://github.com/jgabrielfreitas", getString(R.string.github)));

        Contributor data_base = new Contributor(getString(R.string.cristian_danner));
        data_base.children.add(new Link(null, null, getString(R.string.data_base)));
        data_base.children.add(new Link(getResources().getDrawable(R.drawable.google_plus), "https://plus.google.com/100896323147006054393/posts", "+ Cristian Danner"));
        data_base.children.add(new Link(getResources().getDrawable(R.drawable.github_logo), "https://github.com/cristiandanner", getString(R.string.github)));

        Contributor router_lib = new Contributor(getString(R.string.google_lib_router_creator));
        router_lib.children.add(new Link(null, null, getString(R.string.router_lib)));
        router_lib.children.add(new Link(getResources().getDrawable(R.drawable.github_logo), "https://github.com/jd-alexander/Google-Directions-Android", getString(R.string.github)));

        // add and show contributors
        groups.append(0, design_and_code);
        groups.append(1, data_base);
        groups.append(2, router_lib);
    }

    // add arrow in action bar to back on activity
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(this).addNextIntentWithParentStack(upIntent) // Add all of this activity's parents to the back stack
                            .startActivities();// Navigate up to the closest parent
                } else {
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
