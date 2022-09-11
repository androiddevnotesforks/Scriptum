package sgtmelon.scriptum.infrastructure.screen

import android.graphics.drawable.AnimationDrawable
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import sgtmelon.scriptum.R

internal class GradientFabDelegatorImpl(
    private val activity: AppCompatActivity,
    private var isVisible: Boolean = true,
    private val onClick: (view: View) -> Unit
) : DefaultLifecycleObserver,
    GradientFabDelegator {

    private var parentCard: CardView? = null
    private var gradientView: View? = null
    private var clickView: View? = null

    private var gradientDrawable: AnimationDrawable? = null

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        setupViews()
        setupGradient()
    }

    private fun setupViews() {
        parentCard = activity.findViewById(R.id.gradient_fab_card)
        gradientView = activity.findViewById(R.id.gradient_fab_anim)
        clickView = activity.findViewById(R.id.gradient_fab_click)

        clickView?.setOnClickListener { onClick(it) }
    }

    private fun setupGradient() {
        val resources = activity.resources

        val gradient = gradientView?.background as? AnimationDrawable
        gradient?.setEnterFadeDuration(resources.getInteger(R.integer.gradient_enter_time))
        gradient?.setExitFadeDuration(resources.getInteger(R.integer.gradient_exit_time))
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)

        parentCard = null
        gradientView = null
        clickView = null

        changePlay(isPlay = false)
        gradientDrawable = null
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        changePlay(isPlay = true)
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        changePlay(isPlay = false)
    }

    private fun changePlay(isPlay: Boolean) {
        if (isPlay) {
            gradientDrawable?.start()
        } else {
            gradientDrawable?.stop()
        }
    }

    override fun changeVisibility(isVisible: Boolean) {
        this.isVisible = isVisible

        changePlay(isVisible)

        parentCard?.isEnabled = isVisible
        clickView?.isEnabled = isVisible

        if (isVisible) show() else hide()
    }

    // TODO animation
    private fun show() {
        parentCard?.visibility = View.VISIBLE
    }

    // TODO animation
    private fun hide() {
        parentCard?.visibility = View.GONE
    }
}