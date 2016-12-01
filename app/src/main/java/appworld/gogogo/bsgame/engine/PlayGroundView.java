package appworld.gogogo.bsgame.engine;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Raul on 04.11.2016.
 */

public class PlayGroundView extends View implements View.OnTouchListener {

    private static String NUMBER_OF_SQUARES = "numberOfSquares";

    private Paint mPaint;
    private Rect[] rects;
    private boolean[] markedRects;
    private int numberOfSquares;
    private int parentWidth;
    private int parentHeight;

    public PlayGroundView(Context context) {
        super(context);
    }

    public PlayGroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //TODO: getAttribute ist nicht richtig
        // and we set a new Paint with the desired attributes
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3);

        // TODO: numbers of squares has to be dinamically called
        numberOfSquares = 20;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        parentHeight = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (rects == null) {
            int maxValue = Math.min(parentHeight, parentWidth);
            int squareSideSize = (maxValue - 2) / numberOfSquares;
            rects = new Rect[numberOfSquares * numberOfSquares];
            markedRects = new boolean[numberOfSquares * numberOfSquares];

            for (int i = 0; i < numberOfSquares; i++) {
                for (int u = 0; u < numberOfSquares; u++) {
                    rects[i * numberOfSquares + u] = new Rect(
                            1 + u * squareSideSize,
                            1 + i * squareSideSize,
                            1 + u * squareSideSize + squareSideSize,
                            1 + i * squareSideSize + squareSideSize);
                    markedRects[i * numberOfSquares + u] = false;
                }
            }
        }

        Rect rect;
        for (int i = 0; i < rects.length; i++) {
            rect = rects[i];
            canvas.drawRect(rects[i], mPaint);
            if (markedRects[i]) {
                // Draws the X in a marked Squared
                canvas.drawLine(rect.top, rect.left, rect.bottom, rect.right, mPaint);
                canvas.drawLine(rect.top, rect.right, rect.bottom, rect.left, mPaint);
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        int i = 0;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (Rect rect : rects) {
                    if (rect.left < x && x < rect.right && rect.bottom < y && y < rect.top) {
                        markedRects[i] = true;
                        invalidate();
                        i++;
                    }
                }
                break;
        }

        return true;
    }


}
