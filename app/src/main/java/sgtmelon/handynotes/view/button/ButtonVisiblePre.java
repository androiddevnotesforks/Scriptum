package sgtmelon.handynotes.view.button;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.office.Help;

/**
 * Версия кнопки видимости категории для версии < API 21
 */
public class ButtonVisiblePre extends AppCompatImageButton {

    private final Context context;

    public ButtonVisiblePre(Context context) {
        super(context);

        this.context = context;

        setupDrawable();
    }

    public ButtonVisiblePre(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;

        setupDrawable();
    }

    public ButtonVisiblePre(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;

        setupDrawable();
    }

    private Drawable visibleOn, visibleOff;

    private void setupDrawable() {
        visibleOn = Help.Icon.getDrawable(context, R.drawable.ic_button_visible_on);
        visibleOff = Help.Icon.getDrawable(context, R.drawable.ic_button_visible_off, R.color.colorDarkSecond);
    }

    public void setVisible(boolean visible) {
        if (visible) setImageDrawable(visibleOn);
        else setImageDrawable(visibleOff);
    }

}
