package sgtmelon.handynotes.element.button;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageButton;
import sgtmelon.handynotes.R;
import sgtmelon.handynotes.office.Help;
import sgtmelon.handynotes.office.intf.IntfItem;

public class BtnVisiblePreL extends AppCompatImageButton implements IntfItem.Animation {

    final Context context;

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

    Drawable visibleOn;
    Drawable visibleOff;

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
