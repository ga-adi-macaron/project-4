package com.scottlindley.sudokuapp;

import android.content.Context;
import android.widget.TextView;

/**
 * Created by Scott Lindley on 12/11/2016.
 */

public class SudokuCellView extends TextView {
    public SudokuCellView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
