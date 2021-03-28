package sgtmelon.scriptum.presentation.screen.vm.impl.preference

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.domain.interactor.callback.preference.IPrintInteractor
import sgtmelon.scriptum.domain.model.annotation.test.RunPrivate
import sgtmelon.scriptum.domain.model.data.IntentData
import sgtmelon.scriptum.domain.model.data.IntentData.Print.Default
import sgtmelon.scriptum.domain.model.data.IntentData.Print.Intent
import sgtmelon.scriptum.domain.model.key.PrintType
import sgtmelon.scriptum.extension.clearAdd
import sgtmelon.scriptum.extension.runBack
import sgtmelon.scriptum.idling.AppIdlingResource
import sgtmelon.scriptum.idling.IdlingTag
import sgtmelon.scriptum.presentation.screen.ui.callback.preference.IPrintActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.IPrintViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.ParentViewModel

/**
 * ViewModel for [IPrintActivity].
 */
class PrintViewModel(
    application: Application
) : ParentViewModel<IPrintActivity>(application),
    IPrintViewModel {

    private lateinit var interactor: IPrintInteractor

    fun setInteractor(interactor: IPrintInteractor) {
        this.interactor = interactor
    }


    @RunPrivate val itemList: MutableList<Any> = ArrayList()

    @RunPrivate var type: PrintType? = null

    override fun onSetup(bundle: Bundle?) {
        val typeOrdinal = bundle?.getInt(Intent.TYPE, Default.TYPE) ?: Default.TYPE
        val type = PrintType.values().getOrNull(typeOrdinal) ?: return
        this.type = type

        callback?.setupView(type)
        callback?.setupInsets()
    }

    override fun onUpdateData() {
        AppIdlingResource.getInstance().startWork(IdlingTag.Print.LOAD_DATA)

        viewModelScope.launch {
            callback?.showProgress()

            runBack { itemList.clearAdd(interactor.getList()) }

            callback?.notifyList(itemList)
            callback?.onBindingList()

            AppIdlingResource.getInstance().stopWork(IdlingTag.Print.LOAD_DATA)
        }
    }

    override fun onSaveData(bundle: Bundle) = with(bundle) {
        putInt(IntentData.Note.Intent.TYPE, type?.ordinal ?: Default.TYPE)
    }
}