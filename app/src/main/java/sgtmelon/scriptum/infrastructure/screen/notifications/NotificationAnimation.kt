package sgtmelon.scriptum.infrastructure.screen.notifications

import androidx.transition.TransitionManager
import sgtmelon.scriptum.R
import sgtmelon.scriptum.databinding.ActivityNotificationBinding
import sgtmelon.scriptum.infrastructure.utils.getListTransition

class NotificationAnimation {

    inline fun startListAnimation(binding: ActivityNotificationBinding?, changeUi: () -> Unit) {
        if (binding == null) return

        with(binding) {
            val duration = root.resources.getInteger(R.integer.list_fade_time)
            val transition = getListTransition(
                duration, progressBar, recyclerView, infoInclude.parentContainer
            )

            TransitionManager.beginDelayedTransition(parentContainer, transition)

            changeUi()
        }
    }
}