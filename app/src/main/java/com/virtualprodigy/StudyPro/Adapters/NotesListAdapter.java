package com.virtualprodigy.studypro.Adapters;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.virtualprodigy.studypro.Models.Notes.Note;
import com.virtualprodigy.studypro.Models.Notes.NoteImageLocation;
import com.virtualprodigy.studypro.Notes.NoteEditor;
import com.virtualprodigy.studypro.R;
import com.virtualprodigy.studypro.ottoBus.OttoHelper;
import com.virtualprodigy.studypro.ottoBus.busPostEvents.LaunchNoteEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by virtualprodigyllc on 2/9/16.
 */
public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.NotesViewHolder> {
    private final String TAG = this.getClass().getSimpleName();
    private Context context;
    private List<Note> notelList;

    public NotesListAdapter(Context context, List<Note> notelList) {
        this.context = context;
        this.notelList = notelList;
    }

    @Override
    public int getItemCount() {
        return notelList.size();
    }

    public static class NotesViewHolder extends RecyclerView.ViewHolder {
        protected TextView noteBody;
        protected TextView title;
        protected TextView photoCount;
        protected ImageView noteImageThumb;
        protected TextView time;
        protected TextView date;
        protected View cardContainer;

        public NotesViewHolder(View cardContainer) {
            super(cardContainer);
            this.cardContainer = cardContainer;
            time = (TextView) cardContainer.findViewById(R.id.timeDisplay);
            date = (TextView) cardContainer.findViewById(R.id.dateDisplay);
            photoCount = (TextView) cardContainer.findViewById(R.id.noteImageCount);
            noteImageThumb = (ImageView) cardContainer.findViewById(R.id.noteThumbnail);
            title = (TextView) cardContainer.findViewById(R.id.titleTextView);
            noteBody = (TextView) cardContainer.findViewById(R.id.noteBodyPreview);

        }
    }

    @Override
    public NotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.notes_cardview_item, parent, false);
        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotesViewHolder holder, int position) {
        final Note note = notelList.get(position);

        holder.date.setText(note.getDate(context.getResources()));
        holder.time.setText(note.getTime(context.getResources()));
        holder.title.setText(note.getTitle());
        holder.noteBody.setText(note.getNote());

        int imageCount = note.getImageCount();
        if (imageCount == 0) {
            Picasso.with(context).load(R.drawable.person_outline).fit().into(holder.noteImageThumb);
        } else {
            imageCount = imageCount > 1 ? imageCount - 1 : 0;
            ArrayList<NoteImageLocation> imageLocations = note.getNoteImageLocation();
            if (imageLocations != null && imageLocations.size() > 0) {
                NoteImageLocation imageLocation = imageLocations.get(0);
                Picasso.with(context).load(imageLocation.getUri()).fit().centerCrop().into(holder.noteImageThumb);
            } else {
                Picasso.with(context).load(R.drawable.person_outline).fit().into(holder.noteImageThumb);
                Log.e(TAG, "Error loading thumb image location " + (imageLocations != null ? imageLocations.get(0) : " null array"));
            }
        }

        String photoCountString = context.getString(R.string.note_photo_count);
        photoCountString = String.format(photoCountString, imageCount);
        holder.photoCount.setText(photoCountString);

        holder.cardContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle noteSelected = new Bundle();
                noteSelected.putParcelable(NoteEditor.KEY_NOTE_PARCEL, note);
                OttoHelper.getInstance().post(new LaunchNoteEvent(noteSelected));
            }
        });
    }

    public static class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int mVerticalSpaceHeight;

        public VerticalSpaceItemDecoration(Context context) {
            this.mVerticalSpaceHeight = (int) context.getResources().getDimension(R.dimen.note_cardview_divider_space);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.bottom = mVerticalSpaceHeight;
        }
    }
}

