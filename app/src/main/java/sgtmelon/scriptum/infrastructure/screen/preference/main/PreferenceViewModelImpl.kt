package sgtmelon.scriptum.infrastructure.screen.preference.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSummaryUseCase
import sgtmelon.scriptum.infrastructure.model.key.Theme

class PreferenceViewModelImpl(
    private val preferencesRepo: PreferencesRepo,
    private val getSummary: GetSummaryUseCase
) : ViewModel(),
    PreferenceViewModel {

    override val theme: Theme get() = preferencesRepo.theme

    override val themeSummary by lazy { MutableLiveData(getSummary()) }

    override fun updateTheme(value: Int) {
        themeSummary.postValue(getSummary(value))
    }

    override val isDeveloper by lazy { MutableLiveData(preferencesRepo.isDeveloper) }

    override fun unlockDeveloper() {
        if (preferencesRepo.isDeveloper) return

        preferencesRepo.isDeveloper = true
        isDeveloper.postValue(true)
    }
}