package com.ezequielc.successplanner.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ezequielc.successplanner.R;

import java.io.IOException;

public class VisionBoardActivity extends AppCompatActivity {
    public static final int IMAGE_REQUEST = 1;

    View.OnTouchListener mTouchListener;
    ImageView mNewImage;
    TextView mNewText;
    ViewGroup mViewGroup;
    int mStartX, mStartY;
    boolean mEditingOrDeleting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vision_board);

        getSupportActionBar().setTitle("Vision Board");

        mViewGroup = (ViewGroup) findViewById(R.id.activity_vision_board);

        mTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (mEditingOrDeleting) {
                    return false;
                }

                RelativeLayout.LayoutParams getLayoutParams =
                        (RelativeLayout.LayoutParams) view.getLayoutParams();

                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:
                        mStartX = (int) motionEvent.getX();
                        mStartY = (int) motionEvent.getY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        int delta_x = (int) motionEvent.getX() - mStartX;
                        int delta_y = (int) motionEvent.getY() - mStartY;
                        getLayoutParams.leftMargin = getLayoutParams.leftMargin + delta_x;
                        getLayoutParams.rightMargin = getLayoutParams.rightMargin - delta_x;
                        getLayoutParams.topMargin = getLayoutParams.topMargin + delta_y;
                        getLayoutParams.bottomMargin = getLayoutParams.bottomMargin - delta_y;
                        view.setLayoutParams(getLayoutParams);
                        break;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                        break;
                }
                mViewGroup.invalidate();
                return true;
            }
        };
    }

    public void textOptions(){
        CharSequence[] textOptions = {"Add New", "Edit Text"};
        AlertDialog.Builder builder = new AlertDialog.Builder(VisionBoardActivity.this)
                .setTitle("Text Options")
                .setItems(textOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0: // Add New
                                addNewText();
                                break;
                            case 1: // Edit Text
                                editTextView();
                                break;
                            default:
                                break;
                        }
                    }
                });
        builder.create().show();
    }

    public void addNewText(){
        RelativeLayout.LayoutParams wrapContent =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);

        mNewText = new TextView(getApplication());
        mNewText.setLayoutParams(wrapContent);

        AlertDialog.Builder builder = new AlertDialog.Builder(VisionBoardActivity.this);
        LayoutInflater inflater = LayoutInflater.from(VisionBoardActivity.this);
        View view = inflater.inflate(R.layout.dialog_add_text, null);
        builder.setView(view);

        final EditText editText = (EditText) view.findViewById(R.id.new_text_edit_text);

        builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (editText.getText().toString().trim().length() == 0) {
                            Toast.makeText(VisionBoardActivity.this, "Please fill field!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String input = editText.getText().toString();
                        mNewText.setText(input);
                        mViewGroup.addView(mNewText);
                    }
                })
                .setNegativeButton("Cancel", null);
        builder.create().show();

        mNewText.setTextSize(24); // TODO; TEMP CODE // REPLACE WITH OPTION
        mNewText.setOnTouchListener(mTouchListener);
    }

    public void editTextView(){
        if (mViewGroup.getChildCount() == 0) {
            Toast.makeText(this, "Vision Board is Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        mEditingOrDeleting = true;
        Toast.makeText(this, "Choose Text to Edit...", Toast.LENGTH_LONG).show();

        for (int i = 0; i < mViewGroup.getChildCount(); i++) {
            final View child = mViewGroup.getChildAt(i);
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Handles Error when picking an ImageView instead of a TextView, ClassCastException
                    if (child instanceof ImageView) {
                        Toast.makeText(VisionBoardActivity.this, "This is an Image", Toast.LENGTH_SHORT).show();
                        mEditingOrDeleting = false;
                        return;
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(VisionBoardActivity.this);
                    LayoutInflater inflater = LayoutInflater.from(VisionBoardActivity.this);
                    View dialogView = inflater.inflate(R.layout.dialog_add_text, null);
                    builder.setView(dialogView);

                    final EditText editText = (EditText) dialogView.findViewById(R.id.new_text_edit_text);
                    editText.setText(((TextView) child).getText().toString());

                    builder.setPositiveButton("EDIT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String input = editText.getText().toString();
                            ((TextView) child).setText(input);
                        }
                    })
                            .setNegativeButton("Cancel", null);
                    builder.create().show();
                    mEditingOrDeleting = false;
                }
            });
        }
    }

    public void addNewImage(Bitmap bitmap){
        RelativeLayout.LayoutParams wrapContent =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);

        mNewImage = new ImageView(getApplicationContext());
        mNewImage.setLayoutParams(wrapContent);
        mViewGroup.addView(mNewImage);
        mNewImage.setImageBitmap(bitmap);

        mNewImage.setOnTouchListener(mTouchListener);
    }

    public void deletionOptions(){
        if (mViewGroup.getChildCount() == 0) {
            Toast.makeText(this, "Vision Board is Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        CharSequence[] deleteOptions = {"Delete Item", "Delete All"};
        AlertDialog.Builder builder = new AlertDialog.Builder(VisionBoardActivity.this)
                .setTitle("Deletion Options:")
                .setItems(deleteOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0: // Delete One View
                                deleteView();
                                break;
                            case 1: // Delete All Views
                                deleteAllViews();
                                break;
                            default:
                                break;
                        }
                    }
                });
        builder.create().show();
    }

    public void deleteAllViews(){
        AlertDialog.Builder builder = new AlertDialog.Builder(VisionBoardActivity.this);
        builder.setMessage("Delete All?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mViewGroup.removeAllViews();
                    }
                })
                .setNegativeButton("No", null);
        builder.create().show();
    }

    public void deleteView(){
        mEditingOrDeleting = true;
        Toast.makeText(this, "Choose Item to Delete...", Toast.LENGTH_LONG).show();

        for (int i = 0; i < mViewGroup.getChildCount(); i++) {
            final View child = mViewGroup.getChildAt(i);
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(VisionBoardActivity.this);
                    builder.setMessage("Delete Item?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    mViewGroup.removeView(child);
                                }
                            })
                            .setNegativeButton("No", null);
                    builder.create().show();
                    mEditingOrDeleting = false;
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.vision_board_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_upload_image:
                selectImage();
                return true;

            case R.id.action_text:
                textOptions();
                return true;

            case R.id.action_delete:
                deletionOptions();
                return true;

            case R.id.action_save:
                Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_settings:
                Intent settingsIntent = new Intent(VisionBoardActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && null != data) {
            Uri image = data.getData();
            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image);
                addNewImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent.createChooser(intent, "Select Image"), IMAGE_REQUEST);
    }
}
