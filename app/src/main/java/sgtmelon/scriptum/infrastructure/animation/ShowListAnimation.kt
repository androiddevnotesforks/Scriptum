package sgtmelon.scriptum.infrastructure.animation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.model.state.list.ShowListState
import sgtmelon.scriptum.infrastructure.utils.extensions.getAlphaAnimator
import sgtmelon.scriptum.infrastructure.utils.extensions.makeVisibleIf
import sgtmelon.scriptum.infrastructure.utils.extensions.resetAlpha
import sgtmelon.test.idling.getWaitIdling

/**
 * Some tips for improve elements hide/show:
 * - Make only progress bar visible, hide others elements (empty info, recycler). Progress bar
 *   will be always displayed on clean screen open.
 */
class ShowListAnimation {

    private var currentShowList: ShowListState? = null
    private var animator: Animator? = null

    fun start(
        showList: Pair<ShowListState, Boolean>,
        parentContainer: ViewGroup,
        progressBar: View,
        recyclerView: View,
        infoContainer: View
    ) {
        val (state, withAnimation) = showList

        /** If change list state to the same one. */
        if (currentShowList == state) return

        if (!withAnimation) {
            currentShowList = state
            updateVisibility(progressBar, recyclerView, infoContainer)
        } else {
            startFade(state, parentContainer, progressBar, recyclerView, infoContainer)
        }
    }

    private fun updateVisibility(
        progressBar: View,
        recyclerView: View,
        infoContainer: View
    ) {
        val showList = currentShowList ?: return

        progressBar.makeVisibleIf(showList is ShowListState.Loading)
        recyclerView.makeVisibleIf(showList is ShowListState.List)
        infoContainer.makeVisibleIf(showList is ShowListState.Empty)

        /**
         * Reset alpha is important, because in [start] function where is a case without
         * animation. That means may occur a situation when alpha=0, but view is [View.VISIBLE].
         */
        progressBar.resetAlpha()
        recyclerView.resetAlpha()
        infoContainer.resetAlpha()
    }

    private fun startFade(
        nextShowList: ShowListState,
        parentContainer: ViewGroup,
        progressBar: View,
        recyclerView: View,
        infoContainer: View
    ) {
        /** Must be called before value update. */
        stopPreviousAnimation(progressBar, recyclerView, infoContainer)

        val currentShowList = currentShowList ?: run {
            currentShowList = nextShowList
            updateVisibility(progressBar, recyclerView, infoContainer)
            return
        }

        this.currentShowList = nextShowList

        val currentView = getShowListView(currentShowList, progressBar, recyclerView, infoContainer)
        val nextView = getShowListView(nextShowList, progressBar, recyclerView, infoContainer)

        /** Post needed for better UI performance. */
        parentContainer.rootView.post {
            val duration = parentContainer.resources.getInteger(R.integer.list_fade_time).toLong()

            animator = buildAnimator(currentView, nextView, duration) {
                animator = null
                updateVisibility(progressBar, recyclerView, infoContainer)
            }
            animator?.start()

            getWaitIdling().start(duration)
        }
    }

    /** Prevent fast tapping lags (if animation is still running). */
    private fun stopPreviousAnimation(
        progressBar: View,
        recyclerView: View,
        infoContainer: View
    ) {
        if (animator != null) {
            animator?.cancel()
            animator = null
            updateVisibility(progressBar, recyclerView, infoContainer)
        }
    }

    private fun getShowListView(
        showList: ShowListState,
        progressBar: View,
        recyclerView: View,
        infoContainer: View
    ): View {
        return when (showList) {
            ShowListState.Loading -> progressBar
            ShowListState.List -> recyclerView
            ShowListState.Empty -> infoContainer
        }
    }

    private fun buildAnimator(
        currentView: View,
        nextView: View,
        duration: Long,
        onEnd: () -> Unit
    ): Animator {
        /**
         * Need skip case when first view to hide is [RecyclerView], because it already had basic
         * animation (of items hide).
         */
        val skipFirst = currentView is RecyclerView

        return AnimatorSet().apply {
            this.duration = duration
            this.interpolator = AccelerateDecelerateInterpolator()

            playSequentially(
                listOfNotNull(
                    if (skipFirst) null else currentView.getAlphaAnimator(visibleTo = false),
                    nextView.getAlphaAnimator(visibleTo = true)?.apply {
                        /** Shift this anim (to get correct sequence) like first isn't null. */
                        if (skipFirst) startDelay = duration
                    },
                )
            )

            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    onEnd()
                }
            })
        }
    }
}