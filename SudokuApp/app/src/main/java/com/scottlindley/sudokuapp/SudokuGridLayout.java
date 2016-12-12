package com.scottlindley.sudokuapp;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridLayout;

/**
 * Created by Scott Lindley on 12/11/2016.
 */

public class SudokuGridLayout extends GridLayout{
    public SudokuGridLayout(Context context) {
        super(context);
    }

    public SudokuGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SudokuGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SudokuGridLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, widthSpec);
    }
}
