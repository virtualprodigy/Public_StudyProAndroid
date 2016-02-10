package com.virtualprodigy.studypro.ottoBus.busPostEvents;

import com.virtualprodigy.studypro.Models.Notes.NoteImageLocation;

/**
 * Created by virtualprodigyllc on 11/17/15.
 */
public class ImageDeleteStatus {
    public boolean status;
    public int position;
    public NoteImageLocation noteImageLocation;

    public ImageDeleteStatus(boolean status, int position, NoteImageLocation noteImageLocation) {
        this.status = status;
        this.position = position;
        this.noteImageLocation = noteImageLocation;
    }
}
