package sgtmelon.iconanim.app;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.ContextCompat;
import sgtmelon.iconanim.R;
import sgtmelon.iconanim.office.intf.AnimIntf;

public class SwitchButton extends AppCompatImageButton implements AnimIntf {

    final Context context;

    @DrawableRes
    protected int srcDisable, srcSelect;
    @DrawableRes
    protected int srcDisableAnim, srcSelectAnim;
    @ColorInt
    protected int srcDisableColor, srcSelectColor;

    Drawable drawableDisable, drawableSelect;

    public SwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;

        initAttribute(attrs);

        setupDrawable();
    }

    public SwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;

        initAttribute(attrs);

        setupDrawable();
    }

    private void initAttribute(AttributeSet attributeSet) {
        TypedArray attributes = context.obtainStyledAttributes(attributeSet, R.styleable.SwitchButton);

        srcDisable = attributes.getResourceId(R.styleable.SwitchButton_srcDisable, R.drawable.ic_android);
        srcSelect = attributes.getResourceId(R.styleable.SwitchButton_srcSelect, R.drawable.ic_android);

        srcDisableAnim = attributes.getResourceId(R.styleable.SwitchButton_srcDisableAnim, R.drawable.ic_android);
        srcSelectAnim = attributes.getResourceId(R.styleable.SwitchButton_srcSelectAnim, R.drawable.ic_android);

        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.srcDisableColor, typedValue, true);
        srcDisableColor = ContextCompat.getColor(context, typedValue.resourceId);

        context.getTheme().resolveAttribute(R.attr.srcSelectColor, typedValue, true);
        srcSelectColor = ContextCompat.getColor(context, typedValue.resourceId);

        attributes.recycle();
    }

    void setupDrawable() {
        drawableDisable = ContextCompat.getDrawable(context, srcDisable);
        if (drawableDisable != null) {
            drawableDisable.setColorFilter(srcDisableColor, PorterDuff.Mode.SRC_ATOP);
        }

        drawableSelect = ContextCompat.getDrawable(context, srcSelect);
        if (drawableSelect != null) {
            drawableSelect.setColorFilter(srcSelectColor, PorterDuff.Mode.SRC_ATOP);
        }

        setImageDrawable(drawableSelect);
    }

    @Override
    public void setDrawable(boolean select, boolean needAnim) {
        if (select) setImageDrawable(drawableSelect);
        else setImageDrawable(drawableDisable);
    }

}
