package sgtmelon.scriptum.interactor

/**
 * Parent interface for communicate with children of [ParentInteractor]
 */
interface IParentInteractor {

    fun onDestroy(func: () -> Unit = {})

}