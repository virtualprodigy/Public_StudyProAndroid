package com.virtualprodigy.studypro.Notes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.squareup.otto.Subscribe;
import com.virtualprodigy.studypro.Adapters.NotesListAdapter;
import com.virtualprodigy.studypro.Database.OrmHelper;
import com.virtualprodigy.studypro.Models.Notes.Note;
import com.virtualprodigy.studypro.R;
import com.virtualprodigy.studypro.StudyProApplication;
import com.virtualprodigy.studypro.ottoBus.OttoHelper;
import com.virtualprodigy.studypro.ottoBus.busPostEvents.LaunchNoteEvent;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotesRecyclerViewFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();
    private Context context;

    @Bind(R.id.notesRecyclerView)
    RecyclerView recyclerView;

    @Inject
    OrmHelper ormHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notes_recycler_fragment, container, false);
        ((StudyProApplication) getActivity().getApplication()).getComponent().inject(this);
        ButterKnife.bind(this, view);
        this.context = getActivity();
        populateListView();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        OttoHelper.getInstance().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        OttoHelper.getInstance().unregister(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        //called when changing fragments
        if(!hidden){
            populateListView();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        populateListView();
    }

    /**
     * Launches the activity for editing a note
     * @param event
     */
    @Subscribe
    private void receiveSelectedNoteEvent(LaunchNoteEvent event) {
        Intent intent = new Intent(context, NoteEditor.class);
        intent.putExtra(NoteEditor.KEY_NOTE_BUNDLE, event.getBundle());
        startActivity(intent);
    }

    /**
     * This method handles the onClick for adding a new note
     */
    @OnClick(R.id.addNewNote)
    public void onClickAddNewNote() {
        Intent intent = new Intent(context, NoteEditor.class);
        startActivity(intent);
    }

    /**
     * populates the recycler view with notes
     */
    private void populateListView() {
        setupNotesRecyclerView();
        try {
            //TODO instead of going through the fun of setting up a cursor, I'm going to use a list, I've decide that I want to throw realm into this project just for fun, so I just need to get this running for testing/demo before Realm
            Dao<Note, Integer> noteDao = ormHelper.getTypeDao(Note.class, Integer.class);
            QueryBuilder<Note, Integer> noteQueryBuilder = noteDao.queryBuilder();
            List<Note> notesList = noteQueryBuilder.query();
            if(notesList != null && notesList.size() > 0){
               recyclerView.setAdapter(new NotesListAdapter(context, notesList));
            }else{
                recyclerView.setAdapter(new NotesListAdapter(context, new ArrayList<Note>()));
            }
        } catch (SQLException e) {
            Log.e(TAG, "Failed getting the notes from the database", e);
            recyclerView.setAdapter(new NotesListAdapter(context, new ArrayList<Note>()));
        }
    }

    /**
     * inits the recycler view
     */
    private void setupNotesRecyclerView() {
        if (recyclerView.getLayoutManager() == null) {
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new NotesListAdapter.VerticalSpaceItemDecoration(context));
        }
    }

}

