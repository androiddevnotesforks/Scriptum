package sgtmelon.scriptum.parent.ui.tests

import android.content.Intent
import androidx.test.core.app.launchActivity
import org.junit.After
import org.junit.Before
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.control.system.AlarmDelegatorImpl
import sgtmelon.scriptum.cleanup.presentation.control.system.BindDelegatorImpl
import sgtmelon.scriptum.cleanup.presentation.screen.ScriptumApplication
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.model.key.preference.Sort
import sgtmelon.scriptum.infrastructure.model.key.preference.Theme
import sgtmelon.scriptum.infrastructure.screen.Screens
import sgtmelon.scriptum.infrastructure.screen.alarm.AlarmActivity
import sgtmelon.scriptum.infrastructure.screen.splash.SplashActivity
import sgtmelon.scriptum.parent.ParentTest
import sgtmelon.scriptum.parent.di.ParentInjector
import sgtmelon.scriptum.parent.ui.screen.splash.SplashScreen
import sgtmelon.scriptum.parent.utils.getRandomSignalCheck
import sgtmelon.test.idling.getIdling
import sgtmelon.test.idling.getWaitIdling

/**
 * Parent class for UI tests.
 */
abstract class ParentUiTest : ParentTest() {

    val context = ParentInjector.provideContext()
    val resources = ParentInjector.provideResources()
    val preferences = ParentInjector.providePreferences()
    val preferencesRepo = ParentInjector.providePreferencesRepo()
    val db = ParentInjector.provideDbDelegator()
    val uiDevice = ParentInjector.provideUiDevice()

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
        resetLongPressTime()

        /** Prepare preferences. */
        setupTheme(ThemeDisplayed.values().random())

        preferencesRepo.isFirstStart = false
        preferences.apply {
            isAutoSaveOn = false
            isPauseSaveOn = false
        }
        preferencesRepo.sort = Sort.CHANGE
        preferencesRepo.isDeveloper = false
        preferencesRepo.signalTypeCheck = getRandomSignalCheck()

        /** Prepare database. */
        db.clear()
    }

    /**
     * Decrease long press time needed for fast access (when it's important) to long press
     * feature.
     */
    protected inline fun smallLongPressTime(onBetween: () -> Unit) {
        changeLongPressTime(timeMs = 100)
        onBetween()
        resetLongPressTime()
    }

    /**
     * Increase long press timeout, for preventing fake espresso click performed like a
     * long one.
     */
    protected fun resetLongPressTime() = changeLongPressTime(timeMs = 2000)

    protected fun changeLongPressTime(timeMs: Long) {
        uiDevice.executeShellCommand("settings put secure long_press_timeout $timeMs")
    }

    /**
     * Call theme setup only with that function. Otherwise you get plenty assertion errors
     * related with theme.
     */
    protected fun setupTheme(theme: ThemeDisplayed) {
        preferencesRepo.theme = when (theme) {
            ThemeDisplayed.LIGHT -> Theme.LIGHT
            ThemeDisplayed.DARK -> Theme.DARK
        }
    }

    private fun setupCompanionData() {
        ScriptumApplication.skipAnimation = true
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
        ScriptumApplication.skipAnimation = false
        AlarmActivity.isFinishOnStop = true

        BindDelegatorImpl[context].clearRecent()
        AlarmDelegatorImpl[context, null].clear()
    }

    //endregion

    //region Launch Splash functions

    inline fun launchSplash(before: () -> Unit, intent: Intent, after: SplashScreen.() -> Unit) {
        before()
        launchActivity<SplashActivity>(intent)
        SplashScreen(after)
    }

    @Deprecated("Replace with launchMain")
    inline fun launchSplash(before: () -> Unit = {}, after: SplashScreen.() -> Unit) {
        launchSplash(before, Screens.Splash.toMain(context), after)
    }

    inline fun launchSplashAlarm(
        item: NoteItem,
        before: () -> Unit = {},
        after: SplashScreen.() -> Unit
    ) {
        launchSplash(before, Screens.Splash.toAlarm(context, item.id), after)
    }

    inline fun launchSplashNotifications(before: () -> Unit = {}, after: SplashScreen.() -> Unit) {
        launchSplash(before, Screens.Splash.toNotification(context), after)
    }

    inline fun launchSplashBind(
        item: NoteItem,
        before: () -> Unit = {},
        after: SplashScreen.() -> Unit
    ) {
        launchSplash(before, Screens.Splash.toBindNote(context, item), after)
    }

    inline fun launchSplashNewNote(
        type: NoteType,
        before: () -> Unit = {},
        after: SplashScreen.() -> Unit
    ) {
        launchSplash(before, Screens.Splash.toNewNote(context, type), after)
    }

    //endregion

}