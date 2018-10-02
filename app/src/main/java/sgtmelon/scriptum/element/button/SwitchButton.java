package sgtmelon.scriptum.element.button;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.ContextCompat;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.office.intf.IntfItem;

public class SwitchButton extends AppCompatImageButton implements IntfItem.Animation {

    final Context context;

    @ColorInt
    protected int colorDisable, colorSelect;

    @DrawableRes
    protected int srcDisable, srcSelect;

    @DrawableRes
    protected int srcDisableAnim, srcSelectAnim;

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

        colorSelect = attributes.getColor(R.styleable.SwitchButton_colorSelect, 0);// TODO: 02.10.2018 set default
        colorDisable = attributes.getColor(R.styleable.SwitchButton_colorDisable, 0);// TODO: 02.10.2018 set default

        srcDisable = attributes.getResourceId(R.styleable.SwitchButton_srcDisable, 0);// TODO: 02.10.2018 set default
        srcSelect = attributes.getResourceId(R.styleable.SwitchButton_srcSelect, 0);// TODO: 02.10.2018 set default

        srcDisableAnim = attributes.getResourceId(R.styleable.SwitchButton_srcDisableAnim, 0);// TODO: 02.10.2018 set default
        srcSelectAnim = attributes.getResourceId(R.styleable.SwitchButton_srcSelectAnim, 0);// TODO: 02.10.2018 set default

        attributes.recycle();
    }

    void setupDrawable() {
        drawableDisable = ContextCompat.getDrawable(context, srcDisable);
        if (drawableDisable != null) {
            drawableDisable.setColorFilter(colorDisable, PorterDuff.Mode.SRC_ATOP);
        }

        drawableSelect = ContextCompat.getDrawable(context, srcSelect);
        if (drawableSelect != null) {
            drawableSelect.setColorFilter(colorSelect, PorterDuff.Mode.SRC_ATOP);
        }

        setImageDrawable(drawableSelect);
    }

    @Override
    public void setDrawable(boolean select, boolean needAnim) {
        if (select) setImageDrawable(drawableSelect);
        else setImageDrawable(drawableDisable);
    }

}
