package sgtmelon.scriptum.tests.scenario.splash

import android.content.Intent
import sgtmelon.scriptum.infrastructure.screen.splash.SplashActivity
import sgtmelon.scriptum.tests.ui.auto.splash.SplashOpenTest
import sgtmelon.scriptum.tests.ui.auto.splash.SplashWrongTest

/**
 * Scenarios for test [SplashActivity].
 */
interface SplashScenario {

    /** Launch app with different [Intent] and via [SplashActivity] open screens. */
    fun openScreens() = SplashOpenTest()

    // TODO в данный момент не работает, надо добавить проверку id заметки на существование её
    //      в базе данных
    /** Similar to [openScreens], but put wrong data inside [Intent]. */
    fun openScreensWithWrongData() = SplashWrongTest()

}