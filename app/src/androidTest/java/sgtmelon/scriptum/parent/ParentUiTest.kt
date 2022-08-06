package sgtmelon.scriptum.parent

import android.content.Intent
import androidx.test.rule.ActivityTestRule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import sgtmelon.common.test.idling.impl.AppIdlingResource
import sgtmelon.common.test.idling.impl.WaitIdlingResource
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.control.system.AlarmControl
import sgtmelon.scriptum.cleanup.presentation.control.system.BindControl
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.SplashActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.notification.AlarmActivity
import sgtmelon.scriptum.cleanup.ui.ParentUi
import sgtmelon.scriptum.cleanup.ui.screen.SplashScreen
import sgtmelon.scriptum.infrastructure.model.key.Sort
import sgtmelon.scriptum.infrastructure.model.key.Theme
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.parent.di.ParentInjector

/**
 * Parent class for UI tests.
 */
abstract class ParentUiTest : ParentTest() {

    @get:Rule val testRule = ActivityTestRule(
        SplashActivity::class.java, true, false
    )

    protected val context = ParentInjector.provideContext()
    protected val preferences = ParentInjector.providePreferences()
    protected val preferencesRepo = ParentInjector.providePreferencesRepo()
    protected val db = ParentInjector.provideTestDbDelegator()
    protected val uiDevice = ParentInjector.provideUiDevice()

    //region SetUp functions

    @Before override fun setUp() {
        super.setUp()

        setupIdling()
        setupDevice()
        setupCompanionData()
    }

    private fun setupIdling() {
        WaitIdlingResource.getInstance().register()
        AppIdlingResource.getInstance().register()
    }

    private fun setupDevice() {
        /**
         * Close all system windows (even opened statusBar), need for prevent interruptions
         * during tests run.
         */
        context.sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))

        /**
         * Turn on wi-fi.
         */
        uiDevice.executeShellCommand("svc wifi enable")

        /**
         * Prepare preferences.
         */
        setupTheme(ThemeDisplayed.values().random())
        preferences.apply {
            isFirstStart = false

            isAutoSaveOn = false
            isPauseSaveOn = false
        }
        preferencesRepo.sort = Sort.CHANGE

        /**
         * Prepare database.
         */
        db.clear()
    }

    /**
     * Call theme setup only with that function. Otherwise you get plenty assertion errors
     * related with theme. It's because need set [ParentUi.appTheme].
     */
    protected fun setupTheme(theme: ThemeDisplayed) {
        ParentUi.theme = theme
        preferences.theme = when (theme) {
            ThemeDisplayed.LIGHT -> Theme.LIGHT
            ThemeDisplayed.DARK -> Theme.DARK
        }.ordinal
    }

    private fun setupCompanionData() {
        SplashActivity.isTesting = true
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
        WaitIdlingResource.getInstance().unregister()
        AppIdlingResource.getInstance().unregister()
    }

    private fun teardownCompanionData() {
        ParentUi.theme = null

        SplashActivity.isTesting = false
        AlarmActivity.isFinishOnStop = true

        BindControl.instance?.clearRecent()
        AlarmControl.instance?.clear()
    }

    //endregion

    //region Launch functions

    /**
     * This function not protected because used inside [ParentUiTest] extensions for
     * fast test run.
     */
    inline fun launch(
        before: () -> Unit = {},
        noinline after: SplashScreen.() -> Unit
    ) {
        before()
        testRule.launchActivity(Intent())
        SplashScreen(after)
    }

    protected inline fun launchAlarm(
        item: NoteItem,
        before: () -> Unit = {},
        noinline after: SplashScreen.() -> Unit
    ) {
        before()
        testRule.launchActivity(SplashActivity.getAlarmInstance(context, item.id))
        SplashScreen(after)
    }

    protected inline fun launchBind(
        item: NoteItem,
        before: () -> Unit = {},
        noinline after: SplashScreen.() -> Unit
    ) {
        before()
        testRule.launchActivity(SplashActivity.getBindInstance(context, item))
        SplashScreen(after)
    }

    protected inline fun launchNotifications(
        before: () -> Unit = {},
        noinline after: SplashScreen.() -> Unit
    ) {
        before()
        testRule.launchActivity(SplashActivity.getNotificationInstance(context))
        SplashScreen(after)
    }

    protected inline fun launchHelpDisappear(
        before: () -> Unit = {},
        noinline after: SplashScreen.() -> Unit
    ) {
        before()
        testRule.launchActivity(SplashActivity.getHelpDisappearInstance(context))
        SplashScreen(after)
    }

    //endregion
}