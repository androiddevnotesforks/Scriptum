package sgtmelon.scriptum.parent

import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Parent class for ViewModel tests.
 */
@ExperimentalCoroutinesApi
abstract class ParentViewModelTest : ParentCoTest() {

    abstract fun onDestroy()

}