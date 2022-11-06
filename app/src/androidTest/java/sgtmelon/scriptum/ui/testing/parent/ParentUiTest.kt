package sgtmelon.scriptum.ui.testing.parent

import android.content.Intent
import androidx.test.rule.ActivityTestRule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import sgtmelon.scriptum.cleanup.presentation.control.system.AlarmControl
import sgtmelon.scriptum.cleanup.presentation.control.system.BindControl
import sgtmelon.scriptum.cleanup.ui.ParentScreen
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.infrastructure.model.key.preference.Sort
import sgtmelon.scriptum.infrastructure.model.key.preference.Theme
import sgtmelon.scriptum.infrastructure.screen.alarm.AlarmActivity
import sgtmelon.scriptum.infrastructure.screen.splash.SplashActivity
import sgtmelon.scriptum.parent.ParentTest
import sgtmelon.scriptum.parent.di.ParentInjector
import sgtmelon.test.idling.getIdling
import sgtmelon.test.idling.getWaitIdling

/**
 * Parent class for UI tests.
 */
abstract class ParentUiTest : ParentTest() {

    @get:Rule val testRule = ActivityTestRule(
        SplashActivity::class.java, true, false
    )

    val context = ParentInjector.provideContext()

    protected val preferences = ParentInjector.providePreferences()
    protected val preferencesRepo = ParentInjector.providePreferencesRepo()
    protected val db = ParentInjector.provideDbDelegator()
    protected val uiDevice = ParentInjector.provideUiDevice()

    //region SetUp functions

    @Before override fun setUp() {
        super.setUp()

        setupIdling()
        setupDevice()
        setupCompanionData()
    }

    private fun setupIdling() {
        getWaitIdling().register()
        getIdling().register()
    }

    private fun setupDevice() {
        /**
         * Close all system windows (even opened statusBar), need for prevent interruptions
         * during tests run.
         */
        context.sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))

        /** Turn on wi-fi. */
        uiDevice.executeShellCommand("svc wifi enable")

        /** Prepare preferences. */
        setupTheme(ThemeDisplayed.values().random())
        preferences.apply {
            isFirstStart = false
            isAutoSaveOn = false
            isPauseSaveOn = false
        }
        preferencesRepo.sort = Sort.CHANGE

        /** Prepare database. */
        db.clear()
    }

    /**
     * Call theme setup only with that function. Otherwise you get plenty assertion errors
     * related with theme. It's because need set [ParentScreen.appTheme].
     */
    protected fun setupTheme(theme: ThemeDisplayed) {
        ParentScreen.theme = theme

        // TODO check how it will work with preferencesRepo (not preferences)
        preferencesRepo.theme = when (theme) {
            ThemeDisplayed.LIGHT -> Theme.LIGHT
            ThemeDisplayed.DARK -> Theme.DARK
        }
    }

    private fun setupCompanionData() {
        AlarmActivity.isFinishOnStop = false
    }

    //endregion

    //region TearDown functions

    @After override fun tearDown() {
        super.tearDown()

        teardownIdling()
        teardownCompanionData()
    }

    private fun teardownIdling() {
        getWaitIdling().unregister()
        getIdling().unregister()
    }

    private fun teardownCompanionData() {
        ParentScreen.theme = null

        AlarmActivity.isFinishOnStop = true

        BindControl[context].clearRecent()
        AlarmControl.instance?.clear()
    }

    //endregion

}