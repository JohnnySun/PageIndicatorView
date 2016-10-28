package moe.johnny.learnpageindicatorview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

public class PageIndicatorView extends View implements ViewPager.OnPageChangeListener{

    private static final String DEFAULT_UNSELECTED_COLOR = "#33ffffff";
    private static final String DEFAULT_SELECTED_COLOR = "#ffffff";
    private int viewPagerId;
    private ViewPager viewPager;
    private int count;

    public PageIndicatorView(Context context) {
        super(context);
    }

    public PageIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public PageIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PageIndicatorView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        initAttrbutes(attrs);
    }

    private void initAttrbutes(@Nullable AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.PageIndicatorView, 0, 0);
        initCountAttribute(typedArray);
    }

    private void initCountAttribute(@NonNull TypedArray typedArray) {
        viewPagerId = typedArray.getResourceId(R.styleable.PageIndicatorView_piv_viewPage, 0);
        Log.d("Test", "id is " + Integer.toString(viewPagerId));
    }

    private void findViewPager() {
        View view = getRootView().findViewById(viewPagerId);
        if (view != null && view instanceof ViewPager) {
            setViewPager((ViewPager)view);
            Log.d("Test", "hello");
        }
    }

    private void setViewPager(@Nullable ViewPager viewPager) {
        if (viewPager != null) {
            this.viewPager = viewPager;
            viewPager.addOnPageChangeListener(this);
            count = viewPager.getAdapter().getCount();
            Log.d("Test", "Count is " + Integer.toString(count));
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        findViewPager();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int circleDiameterPx = dpToPx(6);
        int desiredWidth = 0;
        int desiredheight = circleDiameterPx;

        if(count != 0) {
            desiredWidth = (circleDiameterPx * count) * 2;
        }

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(desiredWidth, widthSize);
        } else {
            width = desiredWidth;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(desiredheight, heightSize);
        } else {
            height = desiredheight;
        }

        if (height < 0) {
            height = 0;
        }

        if (width < 0) {
            width = 0;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int y = getHeight() / 2;

        for (int i = 0; i < count; i++) {
            int x = getXpositonByCount(i);
            if (i == viewPager.getCurrentItem()){
                drawSelectedCircle(canvas, x, y);
            } else {
                drawUnSelectedCurcle(canvas, x, y);
            }
        }
    }

    private void drawSelectedCircle(Canvas canvas, int x, int y) {
        Paint paint = new Paint();
        paint.setColor(Color.parseColor(DEFAULT_SELECTED_COLOR));
        canvas.drawCircle(x, y, dpToPx(3), paint);
    }

    private void drawUnSelectedCurcle(Canvas canvas, int x, int y) {
        Paint paint = new Paint();
        paint.setColor(Color.parseColor(DEFAULT_UNSELECTED_COLOR));
        canvas.drawCircle(x, y, dpToPx(3), paint);
    }

    private int getXpositonByCount(int count) {
        int start = dpToPx(3);
        int next = dpToPx(12);
        return start + next * count;
    }

    private static int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }

}
