package br.com.manta.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import br.com.manta.adapter.AboutAdapter;
import br.com.manta.mantaray.AboutItem;
import br.com.manta.mantaray.R;
import br.com.manta.mantaray.Utils;

public class AboutApplicationActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    ListView        aboutListView;
    List<AboutItem> listAbout = new ArrayList<>();
    AboutAdapter    adapter;
    String          versionName = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_application);

        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        instanceViews();
    }

    private void instanceViews() {
        aboutListView = (ListView) findViewById(R.id.aboutListView);

        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (Exception e) {}

        listAbout.add(new AboutItem(getResources().getDrawable(R.drawable.github_logo), getString(R.string.open_source), getString(R.string.open_in_github)));
        listAbout.add(new AboutItem(getResources().getDrawable(R.drawable.ic_credit), getString(R.string.title_activity_credit), getString(R.string.credit_details)));
        listAbout.add(new AboutItem(getResources().getDrawable(R.drawable.ic_social_share), getString(R.string.do_you_like_this_app), getString(R.string.share_to_your_friends)));
        listAbout.add(new AboutItem(getResources().getDrawable(R.drawable.ic_action_info_outline), getString(R.string.application_version),  versionName));


        adapter = new AboutAdapter(listAbout, getApplicationContext());
        aboutListView.setAdapter(adapter);
        aboutListView.setOnItemClickListener(this);
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

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        switch (position) {

            case 0:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Utils.URL_IN_GITHUB));
                startActivity(browserIntent);
                break;
            case 1:
                Intent intent = new Intent(this, CreditActivity.class);
                startActivity(intent);
                break;

            case 2:

                Intent intentShare = new Intent(Intent.ACTION_SEND);
                intentShare.setType("text/plain");
                intentShare.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app));

                startActivity(Intent.createChooser(intentShare, getString(R.string.share_using)));
                break;


        }
    }
}
