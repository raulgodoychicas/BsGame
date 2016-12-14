package appworld.gogogo.bsgame.engine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

/**
 * Created by Raul on 04.11.2016.
 */

public class PlayGroundView extends View {

    private Paint linePaintGray, pointPaintWhite, selectedLinePaintRed;

    private lineOnCanvas[] verticalLinesOnCanvases;
    private lineOnCanvas[] horizontalLinesOnCanvas;

    private int numberOfSquares;
    private int parentWidth;
    private int parentHeight;

    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;

    public PlayGroundView(Context context, int numberOfSquares) {
        super(context);
        this.numberOfSquares = numberOfSquares;

        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());

        // Define Paints
        linePaintGray = new Paint();
        linePaintGray.setAntiAlias(true);
        linePaintGray.setColor(Color.GRAY);
        linePaintGray.setStyle(Paint.Style.STROKE);
        // material design 38% alpha for hints
        linePaintGray.setAlpha((int) (255 * 0.38));
        linePaintGray.setStrokeWidth(3);

        pointPaintWhite = new Paint();
        pointPaintWhite.setAntiAlias(true);
        pointPaintWhite.setColor(Color.WHITE);
        pointPaintWhite.setStyle(Paint.Style.STROKE);
        pointPaintWhite.setStrokeWidth(9);

        selectedLinePaintRed = new Paint();
        selectedLinePaintRed.setAntiAlias(true);
        selectedLinePaintRed.setColor(Color.RED);
        selectedLinePaintRed.setStyle(Paint.Style.STROKE);
        selectedLinePaintRed.setStrokeWidth(9);
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

        if (horizontalLinesOnCanvas == null && verticalLinesOnCanvases == null) {
            int maxValue = Math.min(parentHeight, parentWidth);
            int lineLength = (maxValue - 2) / numberOfSquares;

            horizontalLinesOnCanvas = new lineOnCanvas[(numberOfSquares + 1) * numberOfSquares];
            verticalLinesOnCanvases = new lineOnCanvas[(numberOfSquares + 1) * (numberOfSquares)];

            for (int i = 0; i < numberOfSquares + 1; i++) {
                for (int u = 0; u < numberOfSquares; u++) {
                    horizontalLinesOnCanvas[u + i * numberOfSquares] = new lineOnCanvas(
                            1 + u * lineLength,
                            1 + i * lineLength,
                            1 + u * lineLength + lineLength,
                            1 + i * lineLength,
                            1);

                    verticalLinesOnCanvases[u + i * numberOfSquares] = new lineOnCanvas(
                            1 + i * lineLength,
                            1 + u * lineLength,
                            1 + i * lineLength,
                            1 + u * lineLength + lineLength,
                            1);
                }
            }
        }

        if (horizontalLinesOnCanvas != null) {
            for (lineOnCanvas horizontalLineOnCanvas : horizontalLinesOnCanvas) {
                canvas.drawLine(horizontalLineOnCanvas.startX, horizontalLineOnCanvas.startY, horizontalLineOnCanvas.stopX, horizontalLineOnCanvas.stopY, linePaintGray);
                canvas.drawPoint(horizontalLineOnCanvas.startX, horizontalLineOnCanvas.startY, pointPaintWhite);
                canvas.drawPoint(horizontalLineOnCanvas.stopX, horizontalLineOnCanvas.stopY, pointPaintWhite);
            }
        }

        if (verticalLinesOnCanvases != null) {
            for (lineOnCanvas verticalLineOnCanvases : verticalLinesOnCanvases) {
                canvas.drawLine(verticalLineOnCanvases.startX, verticalLineOnCanvases.startY, verticalLineOnCanvases.stopX, verticalLineOnCanvases.stopY, linePaintGray);
                canvas.drawPoint(verticalLineOnCanvases.startX, verticalLineOnCanvases.startY, pointPaintWhite);
                canvas.drawPoint(verticalLineOnCanvases.stopX, verticalLineOnCanvases.stopY, pointPaintWhite);
            }
        }

        invalidate();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Let the ScaleGestureDetector inspect all events.
        mScaleDetector.onTouchEvent(event);

        float x = event.getX();
        float y = event.getY();
        int i = 0;

        final int action = event.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {

//                for (Rect rect : rects) {
//                    if (rect.left < x && x < rect.right && rect.bottom < y && y < rect.top) {
//                        markedRects[i] = true;
//                        invalidate();
//                        i++;
//                    }
//                }

                break;
            }

        }
        invalidate();
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();

            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));

            invalidate();
            return true;
        }
    }

    /**
     * Object class to identify all of the Lines
     * Changes will still probably come
     */
    public class lineOnCanvas {
        int startX;
        int startY;
        int stopX;
        int stopY;
        int player;

        lineOnCanvas(int startX, int startY, int stopX, int stopY, int player) {
            this.startX = startX;
            this.startY = startY;
            this.stopX = stopX;
            this.stopY = stopY;
            this.player = player;
        }

    }


}

