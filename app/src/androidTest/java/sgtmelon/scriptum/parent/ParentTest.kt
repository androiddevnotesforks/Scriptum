package sgtmelon.scriptum.parent

import androidx.annotation.CallSuper
import org.junit.After
import org.junit.Before
import sgtmelon.scriptum.parent.di.ParentInjector

/**
 * Parent class for tests
 */
abstract class ParentTest {

    protected val instrumentation = ParentInjector.provideInstrumentation()
    protected val context = ParentInjector.provideContext()
    protected val preferences = ParentInjector.providePreferences()
    protected val preferencesRepo = ParentInjector.providePreferencesRepo()

    @Before @CallSuper open fun setUp() = Unit

    @After @CallSuper open fun tearDown() = Unit

}