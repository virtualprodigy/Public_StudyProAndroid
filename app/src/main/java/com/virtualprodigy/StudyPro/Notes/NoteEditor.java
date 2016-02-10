package com.virtualprodigy.studypro.Notes;

import com.github.clans.fab.FloatingActionMenu;
import com.j256.ormlite.dao.Dao;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.virtualprodigy.studypro.Adapters.NotesImageAdapter;
import com.virtualprodigy.studypro.Database.OrmHelper;
import com.virtualprodigy.studypro.Models.Notes.Note;
import com.virtualprodigy.studypro.Models.Notes.NoteImageLocation;
import com.virtualprodigy.studypro.R;
import com.virtualprodigy.studypro.StudyProApplication;
import com.virtualprodigy.studypro.Utils.FileUtils;
import com.virtualprodigy.studypro.ottoBus.OttoHelper;
import com.virtualprodigy.studypro.ottoBus.busPostEvents.ExpandImageEvent;
import com.virtualprodigy.studypro.ottoBus.busPostEvents.ImageDeleteStatus;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NoteEditor extends AppCompatActivity implements TextWatcher {
    private final String TAG = this.getClass().getSimpleName();
    public static final String KEY_NOTE_PARCEL = "note_parcel";
    public static final String KEY_NOTE_BUNDLE = "note_bundle";
    //activity request codes
    public static final int REQUEST_CODE_GALLERY = 1;
    public static final int REQUEST_CODE_CAMERA = 2;

    private Note note;
    private Context context;
    private File cameraImageFile;

    //expanding the imageview
    private Animator expandImageAnimator;
    private int shortAnimatorDuration;
    /**
     * flaggs wheter the note has been modified
     */
    private boolean noteEdited = false;

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

    protected
    @Bind(R.id.expanded_image)
    ImageView expandedImage;

    protected
    @Bind(R.id.fam)
    FloatingActionMenu notesFam;

    @Inject
    OrmHelper ormHelper;
    @Inject
    FileUtils fileUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editor);
        ((StudyProApplication) this.getApplication()).getComponent().inject(this);
        ButterKnife.bind(this);
        this.context = this;
        setupToolbar();
        setupimageRecyclerView();

        //      sets the floating action menu to close when you click outside of it's area
        notesFam.setClosedOnTouchOutside(true);

        //add text change listener to edittexts to know when note is edited
        noteTitle.addTextChangedListener(this);
        noteBody.addTextChangedListener(this);

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
        OttoHelper.getInstance().register(this);
        populateNote();
    }

    @Override
    protected void onPause() {
        super.onPause();
        OttoHelper.getInstance().unregister(this);
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        noteEdited = true;

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
     * This method displays a status snackbar of the delete
     * if it failed it gives a retry option
     *
     * @param status
     */
    @Subscribe
    public void receiveImageDeleteStatus(final ImageDeleteStatus status) {
        if (status.status) {
            Snackbar.make(coordinatorLayout, R.string.image_delete_successful, Snackbar.LENGTH_LONG).show();
            noteEdited = true;
            //Java pass by reference updates the list in the note
        } else {
            Snackbar
                    .make(coordinatorLayout, R.string.image_delete_failed, Snackbar.LENGTH_LONG)
                    .setAction(R.string.retry, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((NotesImageAdapter) imageRecyclerView.getAdapter()).deleteImageDialog(status.noteImageLocation, status.position);
                        }
                    })
                    .show();
        }
    }

    /**
     * This method gets an image from the Gallery
     */
    @OnClick(R.id.add_gallery_image)
    public void onClickAddGalleryImage() {
        if (notesFam.isOpened()) {
            notesFam.close(true);
        }
        //Check if Note has a unique photo id, if not, create it
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE_GALLERY);
    }

    /**
     * This method gets an image from the Camera
     */
    @OnClick(R.id.add_camera_image)
    public void onClickAddCameraImage() {
        if (notesFam.isOpened()) {
            notesFam.close(true);
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            //provide the file for the image
            cameraImageFile = fileUtils.createImageFilePlusDirectory(MimeTypeMap.getSingleton().getMimeTypeFromExtension("jpg"));
            if (cameraImageFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraImageFile));
            } else {
                Snackbar.make(coordinatorLayout, R.string.camera_file_error, Snackbar.LENGTH_SHORT).show();
                return;
            }

            startActivityForResult(takePictureIntent, REQUEST_CODE_CAMERA);
        } else {
            Snackbar.make(coordinatorLayout, R.string.camera_not_available, Snackbar.LENGTH_SHORT).show();
        }

    }

    /**
     * this method is called to expand the note image in the recycler view via otto
     *
     * @param event
     */
    @Subscribe
    public void requestExpandImageView(ExpandImageEvent event) {
        shortAnimatorDuration = context.getResources().getInteger(android.R.integer.config_shortAnimTime);
        expandRecyclerImage(event.view, event.uri);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_GALLERY) {
                moveGalleryImageToAppStorage(data);
            } else if (requestCode == REQUEST_CODE_CAMERA) {
                addImageToNotesCollection(cameraImageFile);
            }
        }
    }

    /**
     * This method handles the data received in onActivityResult and moves
     * the gallery image to the notes folder
     *
     * @param data
     */
    private void moveGalleryImageToAppStorage(final Intent data) {

        new AsyncTask<Void, Void, File>() {

            @Override
            protected File doInBackground(Void... params) {
                Uri selectedImageUri = data.getData();
                //get the file type
                ContentResolver contentResolver = context.getContentResolver();
                String mimeType = contentResolver.getType(data.getData());

                File noteImage = null;
                if (Build.VERSION.SDK_INT < 19) {
                    try {
                        String selectedImagePath = fileUtils.getPath(selectedImageUri);
                        File sourceFile = new File(selectedImagePath);
                        FileInputStream sourceIS = new FileInputStream(sourceFile);
                        noteImage = fileUtils.copyImageToNotesImageFolder(sourceIS, mimeType);
                    } catch (FileNotFoundException e) {
                        Log.e(TAG, "get image from gallery failed", e);
                    }
                } else {
                    ParcelFileDescriptor parcelFileDescriptor;
                    try {
                        parcelFileDescriptor = context.getContentResolver().openFileDescriptor(selectedImageUri, "r");
                        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                        FileInputStream sourceIS = new FileInputStream(fileDescriptor);
                        noteImage = fileUtils.copyImageToNotesImageFolder(sourceIS, mimeType);
                    } catch (Exception e) {
                        Log.e(TAG, "get image from gallery failed", e);
                    }
                }

                return noteImage;
            }

            @Override
            protected void onPostExecute(File noteImage) {
                super.onPostExecute(noteImage);
                if (noteImage == null) {
                    //display failed snackbar
                    Snackbar.make(coordinatorLayout, R.string.failed_adding_image, Snackbar.LENGTH_LONG).show();
                } else {
                    //update adapter
                    addImageToNotesCollection(noteImage);
                }
            }
        }.execute();
    }

    /**
     * This method adds the image location the collection in the Note class.
     * It does not save it to orm. That is done with the rest of the saving logic
     */
    private void addImageToNotesCollection(File noteImage) {
        checkImageOrientation(noteImage);
        ArrayList<NoteImageLocation> noteImageLocations = note.getNoteImageLocation();
        if (noteImageLocations == null) {
            noteImageLocations = new ArrayList<>();
        }
        //added the image location to collection
        NoteImageLocation imageLocation = new NoteImageLocation(Uri.fromFile(noteImage));
        noteImageLocations.add(imageLocation);
        //set the collection in the note
        note.setNoteImageLocation(noteImageLocations);

        //flag the note as updated
        noteEdited = true;

        //check if the file used for the camera loaded. if so, clear it
        if (cameraImageFile != null) {
            cameraImageFile = null;
        }

        //updated adapter
        NotesImageAdapter adapter = (NotesImageAdapter) imageRecyclerView.getAdapter();
        adapter.setNoteImageLocations(noteImageLocations);
    }

    /**
     * This method will roate the image to landscape if it isn't already
     * @param imageFile
     */
    private void checkImageOrientation(File imageFile){
        //TODO rotate with this code or with Picasso and when do I want to do a rotate, should I check the image bounds?
//        BitmapFactory.Options bounds = new BitmapFactory.Options();
//        bounds.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(imageFile, bounds);
//
//        BitmapFactory.Options opts = new BitmapFactory.Options();
//        Bitmap bm = BitmapFactory.decodeFile(imageFile, opts);
//        ExifInterface exif = new ExifInterface(imageFile);
//        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
//        int orientation = orientString != null ? Integer.parseInt(orientString) :  ExifInterface.ORIENTATION_NORMAL;
//
//        int rotationAngle = 0;
//        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
//        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
//        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
//
//        Matrix matrix = new Matrix();
//        matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
//        Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
    }

    /**
     * The below code was taking from the sample on the
     * Google android developer website and was licensed under the
     * Creative Commons Attribution 2.5 it has been split into two methods
     * expandRecyclerImage & animateExpandImage
     * <p/>
     * License - https://creativecommons.org/licenses/by/2.5/
     * Code location - http://developer.android.com/training/animation/zoom.html
     * <p/>
     * THis method will zoom the image to an expanded size.
     *
     * @param recyclerImageView - the image view from the recycler view
     * @param uri               - the image from the recycler view
     */
    private void expandRecyclerImage(final ImageView recyclerImageView, Uri uri) {

        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (expandImageAnimator != null) {
            expandImageAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        // Since Picasso checks its cache first, use the load callback to wait before
        // starting the expansion animation
        int imageWidth = expandedImage.getWidth();
        int imageHeight = expandedImage.getHeight();
        Picasso.with(context)
                .load(uri)
                .resize(imageWidth, imageHeight)
                .onlyScaleDown()
                .centerInside()
                .noFade()
                .into(expandedImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        animateExpandImage(recyclerImageView);
                    }

                    @Override
                    public void onError() {
                        Log.e(TAG, "Picasso failed to load image for expansion");
                    }
                });
    }

    /**
     * The below code was taking from the sample on the
     * Google android developer website and was licensed under the
     * Creative Commons Attribution 2.5 it has been split into two methods
     * expandRecyclerImage & animateExpandImage
     * <p/>
     * License - https://creativecommons.org/licenses/by/2.5/
     * Code location - http://developer.android.com/training/animation/zoom.html
     * <p/>
     * THis method will zoom the image to an expanded size.
     *
     * @param recyclerImageView - the image view from the recycler view
     */
    private void animateExpandImage(final ImageView recyclerImageView) {
        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        recyclerImageView.getGlobalVisibleRect(startBounds);
        expandedImage.getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        recyclerImageView.setAlpha(0f);
        expandedImage.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImage.setPivotX(0f);
        expandedImage.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImage, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImage, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImage, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImage,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(shortAnimatorDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                expandImageAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                expandImageAnimator = null;
            }
        });
        set.start();
        expandImageAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (expandImageAnimator != null) {
                    expandImageAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImage, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImage,
                                        View.Y, startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImage,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImage,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(shortAnimatorDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        recyclerImageView.setAlpha(1f);
                        expandedImage.setVisibility(View.GONE);
                        expandedImage.setImageDrawable(null);
                        expandImageAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        recyclerImageView.setAlpha(1f);
                        expandedImage.setVisibility(View.GONE);
                        expandImageAnimator = null;
                        expandedImage.setImageDrawable(null);
                    }
                });
                set.start();
                expandImageAnimator = set;
            }
        });
    }


    /**
     * Saves or updates the note in the database
     */
    private void saveNote() {
        if (noteEdited) {
            try {
                note.setTitle(noteTitle.getText().toString());
                note.setNote(noteBody.getText().toString());
                Dao<Note, Integer> noteDao = ormHelper.getTypeDao(Note.class, Integer.class);
                noteDao.createOrUpdate(note);
            } catch (SQLException e) {
                Log.e(TAG, "Failed to create/update the note", e);
            }
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
    



