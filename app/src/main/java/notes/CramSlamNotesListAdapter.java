
/*Prominent notice I have changed the orginal work from the adroid develepers site for my project(2.You must cause any modified files to carry prominent notices stating that You changed the files; and)
 * 
 * 
 * In order to comply with the creative commons license which is confusing cuz I think it means I have to
 *  comply with this apache license which kinda seems like it says comply with the creative commons 2.5 well any way
 *  i added the copy right onto my notepad file in my project. As well as I saved the orginal work and I have to add in my about me that gerneral statement
 *  along with hyperlinks to the orginal project and the creative commons license + apache lincese + says googlge...well Android developers
 *  in no way indorses me. Fuck why don't they have like a service you can contact with questions and they'll send you exact to your inssue instuctions
 *  id gladly pay 50 bucks for that anyway i think i got this down pack I read throught hat license all like 20 webpages of each and im pretty sure thats all i gotta do*/
/*
 * Copyright (C) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package notes;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.CramSlam.VirtualProdigy.R;
import com.CramSlam.VirtualProdigy.settingmenubuttons;

public class CramSlamNotesListAdapter extends ListActivity implements OnClickListener {
	/** Called when the activity is first created. */
	
	private NotesDataBase notesDB;
	
	private static final int add = Menu.FIRST;
	private static final int delete = Menu.FIRST + 1;
	
	private static final int create = 0;
	private static final int edit = 1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainnote);
		notesDB = new NotesDataBase(this);
		notesDB.open();
		AddData();// create method to add data
		 
		View CreateNoteButton = findViewById(R.id.createCramNote);
		  CreateNoteButton.setOnClickListener(this);//need for one click and must be where the button is made lulz
		  registerForContextMenu(getListView());
		  
		  Button info;
			
			info = (Button)findViewById(R.id.infobuttonNotepage);
			info.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					startActivity(new Intent(getApplicationContext(), settingmenubuttons.class));
					
				}
			});
		  
	}
	@Override
	public void onCreateContextMenu(ContextMenu m, View v, ContextMenuInfo mInfo) {
	    super.onCreateContextMenu(m, v, mInfo);
	    m.add(0, delete, 0, R.string.delete);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.settings:
			startActivity(new Intent(getApplicationContext(), settingmenubuttons.class));
			//startActivity(new Intent(this, Prefs.class));
			return true;
		}

		return false;
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	    switch(item.getItemId()) {
	    case delete:
	        AdapterContextMenuInfo info =
	        	(AdapterContextMenuInfo) item.getMenuInfo();
	        notesDB.destroyerNote(info.id);
	        AddData();
	        return true;
	    }
	    return super.onContextItemSelected(item);
	}
 
	 public void onClick(View vi){
	    	switch(vi.getId()){
	    	case R.id.createCramNote: 
	  // EditText e =(EditText)findViewById(R.id.title);
	    //e.setText("Cram Note"+ numNotes++);
	    				startNoteActivity();
	    		 break;
	    	default:
	    		break;
	    	
	    
	    	
	    	}
	    }
	

	private void startNoteActivity() {
		
		Intent intent = new Intent(this, Modify.class);
		intent.putExtra("requestTitle", create);
		startActivityForResult(intent, create);
	}

	private void AddData() {

		Cursor filler = notesDB.fetchAllNotes();
		startManagingCursor(filler);

		String[] beginning = new String[] { NotesDataBase.Note_TITLE };
		int[] end = new int[] { R.id.notenom };

		SimpleCursorAdapter list = new SimpleCursorAdapter(this,
				R.layout.noterowveiw, filler, beginning, end);
		setListAdapter(list);

	}
	
	@Override
 protected void onListItemClick(ListView l,View v,int position,long id){//luckly the following line picked idenifiers I'm running out of fun names...catPISSS
	//idk why it bitched about being public...but I dont wanna make it private, I'm pretty sure it needs to talk to random methods and private would make it diffcult... becoming a good programmer is hard
		super.onListItemClick( l, v, position, id);
		Intent i = new Intent(this, Modify.class);
		i.putExtra(NotesDataBase.NoteID, id);
		startActivityForResult(i,edit);
		
	}
	
	//ok this onresult crap is something about sharing services...I half know what I'm doing and half dont. thanks to not having any programming buddies or mentors....fuck my life this is tough but fun. i can wait till I finiish cramslam
	@Override//why this @Override anyway... I dont remember learning this in class n im sure its android only... fuck i need money for more books
	protected void onActivityResult(int requestCode, int resultCode,Intent intent){
	super.onActivityResult(requestCode,resultCode,intent);
	AddData();}
	
}

