package sgtmelon.scriptum.presentation.screen.presenter

interface IParentPresenter {

    fun onDestroy(func: () -> Unit = {})
}