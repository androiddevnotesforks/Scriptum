package sgtmelon.scriptum.test

import android.content.Context

import org.junit.After
import org.junit.Before

import androidx.annotation.CallSuper

import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import sgtmelon.scriptum.office.utils.PrefUtils

/**
 * Родительский класс включающий в себе объявление часто используемых переменных
 */
abstract class ParentTest {

    private val context: Context = getInstrumentation().targetContext
    val prefUtils: PrefUtils = PrefUtils.getInstance(context)

    @Before
    @CallSuper
    open fun setUp() {

    }

    @After
    @CallSuper
    open fun tearDown() {

    }

}