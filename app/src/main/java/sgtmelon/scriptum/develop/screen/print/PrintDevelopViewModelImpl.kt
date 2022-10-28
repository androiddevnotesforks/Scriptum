package sgtmelon.scriptum.develop.screen.print

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.extensions.runBack
import sgtmelon.scriptum.cleanup.extension.clearAdd
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.ParentViewModel
import sgtmelon.scriptum.develop.model.PrintItem
import sgtmelon.scriptum.develop.model.PrintType
import sgtmelon.scriptum.domain.interactor.preferences.DevelopInteractor
import sgtmelon.scriptum.infrastructure.model.data.IdlingTag
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Print.Default
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Print.Intent
import sgtmelon.test.idling.getIdling
import sgtmelon.test.prod.RunPrivate

/**
 * ViewModel for [IPrintDevelopActivity].
 */
class PrintDevelopViewModelImpl(
    callback: IPrintDevelopActivity,
    private val interactor: DevelopInteractor
) : ParentViewModel<IPrintDevelopActivity>(callback),
    IPrintDevelopViewModel {

    @RunPrivate val itemList: MutableList<PrintItem> = ArrayList()

    @RunPrivate var type: PrintType? = null

    override fun onSetup(bundle: Bundle?) {
        val typeOrdinal = bundle?.getInt(Intent.TYPE, Default.TYPE) ?: Default.TYPE
        val type = PrintType.values().getOrNull(typeOrdinal) ?: return
        this.type = type

        callback?.setupView(type)
        callback?.setupInsets()
    }

    override fun onSaveData(bundle: Bundle) = with(bundle) {
        putInt(Intent.TYPE, type?.ordinal ?: Default.TYPE)
    }

    override fun onUpdateData() {
        val type = type ?: return

        getIdling().start(IdlingTag.Print.LOAD_DATA)

        callback?.beforeLoad()

        fun updateList() = callback?.apply {
            notifyList(itemList)
            onBindingList()
        }

        /**
         * If was rotation need show list. After that fetch updates.
         */
        if (itemList.isNotEmpty()) {
            updateList()
        } else {
            callback?.showProgress()
        }

        viewModelScope.launch {
            runBack { itemList.clearAdd(interactor.getList(type)) }
            updateList()

            getIdling().stop(IdlingTag.Print.LOAD_DATA)
        }
    }
}