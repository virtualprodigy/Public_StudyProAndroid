package com.virtualprodigy.studypro.Notes;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.virtualprodigy.studypro.Database.OrmHelper;
import com.virtualprodigy.studypro.R;
import com.virtualprodigy.studypro.StudyProApplication;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class NotesRecyclerViewFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();


    @Inject
    OrmHelper ormHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notes_recycler_fragment, container, false);
        ((StudyProApplication) getActivity().getApplication()).getComponent().inject(this);
        ButterKnife.bind(this, view);
        return view;
    }

    private void startEditNoteActivity() {

//        Intent intent = new Intent(this, Modify.class);
//        intent.putExtra("requestTitle", create);
//        startActivityForResult(intent, create);
    }

    private void populateListView() {

//        Cursor filler = notesDB.fetchAllNotes();
//        startManagingCursor(filler);
//
//        String[] beginning = new String[]{NotesDataBase.KEY_NOTE_TITLE};
//        int[] end = new int[]{R.id.notenom};
//
//        SimpleCursorAdapter list = new SimpleCursorAdapter(this,
//                R.layout.noterowveiw, filler, beginning, end);
//        setListAdapter(list);

    }

}

