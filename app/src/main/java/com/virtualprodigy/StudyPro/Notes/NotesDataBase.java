
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
package com.virtualprodigy.studypro.Notes;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.virtualprodigy.studypro.Models.Notes.Note;

import java.util.Formatter;
import java.util.Locale;


public class NotesDataBase {
    private final String TAG = this.getClass().getSimpleName();
    private SQLiteDBHelper dbHelper;
    private SQLiteDatabase database;

    private static final String dbName = "studyProDatabase";
    private static final String DatBassTable = "_NotesTable";
    private static final int DATABASE_VERSION = 1;
    private final Context context;

    public static final String KEY_NOTE_ID = "_id";
    public static final String KEY_NOTE_TITLE = "_title";
    public static final String KEY_NOTE_BODY = "_note";
    public static final String KEY_NOTE_DATE_TIME = "_dateTime";

    private class SQLiteDBHelper extends SQLiteOpenHelper {

        SQLiteDBHelper(Context context) {
            super(context, dbName, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            //create the Notes Table.
            //TODO (I won't be using this class but as a note) check api and see how sqlite stores longs, I see the cursor can get a long, make sure the create is typed correctly
            String createNoteTable = "create table %s (%s integer primary key autoincrement, %s text not null, %s text not null %s long not null);";
            String formattedCreateTable = String.format(Locale.US, createNoteTable, KEY_NOTE_ID, KEY_NOTE_TITLE, KEY_NOTE_BODY, KEY_NOTE_DATE_TIME);
            sqLiteDatabase.execSQL(formattedCreateTable);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int old, int newV) {
            Log.d(TAG, "DB upgrading ");
            //nothing to upgrade at this time
        }

    }

    public NotesDataBase(Context context) {
        this.context = context;
    }

    public NotesDataBase open() throws SQLException {
        dbHelper = new SQLiteDBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    /**
     * Creates a new note in the Database
     * @param name
     * @param body
     * @param dateTime
     * @return
     */
    public long createNote(String name, String body, long dateTime) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NOTE_TITLE, name);
        contentValues.put(KEY_NOTE_BODY, body);
        contentValues.put(KEY_NOTE_DATE_TIME, dateTime);
        return database.insert(DatBassTable, null, contentValues);
    }

    /**
     * Deletes a note in the database
     * @param rowID - row of the note to be deleted
     * @return
     */
    public boolean deleteNote(long rowID) {
        return database.delete(DatBassTable, KEY_NOTE_ID + "=" + rowID, null) > 0;
    }

    /**
     * Feteches a cursor of all the notes in the database
     * @return
     */
    public Cursor fetchAllNotes() {
        return database.query(DatBassTable, new String[]{KEY_NOTE_ID, KEY_NOTE_TITLE, KEY_NOTE_BODY}, null, null, null, null, null);
    }


    /**
     * Fetches the specific note at the given rowid
     * @param rowID - the rowID of the note
     * @return
     * @throws SQLException
     */
    public Cursor fetchNote(long rowID) throws SQLException {
        Cursor cursor = database.query(true, DatBassTable, new String[]{KEY_NOTE_ID, KEY_NOTE_TITLE, KEY_NOTE_BODY, KEY_NOTE_DATE_TIME}, KEY_NOTE_ID + "=" + rowID, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    /**
     * Returns a note from the db
     * @return
     * @throws SQLException
     */
    public Note getNote(long rowID) throws SQLException {
        Cursor cursor = database.query(true, DatBassTable, new String[]{KEY_NOTE_ID, KEY_NOTE_TITLE, KEY_NOTE_BODY, KEY_NOTE_DATE_TIME}, KEY_NOTE_ID + "=" + rowID, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        Note note = new Note();
        note.setId(cursor.getInt(cursor.getColumnIndex(KEY_NOTE_ID)));
        note.setTitle(cursor.getString(cursor.getColumnIndex(KEY_NOTE_TITLE)));
        note.setNote(cursor.getString(cursor.getColumnIndex(KEY_NOTE_BODY)));
        note.setDateTime(cursor.getLong(cursor.getColumnIndex(KEY_NOTE_DATE_TIME)));
        return note;
    }

    /**
     * Updates a note in the database
     * @param rowID - id of the note to update
     * @param name - the edited name
     * @param note - the edited body of the note
     * @return
     */
    public boolean updateNote(long rowID, String name, String note) {
        ContentValues info = new ContentValues();
        info.put(KEY_NOTE_TITLE, name);
        info.put(KEY_NOTE_BODY, note);
        return database.update(DatBassTable, info, KEY_NOTE_ID + "=" + rowID, null) > 0;
    }


}
