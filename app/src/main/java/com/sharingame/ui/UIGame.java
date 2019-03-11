package com.sharingame.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.sharingame.sharg.R;

/**
 * TODO: document your custom view class.
 */
public class UIGame extends View {
    private String label; // TODO: use a default from R.string...
    private int labelColor = Color.WHITE; // TODO: use a default from R.color...
    private float iconSize = 0; // TODO: use a default from R.dimen...
    private Drawable icon;
    private int textSize = 28;

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;

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
            icon.setCallback(this);
        }

        a.recycle();

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
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
}
