package com.virtualprodigy.studypro.Models.Notes;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by virtualprodigyllc on 2/9/16.
 */
@DatabaseTable(tableName = "_NoteImageLocationTable")
public class NoteImageLocation implements Parcelable {
    @DatabaseField(id = true, columnName = "_id")
    private int id;

    //memoir_id
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Note note;

    @DatabaseField
    private String uri;

    public NoteImageLocation() {
    }

    public NoteImageLocation(Uri uri) {
        if (uri == null) {
            this.uri = null;
        } else {
            this.uri = uri.toString();
        }
        id = -1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Uri getUri() {
        return uri == null ? null : Uri.parse(uri);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.uri);
    }

    protected NoteImageLocation(Parcel in) {
        this.id = in.readInt();
        this.uri = in.readString();
    }

    public static final Parcelable.Creator<NoteImageLocation> CREATOR = new Parcelable.Creator<NoteImageLocation>() {
        public NoteImageLocation createFromParcel(Parcel source) {
            return new NoteImageLocation(source);
        }

        public NoteImageLocation[] newArray(int size) {
            return new NoteImageLocation[size];
        }
    };
}
