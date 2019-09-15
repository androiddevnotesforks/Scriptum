package sgtmelon.scriptum.interactor.callback

/**
 * Parent interface for communicate with children of [ParentInteractor]
 */
interface IParentInteractor {

    fun onDestroy(func: () -> Unit = {})

}