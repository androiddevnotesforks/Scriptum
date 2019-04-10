package sgtmelon.scriptum.test

import android.content.Context
import androidx.annotation.CallSuper
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import sgtmelon.scriptum.data.TestData
import sgtmelon.scriptum.office.annot.def.ThemeDef
import sgtmelon.scriptum.office.utils.Preference
import sgtmelon.scriptum.room.RoomDb
import sgtmelon.scriptum.screen.view.SplashActivity

/**
 * Родительский класс включающий в себе объявление часто используемых переменных
 *
 * @author SerjantArbuz
 */
abstract class ParentTest {

    @get:Rule val testRule = ActivityTestRule(
            SplashActivity::class.java, true, false
    )

    protected val context: Context = getInstrumentation().targetContext

    val preference = Preference(context)

    val testData = TestData(context)
    val db: RoomDb = RoomDb.getInstance(context)

    @Before @CallSuper open fun setUp() {
        when (Math.random() < 0.5) {
            true -> preference.theme = ThemeDef.light
            false -> preference.theme = ThemeDef.dark
        }
    }

    @After @CallSuper open fun tearDown() {

    }

}