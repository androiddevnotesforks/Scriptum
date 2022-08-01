package sgtmelon.scriptum.cleanup.parent

import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Parent class for Presenter tests.
 */
@ExperimentalCoroutinesApi
abstract class ParentPresenterTest : ParentCoTest() {

    abstract fun onDestroy()
}