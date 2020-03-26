package sgtmelon.scriptum

import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Parent class for Interactor tests.
 */
@ExperimentalCoroutinesApi
abstract class ParentInteractorTest : ParentCoTest() {

    abstract fun onDestroy()

}