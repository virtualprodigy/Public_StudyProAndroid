package com.virtualprodigy.studypro.Utils;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.virtualprodigy.studypro.About;
import com.virtualprodigy.studypro.ChangeLog;
import com.virtualprodigy.studypro.Tutorial;

public class settingmenubuttons extends ListActivity {

	String optButtons[] ={"Alerts","About","Tutorial","Send Feedback","Change Log"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setListAdapter(new ArrayAdapter<String>(settingmenubuttons.this,android.R.layout.simple_list_item_1 , optButtons));
	
	}


	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		if(position == 0){
			
		
		startActivity(new Intent(this, Prefs.class));
		}if(position == 1){
			
			
			startActivity(new Intent(this, About.class));
			}if(position == 2){
				
				
				startActivity(new Intent(this, Tutorial.class));
				}if(position==3){
				Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
				String emailAdd[]={"virtualprodigyllc@gmail.com"};
				emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, emailAdd);
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "CramSlam User FeedBack");
				emailIntent.setType("plain/text");
				startActivity(Intent.createChooser(emailIntent, "Send Email Via"));
				
				
			}if(position==4){
				Intent log = new Intent(this, ChangeLog.class);
				log.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				startActivity(new Intent(log));
				
				
			}
		
		
	}


}