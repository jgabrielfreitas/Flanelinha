package br.com.manta.activity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import br.com.manta.mantaray.R;

public class CreditActivity extends Activity implements View.OnClickListener{

    TextView creditInformationsTextView;
    Button   closeDialogButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);

        if(getActionBar() != null) {
            getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2E64B6")));
        }

        instanceViews();

    }

    private void instanceViews() {
        creditInformationsTextView = (TextView) findViewById(R.id.creditInformationsTextView);
        closeDialogButton          = (Button) findViewById(R.id.closeDialogButton);

        creditInformationsTextView.setText(Html.fromHtml(getString(R.string.credits)));

        closeDialogButton.setOnClickListener(this);
    }

    public void onClick(View v) {
        finish();
    }
}
