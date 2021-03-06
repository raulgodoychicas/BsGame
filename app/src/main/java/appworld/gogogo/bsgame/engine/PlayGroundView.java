package appworld.gogogo.bsgame.engine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import appworld.gogogo.bsgame.R;
import appworld.gogogo.bsgame.interfaces.PlayerListener;
import appworld.gogogo.bsgame.objects.LineOnCanvas;
import appworld.gogogo.bsgame.objects.MarkedRects;

/**
 * Created by Raul on 04.11.2016.
 */
public class PlayGroundView extends View {

    private Paint linePaintGray, selectedLinePaintP1, selectedLinePaintP2, rectPaint;

    private LineOnCanvas[] verticalLinesOnCanvases;
    private LineOnCanvas[] horizontalLinesOnCanvas;
    private Rect[] rects;
    private MarkedRects[] markedRects;

    private int numberOfSquares;
    private int parentWidth;
    private int parentHeight;
    private int player;

    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;

    private int gameMode;
    private boolean repeatCheck = true;

    private PlayerListener playerListener;

    public PlayGroundView(Context context) {
        super(context);
    }

    public PlayGroundView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public PlayGroundView(Context context, int gameModeInt, PlayerListener playerListener) {
        super(context);
        this.numberOfSquares = gameModeInt / 10;
        this.gameMode = gameModeInt % 10;
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

        getRootView().setBackgroundColor(getResources().getColor(R.color.Kastl));

        // Draw the Lines for the PlayGround
        if (horizontalLinesOnCanvas == null && verticalLinesOnCanvases == null) {

            int lineLength = (maxValue - 40) / numberOfSquares;

            horizontalLinesOnCanvas = new LineOnCanvas[(numberOfSquares + 1) * numberOfSquares];
            verticalLinesOnCanvases = new LineOnCanvas[(numberOfSquares + 1) * (numberOfSquares)];

            for (int i = 0; i < numberOfSquares + 1; i++) {
                for (int u = 0; u < numberOfSquares; u++) {
                    horizontalLinesOnCanvas[u + i * numberOfSquares] = new LineOnCanvas(
                            20 + u * lineLength,
                            20 + i * lineLength,
                            20 + u * lineLength + lineLength,
                            20 + i * lineLength,
                            -1);

                    verticalLinesOnCanvases[u + i * numberOfSquares] = new LineOnCanvas(
                            20 + i * lineLength,
                            20 + u * lineLength,
                            20 + i * lineLength,
                            20 + u * lineLength + lineLength,
                            -1);
                }
            }
        }

        Paint paint = linePaintGray;

        if (horizontalLinesOnCanvas != null) {
            for (LineOnCanvas horizontalLineOnCanvas : horizontalLinesOnCanvas) {
                if (horizontalLineOnCanvas.player == 0) paint = selectedLinePaintP1;
                if (horizontalLineOnCanvas.player == 1) paint = selectedLinePaintP2;
                canvas.drawLine(horizontalLineOnCanvas.startX, horizontalLineOnCanvas.startY, horizontalLineOnCanvas.stopX, horizontalLineOnCanvas.stopY, paint);
                canvas.drawPoint(horizontalLineOnCanvas.startX, horizontalLineOnCanvas.startY, linePaintGray);
                canvas.drawPoint(horizontalLineOnCanvas.stopX, horizontalLineOnCanvas.stopY, linePaintGray);
                paint = linePaintGray;
            }
        }

        if (verticalLinesOnCanvases != null) {
            for (LineOnCanvas verticalLineOnCanvases : verticalLinesOnCanvases) {
                if (verticalLineOnCanvases.player == 0) paint = selectedLinePaintP1;
                if (verticalLineOnCanvases.player == 1) paint = selectedLinePaintP2;
                canvas.drawLine(verticalLineOnCanvases.startX, verticalLineOnCanvases.startY, verticalLineOnCanvases.stopX, verticalLineOnCanvases.stopY, paint);
                canvas.drawPoint(verticalLineOnCanvases.startX, verticalLineOnCanvases.startY, linePaintGray);
                canvas.drawPoint(verticalLineOnCanvases.stopX, verticalLineOnCanvases.stopY, linePaintGray);
                paint = linePaintGray;
            }
        }

        // Marked rects
        if (rects == null) {
            int squareSideSize = (maxValue - 40) / numberOfSquares;
            rects = new Rect[numberOfSquares * numberOfSquares];
            markedRects = new MarkedRects[numberOfSquares * numberOfSquares];

            for (int i = 0; i < numberOfSquares; i++) {
                for (int u = 0; u < numberOfSquares; u++) {
                    rects[i * numberOfSquares + u] = new Rect(
                            20 + u * squareSideSize,
                            20 + i * squareSideSize,
                            20 + u * squareSideSize + squareSideSize,
                            20 + i * squareSideSize + squareSideSize);
                    markedRects[i * numberOfSquares + u] = new MarkedRects(false, -1);
                }
            }
        }

        Rect rect;
        for (int i = 0; i < rects.length; i++) {
            rect = rects[i];
            if (markedRects[i].marked) {
                if (markedRects[i].player == 0) {
                    rectPaint.setColor(selectedLinePaintP1.getColor());
                } else {
                    rectPaint.setColor(selectedLinePaintP2.getColor());
                }
                rectPaint.setAlpha((int) (255 * 0.54));
                canvas.drawRect(rect, rectPaint);
            }
        }

        boolean isGameFinished = true;
        for (MarkedRects markedRect : markedRects) {
            if (!markedRect.marked) {
                isGameFinished = false;
            }
        }
        if (repeatCheck && isGameFinished) {
            checkWinner();
            repeatCheck = false;
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
                        if (horizontalLinesOnCanvas[i].startY - 50 < y && horizontalLinesOnCanvas[i].stopY + 50 > y) {
                            if (horizontalLinesOnCanvas[i].startX < x && horizontalLinesOnCanvas[i].stopX > x) {
                                horizontalLinesOnCanvas[i].player = player;
                                if (isHorizontalRectFinished(verticalLinesOnCanvases, horizontalLinesOnCanvas, i)) {
                                    playerListener.changeScore(markedRects);
                                    // Change player again because if the rectangle is finish you get another turn
                                    changePlayer();
                                }
                                changePlayer();
                            }
                            playerListener.changePlayer(player);
                        }
                    }
                }
                for (int i = 0; i < verticalLinesOnCanvases.length; i++) {
                    if (verticalLinesOnCanvases[i].player == -1) {
                        if (verticalLinesOnCanvases[i].startX - 50 < x && verticalLinesOnCanvases[i].stopX + 50 > x) {
                            if (verticalLinesOnCanvases[i].startY < y && verticalLinesOnCanvases[i].stopY > y) {
                                verticalLinesOnCanvases[i].player = player;
                                if (isVerticalRectFinished(verticalLinesOnCanvases, horizontalLinesOnCanvas, i)) {
                                    playerListener.changeScore(markedRects);
                                    // Change player again because if the rectangle is finish you get another turn
                                    changePlayer();
                                }
                                changePlayer();
                            }
                            playerListener.changePlayer(player);
                        }
                    }
                }
            }

            if (gameMode == 1 && player == 1) {
                simulateKiChoice();
                Log.v("player", String.valueOf(player));
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
     * @param verticalLinesOnCanvas   Array with the vertical Lines
     * @param horizontalLinesOnCanvas Array with the horiziontal Lines
     * @param i                       index of the chosen Line
     * @return true if a Rect was closed
     */
    private boolean isVerticalRectFinished(LineOnCanvas[] verticalLinesOnCanvas, LineOnCanvas[] horizontalLinesOnCanvas, int i) {
        // Check if the Rect to the left is Finished

        boolean response = false;

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
                        response = true;
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
                        response = true;
                    }
                }
            }
        }
        return response;
    }

    /**
     * Check if a Rect is finished when an horizontal Line is chosen
     *
     * @param verticalLinesOnCanvas   Array with the vertical Lines
     * @param horizontalLinesOnCanvas Array with the horiziontal Lines
     * @param i                       index of the chosen Line
     * @return true if a Rect was closed
     */
    private boolean isHorizontalRectFinished(LineOnCanvas[] verticalLinesOnCanvas, LineOnCanvas[] horizontalLinesOnCanvas, int i) {

        boolean response = false;

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
                        response = true;
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
                        putMarkOnRect((reihe - 1) * numberOfSquares + spalte);
                        response = true;
                    }
                }
            }
        }

        return response;
    }

    /**
     * Define the different colors of the Lines
     * linePaintGray -
     * pointPaint -
     * selectedLinePaintP1 -
     * selectedLinePaintP2 -
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
        linePaintGray.setStrokeWidth(10);

        selectedLinePaintP1 = new Paint();
        selectedLinePaintP1.setAntiAlias(true);
        // material design 38% alpha for hints
        selectedLinePaintP1.setColor(getResources().getColor(R.color.Blaue_Striche));
        selectedLinePaintP1.setStyle(Paint.Style.STROKE);
        selectedLinePaintP1.setStrokeWidth(10);

        selectedLinePaintP2 = new Paint();
        selectedLinePaintP2.setAntiAlias(true);
        // material design 38% alpha for hints
        selectedLinePaintP2.setColor(getResources().getColor(R.color.Orange_Striche));
        selectedLinePaintP2.setStyle(Paint.Style.STROKE);
        selectedLinePaintP2.setStrokeWidth(10);

        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setColor(Color.DKGRAY);
        rectPaint.setStyle(Paint.Style.FILL);
        rectPaint.setStrokeWidth(5);
    }

    /**
     * Mark the chosen rectangle with the actual player number
     *
     * @param rectNum
     */
    private void putMarkOnRect(int rectNum) {
        markedRects[rectNum].marked = true;
        markedRects[rectNum].player = player;
    }

    /**
     * Change player
     */
    private void changePlayer() {
        player = 1 - player;
    }

    /**
     * This Method simulates the KI moves if the singleplayre mode is chosen.
     */
    private void simulateKiChoice() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int kiMoveInt;
                double randonInt = Math.random();

                if (randonInt > 0.5 && Ki.simulateKiMove(horizontalLinesOnCanvas) != -1) {
                    kiMoveInt = Ki.simulateKiMove(horizontalLinesOnCanvas);
                    horizontalLinesOnCanvas[kiMoveInt].player = player;
                    if (isHorizontalRectFinished(verticalLinesOnCanvases, horizontalLinesOnCanvas, kiMoveInt)) {
                        playerListener.changeScore(markedRects);
                        changePlayer();
                    }
                } else if (Ki.simulateKiMove(verticalLinesOnCanvases) != -1) {
                    kiMoveInt = Ki.simulateKiMove(verticalLinesOnCanvases);
                    verticalLinesOnCanvases[kiMoveInt].player = player;
                    if (isVerticalRectFinished(verticalLinesOnCanvases, horizontalLinesOnCanvas, kiMoveInt)) {
                        playerListener.changeScore(markedRects);
                        changePlayer();
                    }
                }
                changePlayer();
                playerListener.changePlayer(player);
                if (player == 1) {
                    simulateKiChoice();
                }
            }
        }, 1000);
    }

    private void checkWinner() {
        int player1points = 0;
        int player2points = 0;
        for (MarkedRects markedRect : markedRects) {
            if (markedRect.player == 0) player1points++;
            else if (markedRect.player == 1) player2points++;
        }
        if (player1points > player2points) {
            playerListener.onGameFinished("1", String.valueOf(player1points));
        } else if (player1points < player2points) {
            playerListener.onGameFinished("2", String.valueOf(player2points));
        }
    }
}

