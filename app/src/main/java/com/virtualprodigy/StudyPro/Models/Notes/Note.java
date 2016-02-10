package com.virtualprodigy.studypro.Models.Notes;

import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateFormat;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.virtualprodigy.studypro.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

/**
 * Created by virtualprodigyllc on 2/1/16.
 */
@DatabaseTable(tableName = "_NotesTable")
public class Note implements Parcelable {

    @DatabaseField(generatedId = true, columnName = "_id")
    private int id;

    @DatabaseField(columnName = "_title")
    private String title;

    @DatabaseField(columnName = "_note")
    private String note;

    /**
     * The ms of the date and time the note was created
     */
    @DatabaseField(columnName = "_dateTime")
    private long dateTime;

    public Note() {
        id = -1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @ForeignCollectionField(eager = true)
    private Collection<NoteImageLocation> noteImageLocations;

    /**
     * Returns a formatted time
     *
     * @param res
     * @return
     */
    public String getTime(Resources res) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(res.getString(R.string.time_format), Locale.getDefault());
        CharSequence formattedTime = simpleDateFormat.format(dateTime);
        return formattedTime.toString();
    }

    /**
     * reutnrs a locally formatted date
     *
     * @return
     */
    public String getDate(Resources res) {
        CharSequence formattedDate = DateFormat.format(res.getString(R.string.date_format), dateTime);
        return formattedDate.toString();
    }

    /**
     * This method expects to receive the current
     * ms to be used as the created date and time
     *
     * @param currentMS
     */
    public void setDateTime(long currentMS) {
        this.dateTime = currentMS;
    }

    public ArrayList<NoteImageLocation> getNoteImageLocation() {
        if (noteImageLocations == null) {
            return null;
        }
        return new ArrayList<NoteImageLocation>(noteImageLocations);
    }

    public void setNoteImageLocation(ArrayList<NoteImageLocation> noteImageLocations) {
        this.noteImageLocations = noteImageLocations;
    }

    private NoteImageLocation[] getParcelNoteImageLocation() {
        if (noteImageLocations == null) {
            noteImageLocations = new ArrayList<>();
        }
        NoteImageLocation[] imageLocArray = noteImageLocations.toArray(new NoteImageLocation[noteImageLocations.size()]);
        return imageLocArray;
    }

    /**
     * This method returns the number of images within the note
     *
     * @return - the size of the noteImageLocations
     */
    public int getImageCount() {
        if (noteImageLocations == null) {
            return 0;
        } else {
            return noteImageLocations.size();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedArray(getParcelNoteImageLocation(), flags);
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.note);
        dest.writeLong(this.dateTime);
    }

    protected Note(Parcel in) {
        NoteImageLocation[] imageTypedArray = in.createTypedArray(NoteImageLocation.CREATOR);
        this.noteImageLocations = Arrays.asList(imageTypedArray);
        this.id = in.readInt();
        this.title = in.readString();
        this.note = in.readString();
        this.dateTime = in.readLong();
    }

    public static final Parcelable.Creator<Note> CREATOR = new Parcelable.Creator<Note>() {
        public Note createFromParcel(Parcel source) {
            return new Note(source);
        }

        public Note[] newArray(int size) {
            return new Note[size];
        }
    };
}
