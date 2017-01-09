package com.korbkenny.peoplesplaylist;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;

/**
 * Created by KorbBookProReturns on 1/5/17.
 */

public class BitmapManipulation {

    public static Bitmap cropToSquare(Bitmap bitmap){
        if(bitmap.getWidth() >= bitmap.getHeight()){
            Bitmap newBitmap = Bitmap.createBitmap(
                    bitmap,
                    (bitmap.getWidth() / 2) - (bitmap.getHeight() / 2),
                    0,
                    bitmap.getHeight(),
                    bitmap.getHeight()
            );
            return newBitmap;
        } else{
            Bitmap newBitmap = Bitmap.createBitmap(
                    bitmap,
                    0,
                    (bitmap.getHeight()/2) - (bitmap.getWidth()/2),
                    bitmap.getWidth(),
                    bitmap.getWidth()
            );
            return newBitmap;
        }
    }

    public static Bitmap resizeForCover(Bitmap bitmap,int size){
        return Bitmap.createScaledBitmap(bitmap,size,size,false);
    }

    public static Bitmap cropToCircle(Bitmap bitmap) {
        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();
        final Bitmap outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        final Path path = new Path();
        path.addCircle(
                (float)(width / 2)
                , (float)(height / 2)
                , (float) Math.min(width, (height / 2))
                , Path.Direction.CCW);

        final Canvas canvas = new Canvas(outputBitmap);
        canvas.clipPath(path);
        canvas.drawBitmap(bitmap, 0, 0, null);
        return outputBitmap;
    }
}
