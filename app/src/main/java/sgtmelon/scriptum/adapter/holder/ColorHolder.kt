package sgtmelon.scriptum.adapter.holder

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Fade
import androidx.transition.TransitionManager
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.ColorAdapter

/**
 * Держатель цвета приложения для [ColorAdapter]
 *
 * @author SerjantArbuz
 */
class ColorHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val parentContainer: ViewGroup = itemView.findViewById(R.id.color_parent_container)

    val backgroundView: View = itemView.findViewById(R.id.color_background_view)
    val checkImage: ImageView = itemView.findViewById(R.id.color_check_image)
    val clickView: View = itemView.findViewById(R.id.color_click_view)

    fun showCheck() = prepareCheckTransition { checkImage.visibility = View.VISIBLE }

    fun hideCheck() = prepareCheckTransition { checkImage.visibility = View.GONE }

    private fun prepareCheckTransition(func: () -> Unit) {
        val fade = Fade().setDuration(200).addTarget(checkImage)
        TransitionManager.beginDelayedTransition(parentContainer, fade)

        func()
    }

}