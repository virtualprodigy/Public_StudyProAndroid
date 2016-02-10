package com.virtualprodigy.studypro.Notes;

import com.j256.ormlite.dao.Dao;
import com.virtualprodigy.studypro.Adapters.NotesImageAdapter;
import com.virtualprodigy.studypro.Database.OrmHelper;
import com.virtualprodigy.studypro.Models.Notes.Note;
import com.virtualprodigy.studypro.Models.Notes.NoteImageLocation;
import com.virtualprodigy.studypro.R;
import com.virtualprodigy.studypro.StudyProApplication;

import android.content.Context;
import android.os.Bundle;

import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NoteEditor extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    public static final String KEY_NOTE_PARCEL = "note_parcel";
    public static final String KEY_NOTE_BUNDLE = "note_bundle";

    private Note note;
    private Context context;

    protected
    @Bind(R.id.title)
    EditText noteTitle;
    protected
    @Bind(R.id.body)
    NotepadEditText noteBody;
    protected
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    protected
    @Bind(R.id.noteImagesRecyclerView)
    RecyclerView imageRecyclerView;
    protected
    @Bind(R.id.emptyImageList)
    TextView emptyRecyclerView;
    protected
    @Bind(R.id.snackbarContainer)
    CoordinatorLayout coordinatorLayout;

    @Inject
    OrmHelper ormHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editor);
        ((StudyProApplication) this.getApplication()).getComponent().inject(this);
        ButterKnife.bind(this);
        this.context = this;
        setupToolbar();
        setupimageRecyclerView();
    }

    /**
     * configs the toolbar
     */
    private void setupToolbar() {
        toolbar.setTitle(R.string.note_editor_activity_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    /**
     * This method either populates the note from the bundle or sets up new note
     */
    private void populateNote() {
        if (getIntent().hasExtra(KEY_NOTE_BUNDLE)) {
            Bundle bundle = getIntent().getBundleExtra(KEY_NOTE_BUNDLE);
            note = (Note) bundle.getParcelable(KEY_NOTE_PARCEL);

            noteTitle.setText(note.getTitle());
            noteBody.setText(note.getNote());
        } else {
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
    public void onBackPressed() {
        super.onBackPressed();
        saveNote();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notes_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_save:
                saveNote();
                finish();
                return true;
            case R.id.menu_delete:
                deleteNote();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Setup the recycler view for displaying the users
     * photos
     */
    private void setupimageRecyclerView() {
        imageRecyclerView.setHasFixedSize(false);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        imageRecyclerView.setLayoutManager(mLayoutManager);
        imageRecyclerView.addItemDecoration(new NotesImageAdapter.HorizontalSpaceItemDecoration(context));

        //put images in the adapter
        ArrayList<NoteImageLocation> imageLocations = getNoteImages();
        imageRecyclerView.setAdapter(new NotesImageAdapter(context, imageLocations, emptyRecyclerView));
    }

    /**
     * Returns the images locations for the current note
     *
     * @return
     */
    private ArrayList<NoteImageLocation> getNoteImages() {
        //TODO fill stub
        return new ArrayList<>();
    }

    /**
     * Saves or updates the note in the database
     */
    private void saveNote() {
        try {
            note.setTitle(noteTitle.getText().toString());
            note.setNote(noteBody.getText().toString());
            Dao<Note, Integer> noteDao = ormHelper.getTypeDao(Note.class, Integer.class);
            noteDao.createOrUpdate(note);
        } catch (SQLException e) {
            Log.e(TAG, "Failed to create/update the note", e);
        }
    }

    /**
     * Deletes the note from the db
     */
    private void deleteNote() {
        try {
            Dao<Note, Integer> noteDao = ormHelper.getTypeDao(Note.class, Integer.class);
            noteDao.delete(note);
        } catch (SQLException e) {
            Log.e(TAG, "Failed to delete the note", e);
        }
    }

}
    



