package sgtmelon.scriptum.test.parent

import android.content.Intent
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.UiDevice
import org.junit.After
import org.junit.Before
import org.junit.Rule
import sgtmelon.scriptum.domain.model.annotation.Sort
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.idling.AppIdlingResource
import sgtmelon.scriptum.idling.WaitIdlingResource
import sgtmelon.scriptum.presentation.adapter.holder.RankHolder
import sgtmelon.scriptum.presentation.control.system.AlarmControl
import sgtmelon.scriptum.presentation.control.system.BindControl
import sgtmelon.scriptum.presentation.screen.ui.impl.SplashActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.notification.AlarmActivity
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.screen.SplashScreen
import kotlin.random.Random

/**
 * Parent class for UI tests.
 */
abstract class ParentUiTest : ParentTest() {

    @get:Rule val testRule = ActivityTestRule(
        SplashActivity::class.java, true, false
    )

    protected val uiDevice: UiDevice get() = UiDevice.getInstance(instrumentation)

    //region Setup

    @Before override fun setup() {
        super.setup()

        setupIdling()
        setupDevice()
        setupCompanionData()
    }

    /**
     * Call theme setup only with that function. Otherwise you get plenty assertion errors
     * related with theme. It's because need set [ParentUi.appTheme].
     */
    protected fun setupTheme(@Theme theme: Int) {
        ParentUi.theme = theme
        preferenceRepo.theme = theme
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
        setupTheme(if (Random.nextBoolean()) Theme.LIGHT else Theme.DARK)
        preferenceRepo.apply {
            firstStart = false

            sort = Sort.CHANGE

            autoSaveOn = false
            pauseSaveOn = false
        }

        /**
         * Prepare database.
         */
        data.clear()
    }

    private fun setupCompanionData() {
        AlarmActivity.isFinishOnStop = false
    }

    //endregion

    //region TearDown functions

    @After override fun tearDown() {
        super.tearDown()

        tearDownIdling()
        tearDownCompanionData()
    }

    private fun tearDownIdling() {
        WaitIdlingResource.getInstance().unregister()
        AppIdlingResource.getInstance().unregister()
    }

    private fun tearDownCompanionData() {
        ParentUi.theme = null

        RankHolder.isMaxTest = false
        AlarmActivity.isFinishOnStop = true

        BindControl.instance?.clearRecent()
        AlarmControl.instance?.clear()
    }

    //endregion

    //region Launch functions

    protected fun launch(before: () -> Unit = {}, after: SplashScreen.() -> Unit) {
        before()
        testRule.launchActivity(Intent())
        SplashScreen(after)
    }

    protected fun launchAlarm(
        item: NoteItem,
        before: () -> Unit = {},
        after: SplashScreen.() -> Unit
    ) {
        before()
        testRule.launchActivity(SplashActivity.getAlarmInstance(context, item.id))
        SplashScreen(after)
    }

    protected fun launchBind(
        item: NoteItem,
        before: () -> Unit = {},
        after: SplashScreen.() -> Unit
    ) {
        before()
        testRule.launchActivity(SplashActivity.getBindInstance(context, item))
        SplashScreen(after)
    }

    protected fun launchNotifications(before: () -> Unit = {}, after: SplashScreen.() -> Unit) {
        before()
        testRule.launchActivity(SplashActivity.getNotificationInstance(context))
        SplashScreen(after)
    }

    protected fun launchHelpDisappear(before: () -> Unit = {}, after: SplashScreen.() -> Unit) {
        before()
        testRule.launchActivity(SplashActivity.getHelpDisappearInstance(context))
        SplashScreen(after)
    }

    //endregion

}