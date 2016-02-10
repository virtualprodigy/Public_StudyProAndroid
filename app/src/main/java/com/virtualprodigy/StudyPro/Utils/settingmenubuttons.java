package com.virtualprodigy.studypro.Utils;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.virtualprodigy.studypro.About;
import com.virtualprodigy.studypro.ChangeLog;

public class settingmenubuttons extends ListActivity {

    String optButtons[] = {"Alerts", "About", "Send Feedback", "Change Log"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setListAdapter(new ArrayAdapter<String>(settingmenubuttons.this, android.R.layout.simple_list_item_1, optButtons));

    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        switch (position) {
            case 0:
                startActivity(new Intent(this, Prefs.class));
                break;

            case 1:
                startActivity(new Intent(this, About.class));
                break;

            case 2:
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                String emailAdd[] = {"virtualprodigyllc@gmail.com"};
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, emailAdd);
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "CramSlam User FeedBack");
                emailIntent.setType("plain/text");
                startActivity(Intent.createChooser(emailIntent, "Send Email Via"));
                break;

            case 3:
                Intent log = new Intent(this, ChangeLog.class);
                log.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(new Intent(log));
                break;
        }
    }


}

