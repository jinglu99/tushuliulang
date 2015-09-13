package com.zjut.tushuliulang.tushuliulang.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

import com.zjut.tushuliulang.tushuliulang.R;


/**
 * TODO: document your custom view class.
 */
public class TopIcon extends View {
    private String mtext; // TODO: use a default from R.string...
    private int mcolor; // TODO: use a default from R.color...
    private float msize = 0; // TODO: use a default from R.dimen...


//    private TextPaint mTextPaint;
//    private float mTextWidth;
//    private float mTextHeight;

    private Canvas canvas;



    private float malpha;


    private Rect textrect;
    private Paint textpaint;

    public TopIcon(Context context) {
        this(context, null);
//        init(null, 0);
    }

    public TopIcon(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
//        init(attrs, 0);
    }

    public TopIcon(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);

        textrect = new Rect();
        textpaint = new Paint();
        textpaint.setTextSize(msize);
        textpaint.setColor(Color.parseColor("#ff555555"));

        textpaint.getTextBounds(mtext,0,mtext.length(),textrect);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.TopIcon, defStyle, 0);

        mtext = a.getString(
                R.styleable.TopIcon_text);
        mcolor = a.getColor(
                R.styleable.TopIcon_iconcolor,
                mcolor);

        msize = a.getDimension(
                R.styleable.TopIcon_textsize,
                msize);


    }




        @Override
        protected void onDraw(Canvas canvas) {
//            canvas.drawBitmap(mdrawable, null, iconrect, null);
//            setbitmap((int) Math.ceil(255*malpha));
//            canvas.drawBitmap(bitmap,0,0,null);
            canvas.drawText(mtext,getMeasuredWidth()/2-textrect.width()/2,getMeasuredHeight()/2+textrect.height()/2,textpaint);
            drawsourcetext(canvas,(int) Math.ceil(255*malpha));
            drawtargettext(canvas,(int) Math.ceil(255*malpha));
            this.setAlpha(255-malpha);
            super.onDraw(canvas);



        }

    private void drawtargettext(Canvas canvas, int malpha) {
        textpaint.setColor(mcolor);
        textpaint.setAlpha(255-malpha);
        canvas.drawText(mtext,getMeasuredWidth()/2-textrect.width()/2,getMeasuredHeight()/2+textrect.height()/2,textpaint);

    }

    private void drawsourcetext(Canvas canvas, int malpha) {
        textpaint.setColor(0x00000000);
        textpaint.setAlpha(malpha);
        canvas.drawText(mtext, getMeasuredWidth()/2-textrect.width()/2,getMeasuredHeight()/2+textrect.height()/2, textpaint);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


    }



    public  void setalpha(float malpha)
    {
        this.malpha = malpha;
        invalidateview();
    }

    private void invalidateview() {
        if(
                Looper.getMainLooper() == Looper.myLooper()
                )
        {
            invalidate();
        }
        else
        {
            postInvalidate();
        }
    }
}


