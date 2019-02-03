package sgtmelon.scriptum.app.control

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.view.Window
import androidx.appcompat.widget.Toolbar
import sgtmelon.iconanim.library.IconAnimControl
import sgtmelon.scriptum.R
import sgtmelon.scriptum.office.annot.def.ColorDef
import sgtmelon.scriptum.office.annot.def.ThemeDef
import sgtmelon.scriptum.office.utils.ColorUtils
import sgtmelon.scriptum.office.utils.PrefUtils

/**
 * Класс для контроля меню | Для версий API < 21
 */
open class MenuControl(protected val context: Context,
                       private val window: Window,
                       protected val toolbar: Toolbar,
                       private val indicator: View
) : IconAnimControl {

    private val statusOnDark = Build.VERSION.SDK_INT < Build.VERSION_CODES.M

    protected val anim: ValueAnimator = ValueAnimator.ofFloat(0F, 1F)

    protected val cancelOn: Drawable = ColorUtils
            .getDrawable(context, R.drawable.ic_cancel_enter, R.attr.clContent)!!
    protected val cancelOff: Drawable = ColorUtils
            .getDrawable(context, R.drawable.ic_cancel_exit, R.attr.clContent)!!

    private val valTheme: Int = PrefUtils.getInstance(context).theme

    private var statusColorFrom: Int = 0
    private var statusColorTo: Int = 0
    private var toolbarColorFrom: Int = 0
    private var toolbarColorTo: Int = 0

    init {
        val updateListener = ValueAnimator.AnimatorUpdateListener {
            val position = it.animatedFraction

            var blended = ColorUtils.blend(statusColorFrom, statusColorTo, position)
            if (valTheme != ThemeDef.dark && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.statusBarColor = blended
            }

            var background = ColorDrawable(blended)
            indicator.background = background

            blended = ColorUtils.blend(toolbarColorFrom, toolbarColorTo, position)
            background = ColorDrawable(blended)

            if (valTheme != ThemeDef.dark) {
                toolbar.background = background
            }
        }

        anim.addUpdateListener(updateListener)
        anim.duration = context.resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
    }

    /**
     * Установка цвета для UI
     *
     * @param color - Начальный цвет
     */
    fun setColor(@ColorDef color: Int) {
        if (valTheme != ThemeDef.dark) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.statusBarColor = ColorUtils.get(context, color, statusOnDark)
            }
            toolbar.setBackgroundColor(ColorUtils.get(context, color, false))
        }
        indicator.setBackgroundColor(ColorUtils.get(context, color, true))

        setColorFrom(color)
    }

    /**
     * Установка начального цвета
     *
     * @param color - Начальный цвет
     */
    fun setColorFrom(@ColorDef color: Int) {
        statusColorFrom = ColorUtils.get(context, color, statusOnDark)
        toolbarColorFrom = ColorUtils.get(context, color, false)
    }

    /**
     * Покраска UI элементов с анимацией
     *
     * @param color - Конечный цвет
     */
    fun startTint(@ColorDef color: Int) {
        statusColorTo = ColorUtils.get(context, color, statusOnDark)
        toolbarColorTo = ColorUtils.get(context, color, false)

        if (statusColorFrom != statusColorTo) {
            anim.start()
        }
    }

    override fun setDrawable(drawableOn: Boolean, needAnim: Boolean) {
        toolbar.navigationIcon = if (drawableOn) cancelOn else cancelOff
    }

}