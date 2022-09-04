package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.preference.develop

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.common.test.annotation.RunPrivate
import sgtmelon.common.utils.runBack
import sgtmelon.scriptum.cleanup.domain.model.annotation.test.IdlingTag
import sgtmelon.scriptum.cleanup.domain.model.data.IntentData.Print.Default
import sgtmelon.scriptum.cleanup.domain.model.data.IntentData.Print.Intent
import sgtmelon.scriptum.cleanup.domain.model.item.PrintItem
import sgtmelon.scriptum.cleanup.domain.model.key.PrintType
import sgtmelon.scriptum.cleanup.extension.clearAdd
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.preference.develop.IPrintDevelopActivity
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.develop.IPrintDevelopViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.ParentViewModel
import sgtmelon.scriptum.domain.interactor.preferences.DevelopInteractor
import sgtmelon.test.idling.impl.AppIdlingResource

/**
 * ViewModel for [IPrintDevelopActivity].
 */
class PrintDevelopViewModel(
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

        AppIdlingResource.getInstance().startWork(IdlingTag.Print.LOAD_DATA)

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

            AppIdlingResource.getInstance().stopWork(IdlingTag.Print.LOAD_DATA)
        }
    }
}