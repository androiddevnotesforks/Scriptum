package sgtmelon.scriptum.adapter.holder

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Fade
import androidx.transition.TransitionManager
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.ColorAdapter
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.office.utils.bindIndicatorColor
import sgtmelon.scriptum.office.utils.getCompatColor

/**
 * Держатель цвета приложения для [ColorAdapter]
 *
 * @author SerjantArbuz
 */
class ColorHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val context = view.context

    private val parentContainer: ViewGroup = itemView.findViewById(R.id.color_parent_container)
    private val backgroundView: View = itemView.findViewById(R.id.color_background_view)

    private val checkImage: ImageView = itemView.findViewById(R.id.color_check_image)
    val clickView: View = itemView.findViewById(R.id.color_click_view)

    fun bindColor(@Theme theme: Int, @Color color: Int) {
        val colorItem = backgroundView.bindIndicatorColor(theme, color)
        checkImage.setColorFilter(context.getCompatColor(colorItem.content))
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