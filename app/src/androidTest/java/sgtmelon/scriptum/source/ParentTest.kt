package sgtmelon.scriptum.source

import androidx.annotation.CallSuper
import org.junit.After
import org.junit.Before
import sgtmelon.scriptum.cleanup.dagger.component.DaggerScriptumComponent
import sgtmelon.scriptum.cleanup.dagger.component.test.TestComponent
import sgtmelon.scriptum.infrastructure.screen.ScriptumApplication
import sgtmelon.scriptum.source.di.TestInjector

/**
 * Parent class for tests.
 */
abstract class ParentTest {

    @Before @CallSuper open fun setUp() {
        ScriptumApplication.isTesting = true
        inject()
    }

    open fun inject() {
        component = DaggerScriptumComponent.builder()
            .set(application = TestInjector.application)
            .build()
            .getTestComponent()
    }

    @After @CallSuper open fun tearDown() {
        ScriptumApplication.isTesting = false
    }

    companion object {
        lateinit var component: TestComponent
    }
}