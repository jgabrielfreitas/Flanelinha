package br.com.manta.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import br.com.manta.mantaray.CheckinActivity;
import br.com.manta.mantaray.R;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    ListView listViewMenu;
    List<String> optionsArray = new ArrayList<>();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instanceViews();
    }

    private void instanceViews() {

        listViewMenu = (ListView) findViewById(R.id.mainActivityMenuListView);

        optionsArray.add("Realizar check-in");
        optionsArray.add("Realizar busca");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MainActivity.this,
                                                               android.R.layout.simple_list_item_1,
                                                               optionsArray);
        listViewMenu.setAdapter(arrayAdapter);
        listViewMenu.setOnItemClickListener(this);
    }

    private void doIntent(Class mClass){
        Intent intent = new Intent(this, mClass);
        startActivity(intent);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        switch (position){

            case 0:
                doIntent(CheckinActivity.class);
                break;
            case 1:

                break;
        }
    }
}
