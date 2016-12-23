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

import appworld.gogogo.bsgame.interfaces.PlayerListener;

/**
 * Created by Raul on 04.11.2016.
 */
public class PlayGroundView extends View {

    private Paint linePaintGray, pointPaintWhite, selectedLinePaintRed, selectedLinePaintBlue, rectPaint;

    private lineOnCanvas[] verticalLinesOnCanvases;
    private lineOnCanvas[] horizontalLinesOnCanvas;
    private Rect[] rects;
    private boolean[] markedRects;

    private int numberOfSquares;
    private int parentWidth;
    private int parentHeight;
    private int player;

    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;

    private PlayerListener playerListener;

    public PlayGroundView(Context context, int numberOfSquares, PlayerListener playerListener) {
        super(context);
        this.numberOfSquares = numberOfSquares;
        this.playerListener = playerListener;

        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());

        player = 0;

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
        selectedLinePaintRed.setStrokeWidth(3);

        selectedLinePaintBlue = new Paint();
        selectedLinePaintBlue.setAntiAlias(true);
        selectedLinePaintBlue.setColor(Color.BLUE);
        selectedLinePaintBlue.setStyle(Paint.Style.STROKE);
        selectedLinePaintBlue.setStrokeWidth(3);


        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setColor(Color.DKGRAY);
        rectPaint.setStyle(Paint.Style.FILL);
        rectPaint.setStyle(Paint.Style.STROKE);
        rectPaint.setStrokeWidth(3);
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

        int maxValue = Math.min(parentHeight, parentWidth);


        // Draw the Lines for the PlayGround
        if (horizontalLinesOnCanvas == null && verticalLinesOnCanvases == null) {

            int lineLength = (maxValue - 22) / numberOfSquares;

            horizontalLinesOnCanvas = new lineOnCanvas[(numberOfSquares + 1) * numberOfSquares];
            verticalLinesOnCanvases = new lineOnCanvas[(numberOfSquares + 1) * (numberOfSquares)];

            for (int i = 0; i < numberOfSquares + 1; i++) {
                for (int u = 0; u < numberOfSquares; u++) {
                    horizontalLinesOnCanvas[u + i * numberOfSquares] = new lineOnCanvas(
                            11 + u * lineLength,
                            11 + i * lineLength,
                            11 + u * lineLength + lineLength,
                            11 + i * lineLength,
                            -1);

                    verticalLinesOnCanvases[u + i * numberOfSquares] = new lineOnCanvas(
                            11 + i * lineLength,
                            11 + u * lineLength,
                            11 + i * lineLength,
                            11 + u * lineLength + lineLength,
                            -1);
                }
            }
        }

        Paint paint = linePaintGray;

        if (horizontalLinesOnCanvas != null) {
            for (lineOnCanvas horizontalLineOnCanvas : horizontalLinesOnCanvas) {
                if (horizontalLineOnCanvas.player == 0) paint = selectedLinePaintRed;
                if (horizontalLineOnCanvas.player == 1) paint = selectedLinePaintBlue;
                canvas.drawLine(horizontalLineOnCanvas.startX, horizontalLineOnCanvas.startY, horizontalLineOnCanvas.stopX, horizontalLineOnCanvas.stopY, paint);
                canvas.drawPoint(horizontalLineOnCanvas.startX, horizontalLineOnCanvas.startY, pointPaintWhite);
                canvas.drawPoint(horizontalLineOnCanvas.stopX, horizontalLineOnCanvas.stopY, pointPaintWhite);
                paint = linePaintGray;

            }
        }

        if (verticalLinesOnCanvases != null) {
            for (lineOnCanvas verticalLineOnCanvases : verticalLinesOnCanvases) {
                if (verticalLineOnCanvases.player == 0) paint = selectedLinePaintRed;
                if (verticalLineOnCanvases.player == 1) paint = selectedLinePaintBlue;
                canvas.drawLine(verticalLineOnCanvases.startX, verticalLineOnCanvases.startY, verticalLineOnCanvases.stopX, verticalLineOnCanvases.stopY, paint);
                canvas.drawPoint(verticalLineOnCanvases.startX, verticalLineOnCanvases.startY, pointPaintWhite);
                canvas.drawPoint(verticalLineOnCanvases.stopX, verticalLineOnCanvases.stopY, pointPaintWhite);
                paint = linePaintGray;

            }
        }

