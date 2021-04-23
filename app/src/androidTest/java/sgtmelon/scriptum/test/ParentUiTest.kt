package sgtmelon.scriptum.test

import android.content.Intent
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.UiDevice
import org.junit.Rule
import sgtmelon.scriptum.domain.model.annotation.Sort
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.idling.AppIdlingResource
import sgtmelon.scriptum.presentation.adapter.holder.RankHolder
import sgtmelon.scriptum.presentation.control.system.AlarmControl
import sgtmelon.scriptum.presentation.control.system.BindControl
import sgtmelon.scriptum.presentation.screen.ui.ScriptumApplication
import sgtmelon.scriptum.presentation.screen.ui.impl.SplashActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.notification.AlarmActivity
import sgtmelon.scriptum.ui.screen.SplashScreen
import kotlin.random.Random

/**
 * Parent class for UI tests.
 */
abstract class ParentUiTest : ParentTest() {

    protected val uiDevice: UiDevice = UiDevice.getInstance(instrumentation)

    // TODO make private
    @get:Rule val testRule = ActivityTestRule(
        SplashActivity::class.java, true, false
    )

    override fun setUp() {
        super.setUp()

        prepareDevice()

        preferenceRepo.apply {
            theme = if (Random.nextBoolean()) Theme.LIGHT else Theme.DARK
            firstStart = false

            sort = Sort.CHANGE

            autoSaveOn = false
            pauseSaveOn = false
        }

        data.clear()

        AlarmActivity.isFinishOnStop = false
    }

    private fun prepareDevice() {
        /**
         * Close all system windows (even opened statusBar), need for prevent interruptions
         * during tests run.
         */
        context.sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))

        /**
         * Включение wi-fi соединения, если вдруг тестировщик забыл его включить на девайсе.
         */
        uiDevice.executeShellCommand("svc wifi enable")
    }

    override fun tearDown() {
        super.tearDown()

        ScriptumApplication.theme = null

        RankHolder.isMaxTest = false
        AlarmActivity.isFinishOnStop = true

        BindControl.callback?.clearRecent()
        AlarmControl.callback?.clear()

        AppIdlingResource.getInstance().clearWork()
    }

    protected fun launch(
        before: () -> Unit = {},
        intent: Intent = Intent(),
        after: SplashScreen.() -> Unit
    ) {
        before()
        testRule.launchActivity(intent)
        SplashScreen(after)
    }

    protected fun launchBind(item: NoteItem, func: SplashScreen.() -> Unit) = launch(
        intent = SplashActivity.getBindInstance(context, item), after = func
    )

    protected fun launchInfo(func: SplashScreen.() -> Unit) = launch(
        intent = SplashActivity.getNotificationInstance(context), after = func
    )

    protected fun launchAlarm(item: NoteItem, func: SplashScreen.() -> Unit) = launch(
        intent = SplashActivity.getAlarmInstance(context, item.id), after = func
    )
}