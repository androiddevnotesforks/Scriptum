package sgtmelon.scriptum.office.utils

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.MenuItem
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.model.data.ColorData
import sgtmelon.scriptum.office.annot.def.ColorDef
import sgtmelon.scriptum.office.annot.def.ThemeDef

object ColorUtils {

    /**
     * Получение цвета заметки в зависимости от темы и заднего фона
     *
     * @param color    - Идентификатор цвета заметки
     * @param needDark - Если элемент находится на тёмном фоне (например индикатор цвета заметки
     * @return - Один из стандартных цветов приложения
     */
    @ColorInt
    fun get(context: Context, @ColorDef color: Int, needDark: Boolean): Int {
        return when (PrefUtils(context).theme) {
            ThemeDef.light -> {
                if (needDark) ContextCompat.getColor(context, ColorData.dark[color])
                else ContextCompat.getColor(context, ColorData.light[color])
            }
            ThemeDef.dark -> {
                if (needDark) ContextCompat.getColor(context, ColorData.dark[color])
                else context.getColorAttr(R.attr.clPrimary)
            }
            else -> {
                if (needDark) ContextCompat.getColor(context, ColorData.dark[color])
                else context.getColorAttr(R.attr.clPrimary)
            }
        }
    }

    /**
     * Получение цвета по аттрибуту, [attr] - аттрибут цвета
     */
    @ColorInt
    fun Context.getColorAttr(@AttrRes attr: Int): Int {
        val typedValue = TypedValue()

        theme.resolveAttribute(attr, typedValue, true)

        return ContextCompat.getColor(this, typedValue.resourceId)
    }

    /**
     * Получение RGB промежуточного цвета в записимости от значения трансформации
     * [ratio] - положение трансформации
     * [this] - цвет от которого идёт трансформация
     */
    @ColorInt
    fun Int.blend(colorTo: Int, @FloatRange(from = 0.0, to = 1.0) ratio: Float): Int {
        val inverseRatio = 1f - ratio

        val r = Color.red(colorTo) * ratio + Color.red(this) * inverseRatio
        val g = Color.green(colorTo) * ratio + Color.green(this) * inverseRatio
        val b = Color.blue(colorTo) * ratio + Color.blue(this) * inverseRatio

        return Color.rgb(r.toInt(), g.toInt(), b.toInt())
    }

    /**
     * Покараска элемента меню в стандартный цвет
     */
    fun MenuItem.tintIcon(context: Context) {
        val drawable = this.icon
        val wrapDrawable = DrawableCompat.wrap(drawable)

        DrawableCompat.setTint(wrapDrawable, context.getColorAttr(R.attr.clContent))

        this.icon = wrapDrawable
    }

    /**
     * Получение покрашенного изображения
     */
    fun Context.getDrawable(@DrawableRes id: Int, @AttrRes attr: Int): Drawable? {
        val drawable = ContextCompat.getDrawable(this, id) ?: return null
        drawable.setColorFilter(getColorAttr(attr), PorterDuff.Mode.SRC_ATOP)

        return drawable
    }

}