package br.com.manta.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
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

public class CreditActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    ListView aboutListView;
    List<AboutItem> listAbout = new ArrayList<>();
    AboutAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_application);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        instanceViews();
    }

    public void instanceViews() {

        aboutListView = (ListView) findViewById(R.id.aboutListView);

        listAbout.add(new AboutItem(getResources().getDrawable(R.drawable.ic_image_timer_auto), getString(R.string.developer), getString(R.string.my_name)));
        listAbout.add(new AboutItem(getResources().getDrawable(R.drawable.ic_communication_email), getString(R.string.email), getString(R.string.my_email)));
        listAbout.add(new AboutItem(getResources().getDrawable(R.drawable.ic_twitter_logo), getString(R.string.twitter), getString(R.string.my_twitter_name)));
        listAbout.add(new AboutItem(getResources().getDrawable(R.drawable.ic_google_logo_06), getString(R.string.google_plus), getString(R.string.joao_gabriel_freitas)));
        listAbout.add(new AboutItem(getResources().getDrawable(R.drawable.ic_linkedin_logo_initials), getString(R.string.linkedin), getString(R.string.joao_gabriel_freitas)));

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

            case 1:

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.my_email)});
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.flanelinha_where_did_i_part));

                startActivity(Intent.createChooser(intent, getString(R.string.share_with)));
                break;

            case 2:

                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=JGabrielFreitas")));
                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/#!/JGabrielFreitas")));
                }
                break;

            case 3:

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/u/0/+Jo%C3%A3oGabrielDeAndradeFreitas/posts")));
                break;

            case 4:

                Intent intentLinkedin = new Intent(Intent.ACTION_VIEW, Uri.parse("linkedin://411286262"));
                final PackageManager packageManager = getPackageManager();
                final List<ResolveInfo> list = packageManager.queryIntentActivities(intentLinkedin, PackageManager.MATCH_DEFAULT_ONLY);
                if (list.isEmpty()) {
                    intentLinkedin = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.linkedin.com/profile/view?id=411286262"));
                }
                startActivity(intentLinkedin);
                break;

        }
    }
}
