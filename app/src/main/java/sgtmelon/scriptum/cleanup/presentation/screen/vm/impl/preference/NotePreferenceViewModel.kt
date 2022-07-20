package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.preference

import android.os.Bundle
import sgtmelon.scriptum.cleanup.domain.interactor.callback.preference.INotePreferenceInteractor
import sgtmelon.scriptum.cleanup.domain.model.annotation.Color
import sgtmelon.scriptum.cleanup.domain.model.annotation.SavePeriod
import sgtmelon.scriptum.cleanup.domain.model.annotation.Sort
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
    private val interactor: INotePreferenceInteractor
) : ParentViewModel<INotePreferenceFragment>(callback),
    INotePreferenceViewModel {

    override fun onSetup(bundle: Bundle?) {
        callback?.setup()

        callback?.updateSortSummary(getSortSummary())
        callback?.updateColorSummary(interactor.getDefaultColorSummary())
        callback?.updateSavePeriodSummary(interactor.getSavePeriodSummary())
    }

    override fun onClickSort() {
        callback?.showSortDialog(preferencesRepo.sort)
    }

    override fun onResultNoteSort(@Sort value: Int) {
        callback?.updateSortSummary(getSortSummary(value))
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