        // Marked rects
        if (rects == null) {
            int squareSideSize = (maxValue - 2) / numberOfSquares;
            rects = new Rect[numberOfSquares * numberOfSquares];
            markedRects = new boolean[numberOfSquares * numberOfSquares];

            for (int i = 0; i < numberOfSquares; i++) {
                for (int u = 0; u < numberOfSquares; u++) {
                    rects[i * numberOfSquares + u] = new Rect(
                            11 + u * squareSideSize,
                            11 + i * squareSideSize,
                            11 + u * squareSideSize + squareSideSize,
                            11 + i * squareSideSize + squareSideSize);
                    markedRects[i * numberOfSquares + u] = false;
                }
            }

        }

        Rect rect;
        for (int i = 0; i < rects.length; i++) {
            rect = rects[i];
            if (markedRects[i]) {
                canvas.drawRect(rect, rectPaint);
                Log.v("tagged", String.valueOf(i));

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

        final int action = event.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {

                Log.v("getX", String.valueOf(x));
                Log.v("getY", String.valueOf(y));

                for (int i = 0; i < horizontalLinesOnCanvas.length; i++) {
                    if (horizontalLinesOnCanvas[i].player == -1) {
                        if (horizontalLinesOnCanvas[i].startY - 15 < y && horizontalLinesOnCanvas[i].stopY + 15 > y) {
                            if (horizontalLinesOnCanvas[i].startX < x && horizontalLinesOnCanvas[i].stopX > x) {
                                horizontalLinesOnCanvas[i].player = player;
                                playerListener.changePlayer(player);
                                player = 1 - player;
                            }
                        }
                    }
                }

                for (int i = 0; i < verticalLinesOnCanvases.length; i++) {
                    if (verticalLinesOnCanvases[i].player == -1) {
                        if (verticalLinesOnCanvases[i].startX - 15 < x && verticalLinesOnCanvases[i].stopX + 15 > x) {
                            if (verticalLinesOnCanvases[i].startY < y && verticalLinesOnCanvases[i].stopY > y) {
                                verticalLinesOnCanvases[i].player = player;
                                playerListener.changePlayer(player);
                                isVerticalRectFinished(verticalLinesOnCanvases, horizontalLinesOnCanvas, i);
                                player = 1 - player;
                            }
                        }
                    }
                }
            }
            break;
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

    private void isVerticalRectFinished(lineOnCanvas[] verticalLinesOnCanvas, lineOnCanvas[] horizontalLinesOnCanvas, int i) {
        // Check if the Rect to the left is Finished

        int spalte = (i) / (numberOfSquares);
        int reihe = (i) % (numberOfSquares);

        Log.v("index1", String.valueOf(i));
        if ((i / numberOfSquares) != 0) {
            Log.v("index2", String.valueOf(i));
            // Left Line
            if (verticalLinesOnCanvas[i - numberOfSquares].player != -1) {
                Log.v("index3", String.valueOf(i));
                // Top Line
                if (horizontalLinesOnCanvas[reihe * numberOfSquares + spalte - 1].player != -1) {
                    // Botttom Line
                    Log.v("index4", String.valueOf(i));
                    if (horizontalLinesOnCanvas[(reihe + 1) * numberOfSquares + spalte - 1].player != -1) {
                        Log.v("index5", String.valueOf(i));
                        markedRects[reihe * numberOfSquares + spalte -1] = true;
                    }
                }
            }
        }
        // Check if the Rect to the Right is Finished
    }

    /**
     * Object class to identify all of the Lines
     */
    private class lineOnCanvas {
        private int startX;
        private int startY;
        private int stopX;
        private int stopY;
        private int player;

        lineOnCanvas(int startX, int startY, int stopX, int stopY, int player) {
            this.startX = startX;
            this.startY = startY;
            this.stopX = stopX;
            this.stopY = stopY;
            this.player = player;
        }
    }
}

