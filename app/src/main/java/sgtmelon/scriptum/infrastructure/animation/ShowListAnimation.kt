package sgtmelon.scriptum.infrastructure.animation

import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.transition.Fade
import androidx.transition.Transition
import androidx.transition.TransitionManager
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.model.state.list.ShowListState
import sgtmelon.scriptum.infrastructure.utils.extensions.makeVisibleIf
import sgtmelon.test.idling.getWaitIdling

/**
 * Some tips for improve elements hide/show:
 * - Make only progress bar visible, hide others elements (empty info, recycler). Progress bar
 *   will be always displayed on clean screen open.
 */
class ShowListAnimation {

    fun start(
        showList: Pair<ShowListState, Boolean>,
        parentContainer: ViewGroup,
        progressBar: View,
        recyclerView: View,
        infoContainer: View
    ) {
        val (state, withAnimation) = showList
        if (withAnimation) {
            startFade(state, parentContainer, progressBar, recyclerView, infoContainer)
        } else {
            changeVisibility(state, progressBar, recyclerView, infoContainer)
        }
    }

    fun startFade(
        showList: ShowListState,
        parentContainer: ViewGroup,
        progressBar: View,
        recyclerView: View,
        infoContainer: View
    ) {
        /** Post needed for better UI performance. */
        parentContainer.rootView.post {
            val duration = parentContainer.resources.getInteger(R.integer.list_fade_time).toLong()
            val transition = getListTransition(duration, progressBar, recyclerView, infoContainer)

            TransitionManager.beginDelayedTransition(parentContainer, transition)

            getWaitIdling().start(duration)
            changeVisibility(showList, progressBar, recyclerView, infoContainer)
        }
    }

    /** Transition for animate hide and show of [targets] related with list. */
    private fun getListTransition(duration: Long, vararg targets: View): Transition {
        val transition = Fade()
            .setDuration(duration)
            .setInterpolator(AccelerateDecelerateInterpolator())

        for (view in targets) {
            transition.addTarget(view)
        }

        return transition
    }

    private fun changeVisibility(
        showList: ShowListState,
        progressBar: View,
        recyclerView: View,
        infoContainer: View
    ) {
        progressBar.makeVisibleIf(showList is ShowListState.Loading)
        recyclerView.makeVisibleIf(showList is ShowListState.List)
        infoContainer.makeVisibleIf(showList is ShowListState.Empty)
    }
}