package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.preference

import android.os.Bundle
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.preference.IPreferenceFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.IPreferenceViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.ParentViewModel
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSummaryUseCase

/**
 * ViewModel for [IPreferenceFragment].
 */
class PreferenceViewModel(
    callback: IPreferenceFragment,
    private val preferencesRepo: PreferencesRepo,
    private val getSummary: GetSummaryUseCase
) : ParentViewModel<IPreferenceFragment>(callback),
    IPreferenceViewModel {

    override fun onSetup(bundle: Bundle?) {
        callback?.setupApp()
        callback?.setupOther()

        if (preferencesRepo.isDeveloper) {
            callback?.setupDeveloper()
        }

        callback?.updateThemeSummary(getSummary())
    }

    override fun onClickTheme() {
        callback?.showThemeDialog(preferencesRepo.theme)
    }

    override fun onResultTheme(value: Int) {
        callback?.updateThemeSummary(getSummary(value))
    }


    override fun onUnlockDeveloper() {
        if (preferencesRepo.isDeveloper) {
            callback?.showToast(R.string.toast_dev_already)
        } else {
            preferencesRepo.isDeveloper = true
            callback?.setupDeveloper()
            callback?.showToast(R.string.toast_dev_unlock)
        }
    }
}