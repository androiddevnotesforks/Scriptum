package sgtmelon.scriptum.adapter.holder

import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.Dimension
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Fade
import androidx.transition.TransitionManager
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.ColorAdapter
import sgtmelon.scriptum.model.item.ColorItem
import sgtmelon.scriptum.office.utils.getCompatColor
import sgtmelon.scriptum.office.utils.getDimen

/**
 * Держатель цвета приложения для [ColorAdapter]
 *
 * @author SerjantArbuz
 */
class ColorHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val context = view.context

    @Dimension private val strokeDimen = context.getDimen(value = 1f)

    private val parentContainer: ViewGroup = itemView.findViewById(R.id.color_parent_container)
    private val backgroundView: View = itemView.findViewById(R.id.color_background_view)

    val checkImage: ImageView = itemView.findViewById(R.id.color_check_image)
    val clickView: View = itemView.findViewById(R.id.color_click_view)

    fun bindColor(colorItem: ColorItem) = with(colorItem) {
        val fillColor = context.getCompatColor(fill)
        val strokeColor = context.getCompatColor(stroke)
        val checkColor = context.getCompatColor(check)

        (backgroundView.background as? GradientDrawable)?.apply {
            setColor(fillColor)
            setStroke(strokeDimen, strokeColor)
        }

        checkImage.setColorFilter(checkColor)
    }

    fun checkShow() {
        checkImage.visibility = View.VISIBLE
    }

    fun checkHide() {
        checkImage.visibility = View.GONE
    }

    fun animateCheckShow() = prepareCheckTransition { checkShow() }

    fun animateCheckHide() = prepareCheckTransition { checkHide() }

    private fun prepareCheckTransition(func: () -> Unit) {
        val fade = Fade().setDuration(200).addTarget(checkImage)
        TransitionManager.beginDelayedTransition(parentContainer, fade)

        func()
    }

}