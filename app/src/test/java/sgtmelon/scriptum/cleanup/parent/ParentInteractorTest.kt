package sgtmelon.scriptum.cleanup.parent

import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Parent class for Interactor tests.
 */
@ExperimentalCoroutinesApi
@Deprecated("Use normal parent test or rewrite it")
abstract class ParentInteractorTest : ParentCoTest() {

    open fun onDestroy() = Unit

}