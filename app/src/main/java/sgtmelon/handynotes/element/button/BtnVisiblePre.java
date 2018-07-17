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
        visibleOn = Help.Icon.getDrawable(context, R.drawable.ic_visible_on);
        visibleOff = Help.Icon.getDrawable(context, R.drawable.ic_visible_off, R.color.colorIconSecond);
    }

    public void setVisible(boolean visible) {
        if (visible) setImageDrawable(visibleOn);
        else setImageDrawable(visibleOff);
    }

}
