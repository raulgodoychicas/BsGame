package appworld.gogogo.bsgame.engine;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Raul on 04.11.2016.
 */

public class PlayGroundView extends View implements View.OnTouchListener{

    private Context context;
    private Paint mPaint;
    private Activity activity;

    public PlayGroundView(Context context) {
        super(context);
    }

    public PlayGroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        context = this.context;
        // and we set a new Paint with the desired attributes
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect rect = new Rect(0, 0, getWidth(), getHeight());
        canvas.drawRect(rect, mPaint);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
