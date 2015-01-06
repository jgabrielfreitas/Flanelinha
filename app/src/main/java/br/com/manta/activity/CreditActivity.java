package br.com.manta.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import br.com.manta.mantaray.R;

public class CreditActivity extends ActionBarActivity {

    TextView creditInformationsTextView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);

        instanceViews();
    }

    private void instanceViews() {
        creditInformationsTextView = (TextView) findViewById(R.id.creditInformationsTextView);

        creditInformationsTextView.setText(Html.fromHtml(getString(R.string.credits)));
    }

}
