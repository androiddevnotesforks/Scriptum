package sgtmelon.scriptum.presentation.screen.vm.impl.preference

import android.app.Application
import android.os.Bundle
import sgtmelon.scriptum.domain.interactor.callback.preference.IPrintInteractor
import sgtmelon.scriptum.domain.model.annotation.test.RunPrivate
import sgtmelon.scriptum.domain.model.data.IntentData
import sgtmelon.scriptum.domain.model.data.IntentData.Print.Default
import sgtmelon.scriptum.domain.model.data.IntentData.Print.Intent
import sgtmelon.scriptum.domain.model.key.PrintType
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


    @RunPrivate var type: PrintType? = null

    override fun onSetup(bundle: Bundle?) {
        val typeOrdinal = bundle?.getInt(Intent.TYPE, Default.TYPE) ?: Default.TYPE
        val type = PrintType.values().getOrNull(typeOrdinal) ?: return
        this.type = type

        callback?.setupView(type)
        callback?.setupInsets()
    }

    override fun onSaveData(bundle: Bundle) = with(bundle) {
        putInt(IntentData.Note.Intent.TYPE, type?.ordinal ?: Default.TYPE)
    }
}