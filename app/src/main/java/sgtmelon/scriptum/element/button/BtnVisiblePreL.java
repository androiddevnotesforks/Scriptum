package sgtmelon.scriptum.element.button;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageButton;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.office.Help;
import sgtmelon.scriptum.office.intf.IntfItem;

public class BtnVisiblePreL extends AppCompatImageButton implements IntfItem.Animation {

    final Context context;

    Drawable visibleOn;
    Drawable visibleOff;

    public BtnVisiblePreL(Context context) {
        super(context);

        this.context = context;
        setupDrawable();
    }

    public BtnVisiblePreL(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        setupDrawable();
    }

    public BtnVisiblePreL(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;
        setupDrawable();
    }

    void setupDrawable() {
        visibleOn = Help.Draw.get(context, R.drawable.ic_visible_on, R.attr.clAccent);
        visibleOff = Help.Draw.get(context, R.drawable.ic_visible_off, R.attr.clIcon);
    }

    @Override
    public void setDrawable(boolean drawableOn, boolean needAnim) {
        if (drawableOn) setImageDrawable(visibleOn);
        else setImageDrawable(visibleOff);
    }

}
