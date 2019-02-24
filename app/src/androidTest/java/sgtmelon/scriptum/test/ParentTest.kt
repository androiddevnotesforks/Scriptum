package sgtmelon.scriptum.test

import android.content.Context
import androidx.annotation.CallSuper
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import org.junit.After
import org.junit.Before
import sgtmelon.scriptum.app.room.RoomDb
import sgtmelon.scriptum.office.annot.def.ThemeDef
import sgtmelon.scriptum.office.utils.PrefUtils

/**
 * Родительский класс включающий в себе объявление часто используемых переменных
 */
abstract class ParentTest {

    protected val context: Context = getInstrumentation().targetContext

    val prefUtils = PrefUtils(context)
    val db: RoomDb = RoomDb.provideDb(context)

    @Before
    @CallSuper
    open fun setUp() {
        when (Math.random() < 0.5) {
            true -> prefUtils.theme = ThemeDef.light
            false -> prefUtils.theme = ThemeDef.dark
        }
    }

    @After
    @CallSuper
    open fun tearDown() {

    }

}