package sgtmelon.scriptum.tests.scenario.control

import sgtmelon.scriptum.tests.ui.control.anim.info.BinInfoAnimTest
import sgtmelon.scriptum.tests.ui.control.anim.info.NotesInfoAnimTest
import sgtmelon.scriptum.tests.ui.control.anim.info.NotificationsInfoAnimTest
import sgtmelon.scriptum.tests.ui.control.anim.info.RankInfoAnimTest

@Suppress("unused")
interface Animation {

    /**
     * For screen empty info. Main idea:
     * - If show info - show it smoothly with animation.
     * - If show list (only one item) - show it without lags.
     * - Animation must goes one by one - not all at one time.
     *
     * Check how it looks, must not be awkward.
     */
    interface ScreenInfo {

        fun rank() = RankInfoAnimTest()

        fun notes() = NotesInfoAnimTest()

        fun bin() = BinInfoAnimTest()

        fun notifications() = NotificationsInfoAnimTest()

        fun roll(): Nothing = run { TODO() }

    }
}