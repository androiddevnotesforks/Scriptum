package sgtmelon.scriptum.presentation.screen.vm.impl.preference

import android.app.Application
import android.os.Bundle
import sgtmelon.scriptum.domain.interactor.callback.preference.INotePreferenceInteractor
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.SavePeriod
import sgtmelon.scriptum.domain.model.annotation.Sort
import sgtmelon.scriptum.presentation.screen.ui.callback.preference.INotePreferenceFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.INotePreferenceViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.ParentViewModel

/**
 * ViewModel for [INotePreferenceFragment].
 */
class NotePreferenceViewModel(
    application: Application
) : ParentViewModel<INotePreferenceFragment>(application),
    INotePreferenceViewModel {

    private lateinit var interactor: INotePreferenceInteractor

    fun setInteractor(interactor: INotePreferenceInteractor) {
        this.interactor = interactor
    }


    override fun onSetup(bundle: Bundle?) {
        callback?.setup()

        callback?.updateSortSummary(interactor.getSortSummary())
        callback?.updateColorSummary(interactor.getDefaultColorSummary())
        callback?.updateSavePeriodSummary(interactor.getSavePeriodSummary())
    }

    override fun onClickSort() {
        callback?.showSortDialog(interactor.sort)
    }

    override fun onResultNoteSort(@Sort value: Int) {
        callback?.updateSortSummary(interactor.updateSort(value))
        callback?.sendNotifyNotesBroadcast()
    }

    override fun onClickNoteColor() {
        callback?.showColorDialog(interactor.defaultColor)
    }

    override fun onResultNoteColor(@Color value: Int) {
        callback?.updateColorSummary(interactor.updateDefaultColor(value))
    }

    override fun onClickSaveTime() {
        callback?.showSaveTimeDialog(interactor.savePeriod)
    }

    override fun onResultSaveTime(@SavePeriod value: Int) {
        callback?.updateSavePeriodSummary(interactor.updateSavePeriod(value))
    }
}