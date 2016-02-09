package com.virtualprodigy.studypro.Notes;

import com.j256.ormlite.dao.Dao;
import com.virtualprodigy.studypro.Database.OrmHelper;
import com.virtualprodigy.studypro.Models.Notes.Note;
import com.virtualprodigy.studypro.R;
import com.virtualprodigy.studypro.StudyProApplication;

import android.app.Activity;
import android.os.Bundle;

import android.util.Log;
import android.widget.EditText;

import java.sql.SQLException;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NoteEditor extends Activity {
    private final String TAG = this.getClass().getSimpleName();
    public static final String KEY_NOTE_PARCEL = "note_parcel";
    public static final String KEY_NOTE_BUNDLE = "note_bundle";

    private Note note;

    @Bind(R.id.title)
    EditText noteTitle;
    @Bind(R.id.body)
    NotepadEditText noteBody;
   
    @Inject
    OrmHelper ormHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editor);
        ((StudyProApplication) this.getApplication()).getComponent().inject(this);
        ButterKnife.bind(this);
        
        setTitle(R.string.note_editor_activity_title);

    }

    /**
     * This method either populates the note from the bundle or sets up new note
     */
    private void populateNote() {
        if(getIntent().hasExtra(KEY_NOTE_BUNDLE)) {
            Bundle bundle = getIntent().getBundleExtra(KEY_NOTE_BUNDLE);
            note = (Note) bundle.getParcelable(KEY_NOTE_PARCEL);

            noteTitle.setText(note.getTitle());
            noteBody.setText(note.getNote());
        }else{
            note = new Note();
            note.setDateTime(System.currentTimeMillis());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        populateNote();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Saves or updates the note in the database
     */
    private void saveNote(){
        try {
            Dao<Note, Integer> noteDao = ormHelper.getTypeDao(Note.class, Integer.class);
            noteDao.createOrUpdate(note);
        } catch (SQLException e) {
            Log.e(TAG, "Failed to create/update the note", e);
        }
    }

}
    



