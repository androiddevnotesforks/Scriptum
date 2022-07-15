package sgtmelon.scriptum.cleanup.presentation.screen.presenter

interface IParentPresenter {

    fun onDestroy(func: () -> Unit = {})
}