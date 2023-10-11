package sgtmelon.scriptum.source.di

import android.app.Instrumentation
import android.content.Context
import android.content.res.Resources
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import sgtmelon.scriptum.infrastructure.screen.ScriptumApplication

/**
 * Injector for clear code and simple providing needed classes in UI tests.
 */
object TestInjector {

    private fun getInstrumentation(): Instrumentation =
        InstrumentationRegistry.getInstrumentation()

    fun getContext(): Context = getInstrumentation().targetContext

    fun getResources(): Resources = getContext().resources

    fun getApplication(): ScriptumApplication {
        return getInstrumentation().targetContext.applicationContext as ScriptumApplication
    }

    fun getUiDevice(): UiDevice = UiDevice.getInstance(getInstrumentation())
}