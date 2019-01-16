package sgtmelon.iconanim.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.CallSuper;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.ContextCompat;
import sgtmelon.iconanim.R;
import sgtmelon.iconanim.office.intf.AnimIntf;

/**
 * Кнопка с автоматизацией процесса смены иконки
 */
public class SwitchButton extends AppCompatImageButton implements AnimIntf {

    public final static int SRC_NULL = -1;

    final Context context;

    @DrawableRes protected int srcDisable, srcSelect;
    @DrawableRes protected int srcDisableAnim, srcSelectAnim;
    @ColorInt protected int srcDisableColor, srcSelectColor;

    @Nullable protected Drawable drawableDisable;
    @Nullable protected Drawable drawableSelect;


    public SwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;

        setupAttribute(attrs);
        setupDrawable();
    }

    public SwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;

        setupAttribute(attrs);
        setupDrawable();
    }

    public final void setSrcDisable(@DrawableRes int srcDisable) {
        this.srcDisable = srcDisable;
    }

    public final void setSrcSelect(@DrawableRes int srcSelect) {
        this.srcSelect = srcSelect;
    }

    public final void setSrcDisableAnim(@DrawableRes int srcDisableAnim) {
        this.srcDisableAnim = srcDisableAnim;
    }

    public final void setSrcSelectAnim(@DrawableRes int srcSelectAnim) {
        this.srcSelectAnim = srcSelectAnim;
    }

    public final void setSrcDisableColor(@ColorInt int srcDisableColor) {
        this.srcDisableColor = srcDisableColor;
    }

    public final void setSrcSelectColor(@ColorInt int srcSelectColor) {
        this.srcSelectColor = srcSelectColor;
    }

    private void setupAttribute(AttributeSet attrs) {
        final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SwitchButton);

        srcDisable = array.getResourceId(R.styleable.SwitchButton_srcDisable, SRC_NULL);
        srcSelect = array.getResourceId(R.styleable.SwitchButton_srcSelect, SRC_NULL);

        srcDisableAnim = array.getResourceId(R.styleable.SwitchButton_srcDisableAnim, SRC_NULL);
        srcSelectAnim = array.getResourceId(R.styleable.SwitchButton_srcSelectAnim, SRC_NULL);

        srcDisableColor = array.getColor(R.styleable.SwitchButton_srcDisableColor, Color.BLACK);
        srcSelectColor = array.getColor(R.styleable.SwitchButton_srcSelectColor, Color.BLACK);

        array.recycle();
    }

    @CallSuper
    public void setupDrawable() {
        if (srcDisable != SRC_NULL) {
            drawableDisable = ContextCompat.getDrawable(context, srcDisable);
            if (drawableDisable != null) {
                drawableDisable.setColorFilter(srcDisableColor, PorterDuff.Mode.SRC_ATOP);
            }
        }

        if (srcSelect != SRC_NULL) {
            drawableSelect = ContextCompat.getDrawable(context, srcSelect);
            if (drawableSelect != null) {
                drawableSelect.setColorFilter(srcSelectColor, PorterDuff.Mode.SRC_ATOP);
            }
        }

        setImageDrawable(drawableSelect);
    }

    @Override
    public void setDrawable(boolean select, boolean needAnim) {
        setImageDrawable(select
                ? drawableSelect
                : drawableDisable);
    }

}