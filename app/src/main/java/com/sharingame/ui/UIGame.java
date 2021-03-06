package com.sharingame.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.sharingame.entity.Game;
import com.sharingame.sharg.R;
import com.sharingame.sharg.ShargActivity;
import com.sharingame.sharg.fragment.UserFragmentGameDetails;

/**
 * TODO: document your custom view class.
 */
public class UIGame extends View implements View.OnClickListener {
    public static Game SELECTED_GAME = null;
    private String label; // TODO: use a default from R.string...
    private int labelColor = Color.WHITE; // TODO: use a default from R.color...
    private float iconSize = 0; // TODO: use a default from R.dimen...
    private Drawable icon;
    private int iconColor = Color.WHITE;
    private int textSize = 28;

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;

    private Game game;

    public UIGame(Context context) {
        super(context);
        init(null, 0);
    }

    public UIGame(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public UIGame(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.UIGame, defStyle, 0);

        label = a.getString(R.styleable.UIGame_label);
        labelColor = a.getColor(R.styleable.UIGame_labelColor, labelColor);
        iconSize = a.getDimension(R.styleable.UIGame_iconSize, iconSize);

        if (a.hasValue(R.styleable.UIGame_mIcon)) {
            icon = a.getDrawable(R.styleable.UIGame_mIcon);
            DrawableCompat.setTint(icon, iconColor);
            icon.setCallback(this);
        }

        a.recycle();

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
        setOnClickListener(this);
    }

    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize(textSize);
        mTextPaint.setColor(labelColor);
        mTextWidth = mTextPaint.measureText(label);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        // Draw the text.
        canvas.drawText(label,paddingLeft*2 + iconSize,paddingTop + (contentHeight + mTextHeight) / 2 + 10,
                mTextPaint);

        // Draw the example drawable on top of the text.
        if (icon != null) {
            icon.setBounds(paddingLeft, paddingTop, paddingLeft + (int)iconSize, paddingTop + (int)iconSize);
            icon.draw(canvas);
        }
    }

    public void setGame(Game game){
        this.game = game;
        setLabel(this.game.getTitle());
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
        invalidateTextPaintAndMeasurements();
    }

    public int getLabelColor() {
        return labelColor;
    }

    public void setLabelColor(int labelColor) {
        this.labelColor = labelColor;
        invalidateTextPaintAndMeasurements();
    }

    public float getIconSize() {
        return iconSize;
    }

    public void setIconSize(float iconSize) {
        this.iconSize = iconSize;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    @Override
    public void onClick(View v) {
        SELECTED_GAME = game;
        UserFragmentGameDetails fragmentDetails = new UserFragmentGameDetails();
        FragmentManager fm = ShargActivity.GetInstance().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_user_layout, fragmentDetails);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
        //Toast.makeText(getContext(), "Game Title: " + game.getTitle(), Toast.LENGTH_SHORT).show();
    }
}
