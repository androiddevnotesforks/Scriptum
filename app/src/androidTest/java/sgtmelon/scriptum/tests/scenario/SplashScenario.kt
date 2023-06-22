package sgtmelon.scriptum.tests.scenario

import android.content.Intent
import sgtmelon.scriptum.infrastructure.screen.main.MainActivity
import sgtmelon.scriptum.infrastructure.screen.splash.SplashActivity
import sgtmelon.scriptum.infrastructure.screen.splash.SplashOpen
import sgtmelon.scriptum.tests.ui.auto.splash.SplashOpenTest
import sgtmelon.scriptum.tests.ui.auto.splash.SplashWrongTest

/**
 * Scenarios for test [SplashActivity].
 */
@Suppress("unused")
interface SplashScenario {

    /**
     * Launch app with different [Intent] and via [SplashActivity] open screens.
     * - Must be open screen/screens-chain declared in [SplashOpen].
     */
    fun openScreens() = SplashOpenTest()

    /**
     * Similar to [openScreens], but put wrong data inside [Intent].
     * - Must be open only [MainActivity], without screens where we setup wrong data.
     */
    fun openScreensWithWrongData() = SplashWrongTest()
}