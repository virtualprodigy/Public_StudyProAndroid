
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


import com.virtualprodigy.studypro.R;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Modify extends Activity {
	private static final String TAG = "NOTEPAD";
	EditText modTitle;
	EditText modBody;
	Long RowId;
	static int numNotes = 1;
	static int i;
	boolean newNamed;
	static boolean stateChangename;
	static int stateChangei;
	private NotesDataBase accessor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		accessor = new NotesDataBase(this);
		accessor.open();
		setContentView(R.layout.editor);
		setTitle(R.string.modify_note);
		newNamed = false;
		
		 modTitle = (EditText) findViewById(R.id.title);
		 
		 modBody = (EditText) findViewById(R.id.body);
		Button confirmButton = (Button) findViewById(R.id.confirm);
		//wow right here is a dozy
		RowId = (savedInstanceState == null) ? null:
			(Long) savedInstanceState.getSerializable(NotesDataBase.NoteID);
		
		
		
		int defaultName = getIntent().getIntExtra("requestTitle", 3);//getStringExtra("requestTitle") ;getes string values//gets the  extra data saying this is a create state note so i can default a title
		
		getIntent().removeExtra("requestTitle");//this then removes the item from the extras to prevent crashing do to note creation logic errors
		
		if(RowId == null){
			
			Bundle extras = getIntent().getExtras();
			RowId = extras != null ? extras.getLong(NotesDataBase.NoteID)
					: null;
						
			}
		
		
		populateSavedNote();
		 confirmButton.setOnClickListener(new View.OnClickListener() {
			
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setResult(RESULT_OK);
				finish();
			}
		});
		 
		 
		 
		 
		 
		if(defaultName == 0){
			//	Log.d(TAG,"new note again");
				modTitle.setText("CramSlam Note " + numNotes++);
				i = numNotes -1 ;
				newNamed = true;
			}
		
		
		}
	private void populateSavedNote(){
		
		if(RowId != null){
			Cursor c = accessor.fetchNote(RowId);//they named the cursor 
			//note this time instead of 
			//c or cursor most likly because this will fill out the note and 
			//leave the cursor after the finish reproduced note
			startManagingCursor(c);
			modTitle.setText(c.getString(c.getColumnIndexOrThrow(NotesDataBase.Note_TITLE)));//this had me stumped for a week, the title kept changeing cuz i had this set to noteid not notetitle
			modBody.setText(c.getString(c.getColumnIndexOrThrow(NotesDataBase.Note)));
		}
	}
	@Override
	protected void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
		saveState();
		outState.putSerializable(NotesDataBase.NoteID, RowId);
	}
	@Override
	protected void onPause(){
		super.onPause();
		saveState();
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		populateSavedNote();
	}
	@Override
	protected void onStop(){
		super.onStop();
		
	}
	@Override
	protected void onDestroy(){
		super.onDestroy();
		
	}

	private void saveState(){
		
		String title = modTitle.getText().toString();
		String body = modBody.getText().toString();
		
		
		//Log.d(TAG,"title"+title+"body"+body.length()+"  !! i value  "+i+ stateChangename);
		
		if(stateChangename == true ){
			
			
		//	Log.d(TAG,"???????????State"+stateChangename+ " the name "+"CramSlam Note " + numNotes + "title"+title);
			if(title.equals("CramSlam Note " + numNotes )){
				
			//	Log.d(TAG,"########State"+"CramSlam Note " + numNotes+ "title"+title);
				if(body.length()!=0){
					numNotes = numNotes +1;
				//	Log.d(TAG,"***** numNotes is ++"+numNotes);
				}
				
			}
			
		}
		if(title.equals("CramSlam Note " + i)&& body.length()==0){//make sure the change of the value of numNotes doesnt fuck up other notes
		//	Log.d(TAG,"COWS");
			title = null;
			body = null;
			if(newNamed == true){
				stateChangei = numNotes;
				numNotes = numNotes - 1;
			newNamed = false;
			stateChangename = true;
			
		//	Log.d(TAG,"!!!!!!!!RUN"+stateChangename+" "+ numNotes);
			}
			return;
		}else{
			
			
	
	//	Log.d(TAG,"IS this happening");
		if(RowId == null){
			long id = accessor.createNote(title, body);
			if(id>0){
			//	Log.d(TAG,"well whats the row id"+RowId+"whats id"+id);
				RowId = id;
			}
		}else{
		//	Log.d(TAG,"when run"+RowId);
			accessor.updateNote(RowId, title, body);
			}
		}}
	
	}
	



