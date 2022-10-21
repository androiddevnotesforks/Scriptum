package sgtmelon.scriptum.infrastructure.screen.preference.note

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSummaryUseCase
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.SavePeriod
import sgtmelon.scriptum.infrastructure.model.key.preference.Sort

class NotePreferenceViewModelImpl(
    private val preferencesRepo: PreferencesRepo,
    private val getSortSummary: GetSummaryUseCase,
    private val getDefaultColorSummary: GetSummaryUseCase,
    private val getSavePeriodSummary: GetSummaryUseCase
) : ViewModel(),
    NotePreferenceViewModel {

    override val sort: Sort get() = preferencesRepo.sort

    override val sortSummary = MutableLiveData(getSortSummary())

    override fun updateSort(value: Int) {
        sortSummary.postValue(getSortSummary(value))
    }

    override val defaultColor: Color get() = preferencesRepo.defaultColor

    override val defaultColorSummary = MutableLiveData(getDefaultColorSummary())

    override fun updateDefaultColor(value: Int) {
        defaultColorSummary.postValue(getDefaultColorSummary(value))
    }

    override val savePeriod: SavePeriod get() = preferencesRepo.savePeriod

    override val savePeriodSummary = MutableLiveData(getSavePeriodSummary())

    override fun updateSavePeriod(value: Int) {
        savePeriodSummary.postValue(getSavePeriodSummary(value))
    }
}