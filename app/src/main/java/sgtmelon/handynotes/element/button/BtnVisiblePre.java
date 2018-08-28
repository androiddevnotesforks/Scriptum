package sgtmelon.handynotes.element.button;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageButton;
import sgtmelon.handynotes.R;
import sgtmelon.handynotes.office.Help;

/**
 * Версия кнопки видимости категории для версии < API 21
 */
public class BtnVisiblePre extends AppCompatImageButton {

    private final Context context;

    public BtnVisiblePre(Context context) {
        super(context);

        this.context = context;

        setupDrawable();
    }

    public BtnVisiblePre(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;

        setupDrawable();
    }

    public BtnVisiblePre(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;

        setupDrawable();
    }

    private Drawable visibleOn, visibleOff;

    private void setupDrawable() {
        visibleOn = Help.Draw.get(context, R.drawable.ic_visible_on, R.attr.clAccent);
        visibleOff = Help.Draw.get(context, R.drawable.ic_visible_off, R.attr.clIcon);
    }

    public void setVisible(boolean visible) {
        if (visible) setImageDrawable(visibleOn);
        else setImageDrawable(visibleOff);
    }

}
