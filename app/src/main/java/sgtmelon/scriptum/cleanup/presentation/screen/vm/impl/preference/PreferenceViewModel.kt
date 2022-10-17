package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.preference

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.IPreferenceViewModel
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSummaryUseCase
import sgtmelon.scriptum.infrastructure.model.key.Theme

class PreferenceViewModel(
    private val preferencesRepo: PreferencesRepo,
    private val getSummary: GetSummaryUseCase
) : ViewModel(),
    IPreferenceViewModel {

    override val isDeveloper: MutableLiveData<Boolean> by lazy {
        MutableLiveData(preferencesRepo.isDeveloper)
    }

    override val theme: Theme get() = preferencesRepo.theme

    override val themeSummary: MutableLiveData<String> by lazy { MutableLiveData(getSummary()) }

    override fun updateTheme(value: Int) {
        themeSummary.postValue(getSummary(value))
    }

    override fun unlockDeveloper() {
        if (preferencesRepo.isDeveloper) return

        preferencesRepo.isDeveloper = true
        isDeveloper.postValue(true)
    }
}