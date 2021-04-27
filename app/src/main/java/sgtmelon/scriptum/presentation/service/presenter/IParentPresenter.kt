package sgtmelon.scriptum.presentation.service.presenter

interface IParentPresenter {

    fun onDestroy(func: () -> Unit = {})
}