package sgtmelon.scriptum.test

import android.content.Intent
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.screen.ui.SplashActivity
import sgtmelon.scriptum.ui.screen.SplashScreen
import kotlin.random.Random

/**
 * Parent class for UI tests
 */
abstract class ParentUiTest : ParentTest() {

    @get:Rule val testRule = ActivityTestRule(
            SplashActivity::class.java, true, false
    )

    override fun setUp() {
        super.setUp()

        iPreferenceRepo.apply {
            theme = if (Random.nextBoolean()) Theme.LIGHT else Theme.DARK
            firstStart = false

            autoSaveOn = false
            pauseSaveOn = false
        }

        data.clear()
    }

    protected fun launch(before: () -> Unit = {},
                         intent: Intent = Intent(),
                         after: SplashScreen.() -> Unit) {
        before()
        testRule.launchActivity(intent)
        SplashScreen().apply(after)
    }

    protected fun launchBind(noteModel: NoteModel, func: SplashScreen.() -> Unit) = launch(
            intent = SplashActivity.getBindInstance(context, noteModel.noteEntity), after = func
    )

    protected fun launchAlarm(noteModel: NoteModel, func: SplashScreen.() -> Unit) = launch(
            intent = SplashActivity.getAlarmInstance(context, noteModel.noteEntity), after = func
    )

}