package sgtmelon.scriptum.control.menu

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.view.Window
import androidx.appcompat.widget.Toolbar
import sgtmelon.iconanim.IconAnimControl
import sgtmelon.scriptum.R
import sgtmelon.scriptum.extension.blend
import sgtmelon.scriptum.extension.getAppSimpleColor
import sgtmelon.scriptum.extension.getAppThemeColor
import sgtmelon.scriptum.extension.getTintDrawable
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.key.ColorShade

/**
 * Класс для контроля меню | Для версий API < 21
 */
open class MenuControl(@Theme private val theme: Int,
                       protected val context: Context,
                       private val window: Window,
                       protected val toolbar: Toolbar?,
                       private val indicator: View?
) : IconAnimControl.Callback {

    private val statusOnDark = Build.VERSION.SDK_INT < Build.VERSION_CODES.M

    private val anim: ValueAnimator = ValueAnimator.ofFloat(0F, 1F)

    protected val cancelOn: Drawable? = context.getTintDrawable(R.drawable.ic_cancel_enter)
    protected val cancelOff: Drawable? = context.getTintDrawable(R.drawable.ic_cancel_exit)

    private var statusColorFrom: Int = 0
    private var statusColorTo: Int = 0
    private var toolbarColorFrom: Int = 0
    private var toolbarColorTo: Int = 0
    private var indicatorColorFrom: Int = 0
    private var indicatorColorTo: Int = 0

    init {
        val updateListener = ValueAnimator.AnimatorUpdateListener {
            val position = it.animatedFraction
            var blended: Int

            if (theme != Theme.dark && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                blended = statusColorFrom.blend(statusColorTo, position)
                window.statusBarColor = blended
            }

            blended = toolbarColorFrom.blend(toolbarColorTo, position)
            if (theme != Theme.dark) toolbar?.setBackgroundColor(blended)

            blended = indicatorColorFrom.blend(indicatorColorTo, position)
            if (theme == Theme.dark) indicator?.setBackgroundColor(blended)
        }

        anim.addUpdateListener(updateListener)
        anim.duration = context.resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
    }

    /**
     * Установка цвета для UI
     *
     * @param color - Начальный цвет
     */
    fun setColor(@Color color: Int) {
        if (theme != Theme.dark) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.statusBarColor = context.getAppThemeColor(color, statusOnDark)
            }
            toolbar?.setBackgroundColor(context.getAppThemeColor(color, false))
        }

        indicator?.setBackgroundColor(context.getAppThemeColor(color, true))

        setColorFrom(color)
    }

    /**
     * Установка начального цвета, [color] - начальный цвет
     */
    fun setColorFrom(@Color color: Int) {
        statusColorFrom = context.getAppThemeColor(color, statusOnDark)
        toolbarColorFrom = context.getAppThemeColor(color, needDark = false)
        indicatorColorFrom = context.getAppSimpleColor(color, ColorShade.DARK)
    }

    /**
     * Покраска UI элементов с анимацией, [color] - конечный цвет
     */
    fun startTint(@Color color: Int) {
        statusColorTo = context.getAppThemeColor(color, statusOnDark)
        toolbarColorTo = context.getAppThemeColor(color, needDark = false)
        indicatorColorTo = context.getAppSimpleColor(color, ColorShade.DARK)

        if (statusColorFrom != statusColorTo
                || toolbarColorFrom != toolbarColorTo
                || indicatorColorFrom != indicatorColorTo) anim.start()
    }

    override fun setDrawable(drawableOn: Boolean, needAnim: Boolean) {
        toolbar?.navigationIcon = if (drawableOn) cancelOn else cancelOff
    }

}