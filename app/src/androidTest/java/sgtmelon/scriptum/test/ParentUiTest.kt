package sgtmelon.scriptum.test

import android.content.Intent
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import sgtmelon.scriptum.domain.model.annotation.Sort
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.idling.AppIdlingResource
import sgtmelon.scriptum.presentation.adapter.holder.RankHolder
import sgtmelon.scriptum.presentation.control.system.AlarmControl
import sgtmelon.scriptum.presentation.control.system.BindControl
import sgtmelon.scriptum.presentation.screen.ui.impl.SplashActivity
import sgtmelon.scriptum.ui.screen.SplashScreen
import kotlin.random.Random

/**
 * Parent class for UI tests.
 */
abstract class ParentUiTest : ParentTest() {

    @get:Rule val testRule = ActivityTestRule(
        SplashActivity::class.java, true, false
    )

    override fun setUp() {
        super.setUp()

        preferenceRepo.apply {
            theme = if (Random.nextBoolean()) Theme.LIGHT else Theme.DARK
            firstStart = false

            sort = Sort.CHANGE

            autoSaveOn = false
            pauseSaveOn = false
        }

        data.clear()
    }

    override fun tearDown() {
        super.tearDown()

        RankHolder.isMaxTest = false

        BindControl.callback?.clearRecent()
        AlarmControl.callback?.clear()

        AppIdlingResource.worker.clearWork()
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