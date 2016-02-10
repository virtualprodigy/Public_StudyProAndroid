package com.virtualprodigy.studypro.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.squareup.picasso.Picasso;
import com.virtualprodigy.studypro.Database.OrmHelper;
import com.virtualprodigy.studypro.Models.Notes.NoteImageLocation;
import com.virtualprodigy.studypro.R;
import com.virtualprodigy.studypro.StudyProApplication;
import com.virtualprodigy.studypro.Utils.FileUtils;
import com.virtualprodigy.studypro.ottoBus.OttoHelper;
import com.virtualprodigy.studypro.ottoBus.busPostEvents.ExpandImageEvent;
import com.virtualprodigy.studypro.ottoBus.busPostEvents.ImageDeleteStatus;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by virtualprodigyllc on 2/9/16.
 */
public class NotesImageAdapter extends RecyclerView.Adapter<NotesImageAdapter.NoteViewHolder> {
    private final String TAG = this.getClass().getSimpleName();
    private Context context;
    private List<NoteImageLocation> noteImageLocations;
    private TextView emptyRecyclerView;
    private RecyclerView recyclerView;
    protected int imageWidth;
    protected int imageHeight;

    @Inject
    OrmHelper helper;
    @Inject
    FileUtils fileUtils;

    public NotesImageAdapter(Context context, List<NoteImageLocation> noteImageLocations, TextView emptyRecyclerView) {
        this.context = context;
        this.noteImageLocations = noteImageLocations;
        this.emptyRecyclerView = emptyRecyclerView;
        ((StudyProApplication) ((Activity) context).getApplication()).getComponent().inject(this);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    /**
     * Measures the size of a note image
     */
    private void measureForImageSize() {
        Resources res = context.getResources();
        int horizontalPadding = (int) (res.getDimension(R.dimen.note_image_item_horizontal_padding) * 2);
        int verticalPadding = (int) (res.getDimension(R.dimen.note_image_item_vertical_padding) * 2);

        imageWidth = recyclerView.getWidth() - horizontalPadding;
        imageHeight =recyclerView.getHeight() - verticalPadding;
    }

    /**
     * This method receives a new list of images and
     * notifies the adapter the data set has changed
     *
     * @param noteImageLocations
     */
    public void setNoteImageLocations(List<NoteImageLocation> noteImageLocations) {
        this.noteImageLocations = noteImageLocations;
        notifyDataSetChanged();
    }


    /**
     * if it's review mode the extra space for the add new icon is not needed
     * if the count is 0(can't happen in edit mode) display an empty list messgae
     *
     * @return
     */
    @Override
    public int getItemCount() {
        int count = noteImageLocations.size();
        emptyRecyclerView.setVisibility(count == 0 ? View.VISIBLE : View.INVISIBLE);
        return count;
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        protected ImageView noteImage;
        protected View cardContainer;

        public NoteViewHolder(View cardContainer) {
            super(cardContainer);
            this.cardContainer = cardContainer;
            noteImage = (ImageView) cardContainer.findViewById(R.id.noteImage);
        }
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.note_image_item, parent, false);

        //ensure all the recycler view items are the same size, taking up the full width of the
        //bottom of the screen
        if(imageHeight <=0 || imageWidth <= 0){
            measureForImageSize();
        }

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = imageHeight;
        layoutParams.width = imageWidth;
        view.setLayoutParams(layoutParams);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NoteViewHolder holder, final int position) {

        NoteImageLocation imageLocation = noteImageLocations.get(position);
        if (imageLocation.getUri() == null) {
            Picasso.with(context).load(R.drawable.image_icon).into(holder.noteImage);
        } else {

            Picasso.with(context).load(imageLocation.getUri()).fit().centerCrop().into(holder.noteImage);
        }

        //display larger photo click
        holder.cardContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayLargerPhoto(holder, position);
            }
        });

        //delete photo long press
        holder.cardContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                displayDeleteImageDialog(noteImageLocations.get(position), position);
                return true;
            }
        });
    }

    public static class HorizontalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int mHorizontalSpaceHeight;

        public HorizontalSpaceItemDecoration(Context context) {
            this.mHorizontalSpaceHeight = (int) context.getResources().getDimension(R.dimen.note_images_horizontal_divider_space);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.right = mHorizontalSpaceHeight;
        }
    }

    /**
     * This method opens a dialog and displays a larger version of the photo the user selected
     */
    private void displayLargerPhoto(NoteViewHolder holder, int position) {
        NoteImageLocation imageLocation = noteImageLocations.get(position);
        OttoHelper.getInstance().post(new ExpandImageEvent(holder.noteImage, imageLocation.getUri()));

    }

    /**
     * Displays a dialog to confirm if the user wants to delete a photo
     *
     * @param imageLocation
     * @param position
     */
    public void displayDeleteImageDialog(final NoteImageLocation imageLocation, final int position) {

        final AlertDialog deleteDialog =
                new AlertDialog.Builder(context)
                        .setTitle(context.getString(R.string.image_delete_dialog_title))
                        .setMessage(context.getString(R.string.image_delete_dialog_message))
                        .setPositiveButton(context.getString(R.string.yes)
                                , new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteImageDialog(imageLocation, position);
                                        dialog.dismiss();
                                    }
                                })
                        .setNegativeButton(context.getString(R.string.no), null)
                        .create();
        if (!deleteDialog.isShowing())
            deleteDialog.show();
    }

    /**
     * This method deletes the image from the file system and notifies the results
     *
     * @param imageLocation
     * @param position
     */
    public void deleteImageDialog(NoteImageLocation imageLocation, int position) {
        boolean success = fileUtils.deleteFile(new File(imageLocation.getUri().getPath()));
        //if the file deletes but the db fails to delete mark as a failure to retry. Only happens if the imagelocation has an id
        if (imageLocation.getId() != -1) {
            try {
                Dao<NoteImageLocation, Integer> dao = helper.getTypeDao(NoteImageLocation.class, Integer.class);
                DeleteBuilder<NoteImageLocation, Integer> deleteBuilder = dao.deleteBuilder();
                deleteBuilder.where().eq("id", imageLocation.getId());
                success = dao.delete(deleteBuilder.prepare()) > 0;
            } catch (SQLException e) {
                Log.e(TAG, "Failed to delete the image location in db", e);
                success = false;
            }
        }
        if (success) {
            //delete the file from the adapter and update
            noteImageLocations.remove(position);
            notifyDataSetChanged();
        }
        // notify the adapter's controller so they can handle it possibly retry
        OttoHelper.getInstance().post(new ImageDeleteStatus(success, position, imageLocation));
    }
}

