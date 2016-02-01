
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


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class NotesDataBase {
 
	/* public static final String Note_TITLE = "noteTitle"; //part a pass what bout b, b must say title WTF again...at this point I assume the same for the body and _id labels
	 public static final String Note = "notee"; //these dont work must need other form for sql to work
	 public static final String NoteID = "Noteid";
	*/
	  public static final String Note_TITLE = "title";
	    public static final String Note = "body";
	    public static final String NoteID = "_id";
	    
	/* private static final String Iden = "notesLinkCable";//Iden is safe so its the link cable statement  */
	    private static final String  Iden = "notesLinkCable";
	 private ACTNotesDBBuddy DatBuddy;//dbhelper was a coni
	 private SQLiteDatabase ACTNotesDB;

	 private static final String DATABASE_CREATE =
	        "create table notes (_id integer primary key autoincrement, "
	        + "title text not null, body text not null);";//this line just seems pointless to me two lines of "" pstt

	 
/*	 private static final String DBname = "DatBass";//name pass wat abo "" must be lower case wtf
	 private static final String DatBassTable = "table";//cant use the name table WTF on me
	 private static final int DatBassVER = 2;//works for name
	 */
	    private static final String DBname = "datbass";
	    private static final String DatBassTable = "notes";//for some reason has to be named notes??
	    private static final int DatBassVER = 2;
	 private final Context coni;
	 
	
	 
 private static class ACTNotesDBBuddy extends SQLiteOpenHelper {
	 
		 ACTNotesDBBuddy(Context context){
			 super(context, DBname, null, DatBassVER);
		 }
	 
	 @Override//1 organizing in order as other did wasnt need either, it was a labeling error
	 public void onCreate(SQLiteDatabase sld){
		 sld.execSQL(DATABASE_CREATE);//so the data has so bs stuff pushed in it already lol
	 }
		 
	 @Override//2
	 public void onUpgrade(SQLiteDatabase slld, int old, int newV){
		// Log.w(Iden, "I guess I'm upgrading sdl from ver" + old + " to " + newV +
		//		 	" ps I hear this will kill off the old king and knights saved");//this is a logCat tag I believe anyway this kills old data for an update...wont be doing that
		   
            slld.execSQL("kill the king" + Note_TITLE);//this statement wont do shit but throw an sql error lmao
		 onCreate(slld);
	 }
		 
	 }
	 //3
	 public NotesDataBase(Context con){
		 this.coni=con;
	 }
	 //4
	 public NotesDataBase open() throws SQLException{
		 DatBuddy = new ACTNotesDBBuddy(coni);
		 ACTNotesDB =DatBuddy.getWritableDatabase();
		 return this; 
	 }
	 //5
	 public void close(){
		 DatBuddy.close();
	 }
	  //6 a 10
	 public long createNote( String name, String body){
		 ContentValues Orgin = new ContentValues();
		 Orgin.put(Note_TITLE, name);
		 Orgin.put(Note,body);//my intial thought is this will over right each other but i believe its kinda like a stackish thing :)
	    
		 return ACTNotesDB.insert(DatBassTable ,null,Orgin);
	 }
	 
	 //7a6
	 public boolean destroyerNote(long locoRow){
		return ACTNotesDB.delete(DatBassTable,NoteID + "=" + locoRow,null)>0; 
	 }
	 //8
	 public Cursor fetchAllNotes(){
		 return ACTNotesDB.query(DatBassTable, new String []{ NoteID,Note_TITLE,Note}, null, null, null, null, null);
		 }
	 
	 

	 //9a7
	 public Cursor fetchNote(long rid) throws SQLException{
		 Cursor cursor = ACTNotesDB.query(true, DatBassTable, new String [] {NoteID,Note_TITLE,Note}, NoteID +"="+rid, null, null, null, null, null);
		 
		 if(cursor != null){
			 cursor.moveToFirst();
		 }
		 return cursor;
	 }
	 //10
	  public boolean updateNote(long loco, String name, String note) {
	        ContentValues info = new ContentValues();
	        info.put(Note_TITLE, name);
	        info.put(Note, note);
	        return ACTNotesDB.update(DatBassTable, info, NoteID + "=" + loco, null) > 0;
	    }
	
	 

	 
}
