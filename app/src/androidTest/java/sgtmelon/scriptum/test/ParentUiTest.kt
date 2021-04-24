package sgtmelon.scriptum.test

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.UiDevice
import org.junit.After
import org.junit.Before
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

    protected val uiDevice: UiDevice = UiDevice.getInstance(getInstrumentation())

    //region Setup functions

    @Before override fun setup() {
        super.setup()

        setupIdling()
        setupDevice()
        setupCompanionData()
    }

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

        BindControl.callback?.clearRecent()
        AlarmControl.callback?.clear()
    }

    //endregion

    //region Launch functions

    protected fun launch(
        before: () -> Unit = {},
        intent: Intent? = null,
        after: SplashScreen.() -> Unit
    ) {
        before()

        if (intent == null) {
            ActivityScenario.launch(SplashActivity::class.java).use { SplashScreen(after) }
        } else {
            ActivityScenario.launch<SplashActivity>(intent).use { SplashScreen(after) }
        }
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

    //endregion

}