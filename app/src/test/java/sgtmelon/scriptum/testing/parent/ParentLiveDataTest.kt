package sgtmelon.scriptum.testing.parent

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Rule

/**
 * Parent class for Unit tests where classes use live data variables.
 */
abstract class ParentLiveDataTest : ParentTest() {

    @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()

}