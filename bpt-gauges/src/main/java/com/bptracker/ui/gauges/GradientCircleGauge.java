package com.bptracker.ui.gauges;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.IntRange;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GradientCircleGauge extends LinearLayout {

    private Context mContext;
    private int mZeroAxisDegrees; // relative to the center. (270 = top)


    /** UI properties **/
    private boolean mBackgroundVisible;
    private float mRadius;
    private float mThickness; /** FIXME: allow changing radius and thickness **/
    private int bgColor;
    private int fgStartColor;
    private int fgCenterColor;
    private int fgEndColor;
    private String mTitleText;
    private float mTitleTextRadius;
    private float mTitleTextSize;
    private boolean mAnimationEnabled;


    /** Initial attribute property values **/
    private String mAttrProgressLabel;
    private String mAttrAdditionalLabel;
    private String mAttrAdditionalValue;


    /** UI elements **/
    private ProgressBar mProgressBarBg;
    private ProgressBar mProgressBarFg;
    private TextView mTextViewTopLabel;
    private TextView mTextViewAdditionalLabel;
    private TextView mTextViewAdditionalValue;

    private TextView mTextViewProgressLabel;
    private TextView mTextViewProgressValue;

    private final String LOG_TAG = GradientCircleGauge.class.getSimpleName();

    public GradientCircleGauge(Context context) {
        super(context);

        setInitDefaults(context);
        init();
    }

    public GradientCircleGauge(Context context, AttributeSet attrs) {
        super(context, attrs);

        setInitDefaults(context);

        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.GradientCircleGauge);
        try{
            mTitleText = t.getString(R.styleable.GradientCircleGauge_titleText);
            mBackgroundVisible = t.getBoolean(R.styleable.GradientCircleGauge_backgroundVisible, mBackgroundVisible);
            // TODO: add support to change the default radius and thickness
            //mRadius = t.getDimension(R.styleable.GradientCircleGauge_radius, mRadius);
            //mThickness = t.getDimension(R.styleable.GradientCircleGauge_thickness, mThickness);

            bgColor = t.getColor(R.styleable.GradientCircleGauge_backgroundColor, bgColor);
            fgStartColor = t.getColor(R.styleable.GradientCircleGauge_foregroundStartColor, fgStartColor);
            fgCenterColor = t.getColor(R.styleable.GradientCircleGauge_foregroundCenterColor, fgCenterColor);
            fgEndColor = t.getColor(R.styleable.GradientCircleGauge_foregroundEndColor, fgEndColor);
            mTitleTextRadius = t.getDimension(R.styleable.GradientCircleGauge_titleTextRadius, mTitleTextRadius);
            mTitleTextSize = t.getDimension(R.styleable.GradientCircleGauge_titleTextSize, mTitleTextSize);
            mAnimationEnabled = t.getBoolean(R.styleable.GradientCircleGauge_animation, mAnimationEnabled);

            mAttrAdditionalValue = t.getString(R.styleable.GradientCircleGauge_additionalValue);
            mAttrAdditionalLabel = t.getString(R.styleable.GradientCircleGauge_additionalLabel);
            mAttrProgressLabel = t.getString(R.styleable.GradientCircleGauge_progressLabel);

        } finally {
            t.recycle();
        }

        init();
    }

    public GradientCircleGauge(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    public GradientCircleGauge(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs);
    }

    private void setInitDefaults(Context context){
        mContext = context;

        mBackgroundVisible = true;
        mRadius = getResources().getDimension(R.dimen.default_circle_gauge_radius);
        mThickness = getResources().getDimension(R.dimen.default_circle_gauge_radius);
        mTitleText = "";
        bgColor = ContextCompat.getColor(context, R.color.default_bg_circle_gauge_color);
        fgStartColor = ContextCompat.getColor(context, R.color.default_fg_circle_gauge_start_color);
        fgCenterColor = ContextCompat.getColor(context, R.color.default_fg_circle_gauge_center_color);
        fgEndColor = ContextCompat.getColor(context, R.color.default_fg_circle_gauge_end_color);
        mTitleTextRadius = context.getResources().getDimension(R.dimen.default_circle_gauge_text_radius);
        mTitleTextSize = context.getResources().getDimension(R.dimen.default_circle_gague_text_size);
        mZeroAxisDegrees = 270; //top
        mAnimationEnabled = true;
    }


    private void init(){
        //Log.v(LOG_TAG, "initialize called");

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

        final View vRoot = inflater.inflate(R.layout.gradient_circle_gauge, null, false);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        vRoot.setLayoutParams(layoutParams);
        addView(vRoot);

        RelativeLayout rl = (RelativeLayout) this.getChildAt(0);

        mProgressBarBg = (ProgressBar) rl.getChildAt(0); //TODO: is there a better way to reference them
        mProgressBarFg = (ProgressBar) rl.getChildAt(1);

        LinearLayout ll = (LinearLayout) rl.getChildAt(2);

        mTextViewAdditionalValue = (TextView) ll.getChildAt(0);
        mTextViewAdditionalLabel = (TextView) ll.getChildAt(1);
        mTextViewProgressValue = (TextView) ll.getChildAt(2);
        mTextViewProgressLabel = (TextView) ll.getChildAt(3);

        if(!TextUtils.isEmpty(mAttrProgressLabel)){
            mTextViewProgressLabel.setText(mAttrProgressLabel);
        }

        if (!TextUtils.isEmpty(mAttrAdditionalValue)) {
            mTextViewAdditionalValue.setText(mAttrAdditionalValue);
            mTextViewAdditionalValue.setVisibility(VISIBLE);
        }

        if(!TextUtils.isEmpty(mAttrAdditionalLabel)){
            mTextViewAdditionalLabel.setText(mAttrAdditionalLabel);
            mTextViewAdditionalLabel.setVisibility(VISIBLE);
        }


        LayerDrawable ld = (LayerDrawable) mProgressBarBg.getProgressDrawable();
        GradientDrawable bgGradient = (GradientDrawable) ld.getDrawable(0);


        ld = (LayerDrawable) mProgressBarFg.getProgressDrawable();
        GradientDrawable fgGradient = (GradientDrawable) ld.getDrawable(0);


        if(bgColor != ContextCompat.getColor(mContext, R.color.default_bg_circle_gauge_color)){
            bgGradient.mutate();
            bgGradient.setColors(new int[]{bgColor, bgColor} );
        }

        if(fgStartColor != ContextCompat.getColor(mContext, R.color.default_fg_circle_gauge_start_color)){
            fgGradient.mutate();
            fgGradient.setColors(new int[]{fgStartColor, fgCenterColor, fgEndColor} );
        }

        mTextViewTopLabel = new CurvedTextView(mContext, mTitleTextSize, mTitleText, mTitleTextRadius);

        rl.addView(mTextViewTopLabel);

        mProgressBarBg.setVisibility(mBackgroundVisible ? VISIBLE : INVISIBLE);
        mProgressBarFg.setRotation(270); // progress starts at 0
    }

    /** for setGaugeLevel animations **/
    private void setProgressAnimation(int level){

        //    theta = level / 100
        //    rotation = [(360 - theta) / 2] + 90
        float rotation = (float)((360 - (level * 3.6 )) / 2) + mZeroAxisDegrees;
        mProgressBarFg.setRotation(rotation);

        mProgressBarFg.setProgress(level);
        setProgressTextValue(level + "%");
    }


    public void setGaugeLevel(@IntRange(from=0, to=100) int level){
        setGaugeLevel(level, false);
    }

    /**
     * Set the gauge to the passed in level
     * @param level             The progress level from 0 to 100s
     * @param pauseAnimation    Temporarily disable the update animation on the gauge. This
     *                          applies only when animations are enabled.
     */
    public void setGaugeLevel(@IntRange(from=0, to=100) int level, boolean pauseAnimation){

        if(level < 0 || level > 100){
            //TODO: create custom exception
            throw new RuntimeException("Gauge level is out of bounds: " + level);
        }

        if(mAnimationEnabled && !pauseAnimation){

            ObjectAnimator anim = ObjectAnimator.ofInt(this,
                    "progressAnimation", mProgressBarFg.getProgress(), level );
            //anim.setInterpolator(new DecelerateInterpolator(1f));
            anim.setDuration(700);
            anim.start();

        }else{
            setProgressAnimation(level);
        }
    }

    public int getGaugeLevel(){
        return mProgressBarFg.getProgress();
    }


    /**
     * Sets the textual progress indicator
     * @param text  The text to set
     */
    private void setProgressTextValue(String text) {
        mTextViewProgressValue.setText(text);
    }

    public String getAdditionalLabel(){
        return (String) mTextViewAdditionalLabel.getText();
    }

    public void setAdditionalLabel(String label) {
        mTextViewAdditionalLabel.setText(label);
    }

    public String getAdditionalValue(){
        return (String) mTextViewProgressValue.getText();
    }

    public void setAdditionalValue(String text) {
        mTextViewAdditionalValue.setText(text);
    }


    public String getProgressLabel(){
        return (String) mTextViewProgressValue.getText();
    }

    public void setProgressLabel(String label) {
        mTextViewProgressLabel.setText(label);
    }


    // inspired by: com.cardinalsolutions.progressindicator
    private class CurvedTextView extends TextView {
        private Path myArc;

        private Paint mPaintText;
        private Rect mRectTextBounds;
        private RectF mRectFTextOval;

        private float mTitleTextSize;
        private float mTitleTextRadius;

        public CurvedTextView(Context context, float textSize, String text, float textRadius) {
            super(context);
            initDefaults(context);

            mTitleTextRadius = textRadius;
            mTitleTextSize = textSize;

            mPaintText.setTextSize(mTitleTextSize);
            setText(text);
        }

        public CurvedTextView(Context context, AttributeSet ats) {
            super(context, ats);
            initDefaults(context);
        }

        public CurvedTextView(Context context) {
            super(context);
            initDefaults(context);
        }

        private void initDefaults(Context context) {
            myArc = new Path();

            mRectTextBounds = new Rect();
            mRectFTextOval = new RectF();

            mTitleTextSize = getResources().getDimension(R.dimen.default_circle_gague_text_size);
            mTitleTextRadius = getResources().getDimension(R.dimen.default_circle_gauge_text_radius);

            mPaintText = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaintText.setStyle(Paint.Style.FILL_AND_STROKE);
            mPaintText.setColor(getResources().getColor(R.color.circle_gauge_text_value_color));
            mPaintText.setTextSize(mTitleTextSize);

            setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

            setGravity(Gravity.CENTER);
            setWillNotDraw(false);
        }

        @Override
        protected void onDraw(Canvas canvas) {
           // Log.v(LOG_TAG, "CurvedTextView onDraw called");

            int centerXOnView = getWidth() / 2;
            int centerYOnView = getHeight() / 2;

            int viewXCenterOnScreen = getLeft() + centerXOnView;
            int viewYCenterOnScreen = getTop() + centerYOnView;


            int leftOffset = (int) (viewXCenterOnScreen - mTitleTextRadius );
            int topOffset = (int) (viewYCenterOnScreen - mTitleTextRadius );
            int rightOffset = (int) (viewXCenterOnScreen + mTitleTextRadius);
            int bottomOffset = (int) (viewYCenterOnScreen + mTitleTextRadius);

            mRectFTextOval.set(leftOffset, topOffset, rightOffset, bottomOffset);
            mPaintText.getTextBounds((String)getText(), 0, getText().length(), mRectTextBounds );

            // center the text top center
            float degrees = (float)((180 / Math.PI) * (mRectTextBounds.width() / mTitleTextRadius));
            int startAngle = (int)((180 - degrees) / 2);

            this.myArc.addArc(mRectFTextOval, 180 + startAngle, degrees);

            canvas.drawTextOnPath((String) getText(), this.myArc, 0, 0, this.mPaintText);
        }

    }
}