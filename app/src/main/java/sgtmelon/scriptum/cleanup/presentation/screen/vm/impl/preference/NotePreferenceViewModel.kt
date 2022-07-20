package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.preference

import android.os.Bundle
import sgtmelon.scriptum.cleanup.domain.interactor.callback.preference.INotePreferenceInteractor
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.preference.INotePreferenceFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.INotePreferenceViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.ParentViewModel
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.preferences.GetSummaryUseCase

/**
 * ViewModel for [INotePreferenceFragment].
 */
class NotePreferenceViewModel(
    callback: INotePreferenceFragment,
    private val preferencesRepo: PreferencesRepo,
    private val getSortSummary: GetSummaryUseCase,
    private val getDefaultColorSummary: GetSummaryUseCase,
    private val interactor: INotePreferenceInteractor
) : ParentViewModel<INotePreferenceFragment>(callback),
    INotePreferenceViewModel {

    override fun onSetup(bundle: Bundle?) {
        callback?.setup()

        callback?.updateSortSummary(getSortSummary())
        callback?.updateColorSummary(getDefaultColorSummary())
        callback?.updateSavePeriodSummary(interactor.getSavePeriodSummary())
    }

    override fun onClickSort() {
        callback?.showSortDialog(preferencesRepo.sort)
    }

    override fun onResultNoteSort(value: Int) {
        callback?.updateSortSummary(getSortSummary(value))
        callback?.sendNotifyNotesBroadcast()
    }

    override fun onClickNoteColor() {
        callback?.showColorDialog(preferencesRepo.defaultColor)
    }

    override fun onResultNoteColor(value: Int) {
        callback?.updateColorSummary(getDefaultColorSummary(value))
    }

    override fun onClickSaveTime() {
        callback?.showSaveTimeDialog(preferencesRepo.savePeriod)
    }

    override fun onResultSaveTime(value: Int) {
        callback?.updateSavePeriodSummary(interactor.updateSavePeriod(value))
    }
}