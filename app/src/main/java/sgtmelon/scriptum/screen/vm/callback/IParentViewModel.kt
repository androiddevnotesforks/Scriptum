package sgtmelon.scriptum.screen.vm.callback

/**
 * Родительский интерфейс для общения с ViewModel
 */
interface IParentViewModel {

    fun onDestroy(func: () -> Unit = {})

}