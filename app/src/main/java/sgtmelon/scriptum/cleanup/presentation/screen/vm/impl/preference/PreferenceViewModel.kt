package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.preference

import android.os.Bundle
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.interactor.callback.preference.IPreferenceInteractor
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.preference.IPreferenceFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.IPreferenceViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.ParentViewModel
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo

/**
 * ViewModel for [IPreferenceFragment].
 */
class PreferenceViewModel(
    callback: IPreferenceFragment,
    private val interactor: IPreferenceInteractor,
    private val preferencesRepo: PreferencesRepo
) : ParentViewModel<IPreferenceFragment>(callback),
    IPreferenceViewModel {

    override fun onSetup(bundle: Bundle?) {
        callback?.setupApp()
        callback?.setupOther()

        if (preferencesRepo.isDeveloper) {
            callback?.setupDeveloper()
        }

        callback?.updateThemeSummary(interactor.getThemeSummary())
    }

    override fun onClickTheme() {
        callback?.showThemeDialog(preferencesRepo.theme.ordinal)
    }

    override fun onResultTheme(value: Int) {
        callback?.updateThemeSummary(interactor.updateTheme(value))
    }


    override fun onUnlockDeveloper() {
        if (preferencesRepo.isDeveloper) {
            callback?.showToast(R.string.pref_toast_develop_already)
        } else {
            preferencesRepo.isDeveloper = true
            callback?.setupDeveloper()
            callback?.showToast(R.string.pref_toast_develop_unlock)
        }
    }
}