package sgtmelon.scriptum.parent

import android.app.Application
import android.content.Context
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before

/**
 * Parent class for ViewModel tests.
 */
@ExperimentalCoroutinesApi
abstract class ParentViewModelTest : ParentCoTest() {

    @MockK protected lateinit var application: Application
    @MockK protected lateinit var context: Context

    @Before override fun setup() {
        super.setup()

        every { application.applicationContext } returns context
    }

    abstract fun onDestroy()

}