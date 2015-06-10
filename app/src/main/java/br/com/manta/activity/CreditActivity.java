package br.com.manta.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import br.com.manta.mantaray.R;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;

public class CreditActivity extends ActionBarActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);

        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        createData();
    }

    public void createData() {

        /**
         * look this
         * https://github.com/gabrielemariotti/cardslib/blob/master/doc/GUIDE.md
         * */

        //Create a Card
        Card card = new Card(this);

        //Create a CardHeader
        CardHeader header = new CardHeader(this);

        //Add Header to card
        card.addCardHeader(header);

        //Set card in the cardView
//        CardViewNative cardView = (CardViewNative) findViewById(R.id.carddemo);
//        cardView.setCard(card);

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
