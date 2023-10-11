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

    private val instrumentation: Instrumentation get() {
        return InstrumentationRegistry.getInstrumentation()
    }

    val context: Context get() = instrumentation.targetContext

    val resources: Resources get() = context.resources

    val application: ScriptumApplication get() {
        return instrumentation.targetContext.applicationContext as ScriptumApplication
    }

    val uiDevice: UiDevice get() = UiDevice.getInstance(instrumentation)
}