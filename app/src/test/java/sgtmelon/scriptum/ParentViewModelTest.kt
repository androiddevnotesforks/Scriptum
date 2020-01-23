package sgtmelon.scriptum

import android.app.Application
import android.content.Context
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll

/**
 * Parent class for ViewModel tests.
 */
abstract class ParentViewModelTest : ParentTest() {

    @MockK protected lateinit var application: Application
    @MockK protected lateinit var context: Context

    override fun setUp() {
        super.setUp()
        MockKAnnotations.init(this, relaxUnitFun = true)

        every { application.applicationContext } returns context
    }

    override fun tearDown() {
        super.tearDown()
        unmockkAll()
    }

    abstract fun onDestroy()

}