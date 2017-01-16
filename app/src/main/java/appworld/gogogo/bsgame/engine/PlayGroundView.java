package appworld.gogogo.bsgame.engine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
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
    private markedRects[] markedRects;

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
        defineLineColors();
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
            int squareSideSize = (maxValue - 22) / numberOfSquares;
            rects = new Rect[numberOfSquares * numberOfSquares];
            markedRects = new markedRects[numberOfSquares * numberOfSquares];

            for (int i = 0; i < numberOfSquares; i++) {
                for (int u = 0; u < numberOfSquares; u++) {
                    rects[i * numberOfSquares + u] = new Rect(
                            11 + u * squareSideSize + 10,
                            11 + i * squareSideSize + 10,
                            11 + u * squareSideSize + squareSideSize - 10,
                            11 + i * squareSideSize + squareSideSize - 10);
                    markedRects[i * numberOfSquares + u] = new markedRects(false, -1);
                }
            }
        }

        Rect rect;
        for (int i = 0; i < rects.length; i++) {
            rect = rects[i];
            if (markedRects[i].marked) {
                if (markedRects[i].player == 0) {
                    rectPaint.setColor(selectedLinePaintRed.getColor());
                } else {
                    rectPaint.setColor(selectedLinePaintBlue.getColor());
                }
                canvas.drawRect(rect, rectPaint);
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

                // Close Rect on The Riht Line
                for (int i = 0; i < horizontalLinesOnCanvas.length; i++) {
                    if (horizontalLinesOnCanvas[i].player == -1) {
                        if (horizontalLinesOnCanvas[i].startY - 15 < y && horizontalLinesOnCanvas[i].stopY + 15 > y) {
                            if (horizontalLinesOnCanvas[i].startX < x && horizontalLinesOnCanvas[i].stopX > x) {
                                horizontalLinesOnCanvas[i].player = player;
                                playerListener.changePlayer(player);
                                if (isHorizontalRectFinished(verticalLinesOnCanvases, horizontalLinesOnCanvas, i)) {
                                    playerListener.changeScore();
                                    // Change player again because if the rectangel is finish you get another turn
                                    player = 1 - player;
                                }
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
                                if (isVerticalRectFinished(verticalLinesOnCanvases, horizontalLinesOnCanvas, i)) {
                                    playerListener.changeScore();
                                    // Change player again because if the rectangel is finish you get another turn
                                    player = 1 - player;
                                }
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


    /**
     * Check if a Rect is finished when a vertical Line is chosen
     *
     * @param verticalLinesOnCanvas     Array with the vertical Lines
     * @param horizontalLinesOnCanvas   Array with the horiziontal Lines
     * @param i                         index of the chosen Line
     * @return                          true if a Rect was closed
     */
    private boolean isVerticalRectFinished(lineOnCanvas[] verticalLinesOnCanvas, lineOnCanvas[] horizontalLinesOnCanvas, int i) {
        // Check if the Rect to the left is Finished

        int spalte = i / numberOfSquares;
        int reihe = i % numberOfSquares;

        // Check if there is no more Lines to the left
        if (spalte != 0) {
            // Left Line
            if (verticalLinesOnCanvas[i - numberOfSquares].player != -1) {
                // Top Line
                if (horizontalLinesOnCanvas[reihe * numberOfSquares + spalte - 1].player != -1) {
                    // Botttom Line
                    if (horizontalLinesOnCanvas[(reihe + 1) * numberOfSquares + spalte - 1].player != -1) {
                        putMarkOnRect(reihe * numberOfSquares + spalte - 1);
                        return true;
                    }
                }
            }
        }

        // Check if the Rect to the Right is Finished
        // Check if there are no more Lines to the Right
        if (spalte != numberOfSquares) {
            // Right Line
            if (verticalLinesOnCanvas[i + numberOfSquares].player != -1) {
                // Top Line
                if (horizontalLinesOnCanvas[reihe * numberOfSquares + spalte].player != -1) {
                    // Botttom Line
                    if (horizontalLinesOnCanvas[(reihe + 1) * numberOfSquares + spalte].player != -1) {
                        putMarkOnRect(reihe * numberOfSquares + spalte);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Check if a Rect is finished when an horizontal Line is chosen
     *
     * @param verticalLinesOnCanvas     Array with the vertical Lines
     * @param horizontalLinesOnCanvas   Array with the horiziontal Lines
     * @param i                         index of the chosen Line
     * @return                          true if a Rect was closed
     */
    private boolean isHorizontalRectFinished(lineOnCanvas[] verticalLinesOnCanvas, lineOnCanvas[] horizontalLinesOnCanvas, int i) {

        int reihe = i / numberOfSquares;
        int spalte = i % numberOfSquares;

        // Check if the Rect below is Finished
        // Check if there are Lines Below
        if (reihe != numberOfSquares) {
            // Bottom Line
            if (horizontalLinesOnCanvas[i + numberOfSquares].player != -1) {
                // Left Line
                if (verticalLinesOnCanvas[spalte * numberOfSquares + reihe].player != -1) {
                    // Right Line
                    if (verticalLinesOnCanvas[(spalte + 1) * numberOfSquares + reihe].player != -1) {
                        putMarkOnRect(reihe * numberOfSquares + spalte);
                        return true;
                    }
                }
            }
        }

        // Check if the Rect above is Finished
        // Check if there are Lines Above
        if (reihe != 0) {
            // Top Line
            if (horizontalLinesOnCanvas[i - numberOfSquares].player != -1) {
                // Left Line
                if (verticalLinesOnCanvas[spalte * numberOfSquares + (reihe - 1)].player != -1) {
                    // Right Line
                    if (verticalLinesOnCanvas[(spalte + 1) * numberOfSquares + (reihe - 1)].player != -1) {
                        putMarkOnRect((reihe-1) * numberOfSquares + spalte);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Define the different colors of the Lines
     * linePaintGray - used for the
     * pointPaintWhite -
     * selectedLinePaintRed -
     * selectedLinePaintBlue -
     * rectPaint -
     */
    private void defineLineColors() {

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
        rectPaint.setStrokeWidth(5);

    }

    private void putMarkOnRect(int rectNum) {
        markedRects[rectNum].marked = true;
        markedRects[rectNum].player = player;
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

    /**
     * Mark the different rectangles and set the Player to know
     *
     */
    private class markedRects {
        private boolean marked;
        private int player;

        markedRects(boolean marked, int player) {
            this.marked = marked;
            this.player = player;

        }

    }

}

