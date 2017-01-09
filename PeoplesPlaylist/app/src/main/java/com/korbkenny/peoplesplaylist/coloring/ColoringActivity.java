package com.korbkenny.peoplesplaylist.coloring;

import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.korbkenny.peoplesplaylist.BitmapManipulation;
import com.korbkenny.peoplesplaylist.LoginActivity;
import com.korbkenny.peoplesplaylist.R;
import com.korbkenny.peoplesplaylist.UserSingleton;
import com.korbkenny.peoplesplaylist.maps.MapsActivity;
import com.korbkenny.peoplesplaylist.objects.User;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class ColoringActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Coloring Activity: ";
    public static final String FILE_NAME = "usericon.png";
    private DrawingView drawView;
    private ImageButton currPaint;
    private float smallBrush, mediumBrush, largeBrush;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseUserReference;
    private StorageReference mStorageRef;
    private UserSingleton sSingleton;
    private User ME;
    private Bitmap myIcon;
    private TextView drawButton, eraseButton, saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coloring);



        sSingleton = UserSingleton.getInstance();
        ME = sSingleton.getUser();

        boolean fromMyUserInfo = getIntent().getBooleanExtra("FromMyUserInfo",false);
        if(!fromMyUserInfo) {
            if (!ME.getUserImage().equals("null")) {
                Intent intent = new Intent(ColoringActivity.this, MapsActivity.class);
                startActivity(intent);
                finish();
            }
        }

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseUserReference = mFirebaseDatabase.getReference("Users");
        mStorageRef = FirebaseStorage.getInstance().getReference("usericons").child(ME.getId()).child(FILE_NAME);

        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);

        drawButton = (TextView)findViewById(R.id.brush);
        eraseButton = (TextView)findViewById(R.id.erase);
        saveButton = (TextView)findViewById(R.id.save);

        drawView = (DrawingView)findViewById(R.id.drawing);

        LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_colors);
        currPaint = (ImageButton)paintLayout.getChildAt(0);
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));

        drawButton.setOnClickListener(this);
        eraseButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);

        drawView.setBrushSize(mediumBrush);


    }

    public void paintClicked(View view){
        //use chosen color
        drawView.setErase(false);
        drawView.setBrushSize(drawView.getLastBrushSize());
        if(view!=currPaint){
            //update color
            ImageButton imgView = (ImageButton)view;
            String color = view.getTag().toString();
            drawView.setColor(color);

            imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            currPaint=(ImageButton)view;
        }
    }


    @Override
    public void onClick(final View view) {
        if(view.getId()==R.id.brush){
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Brush size:");
            brushDialog.setContentView(R.layout.brush_chooser);

            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(smallBrush);
                    drawView.setLastBrushSize(smallBrush);
                    drawView.setErase(false);
                    brushDialog.dismiss();
                }
            });

            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(mediumBrush);
                    drawView.setLastBrushSize(mediumBrush);
                    drawView.setErase(false);
                    brushDialog.dismiss();
                }
            });

            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(largeBrush);
                    drawView.setLastBrushSize(largeBrush);
                    drawView.setErase(false);
                    brushDialog.dismiss();
                }
            });
            brushDialog.show();
        }

        else if (view.getId()==R.id.erase){
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Eraser size:");
            brushDialog.setContentView(R.layout.brush_chooser);

            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(smallBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(largeBrush);
                    brushDialog.dismiss();
                }
            });
            brushDialog.show();
        }

        else if(view.getId()==R.id.save){
            myIcon = null;
            drawView.setDrawingCacheEnabled(true);
            myIcon = drawView.getDrawingCache();


            new AsyncTask<Void,Void,String>(){
                @Override
                protected void onPreExecute() {
                    drawButton.setEnabled(false);
                    eraseButton.setEnabled(false);
                    saveButton.setEnabled(false);
                }

                @Override
                protected String doInBackground(Void... voids) {
                    return saveImageToDisk(myIcon);
                }

                @Override
                protected void onPostExecute(String iconPath) {
                    Uri selectedImage = Uri.fromFile(new File(iconPath + "/" + FILE_NAME));
                    mStorageRef.putFile(selectedImage)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                                    if(taskSnapshot.getDownloadUrl()!=null) {
                                        new AsyncTask<Void,Void,Void>(){
                                            @Override
                                            protected Void doInBackground(Void... voids) {
                                                ME.setUserImage(taskSnapshot.getDownloadUrl().toString());
                                                return null;
                                            }
                                            @Override
                                            protected void onPostExecute(Void aVoid) {
                                                sSingleton.setUser(ME);
                                                mDatabaseUserReference.child(ME.getId()).setValue(ME);
                                                Log.d(TAG, "onSuccess: " + ME.getId() + "   " + ME.getUserImage());
                                                finish();
                                            }
                                        }.execute();
                                    }
                                }
                            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }.execute();
            }
        }


    private String saveImageToDisk(Bitmap bitmap){
        Bitmap resizedBitmap = BitmapManipulation.resizeForCover(bitmap,200);
        Bitmap circleBitmap = BitmapManipulation.cropToCircle(resizedBitmap);

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir",MODE_PRIVATE);
        File imagePath = new File(directory,FILE_NAME);

        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(imagePath);
            circleBitmap.compress(Bitmap.CompressFormat.PNG,0,fos);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        Log.d(TAG, "saveImageToDisk: " + directory.getAbsolutePath());
        return directory.getAbsolutePath();
    }
}
