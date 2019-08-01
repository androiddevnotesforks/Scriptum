package sgtmelon.scriptum.screen.vm.callback

/**
 * Родительский интерфейс для общения с ViewModel
 */
interface IParentViewModel {

    // TODO fun onSetup(bundle: Bundle? = null)

    fun onDestroy(func: () -> Unit = {})

}