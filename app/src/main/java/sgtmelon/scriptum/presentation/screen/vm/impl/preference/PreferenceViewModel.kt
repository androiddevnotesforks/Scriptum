package sgtmelon.scriptum.presentation.screen.vm.impl.preference

import android.app.Application
import android.os.Bundle
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.interactor.callback.preference.IPreferenceInteractor
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.presentation.screen.ui.callback.preference.IPreferenceFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.IPreferenceViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.ParentViewModel

/**
 * ViewModel for [IPreferenceFragment].
 */
class PreferenceViewModel(
    application: Application
) : ParentViewModel<IPreferenceFragment>(application),
    IPreferenceViewModel {

    private lateinit var interactor: IPreferenceInteractor

    fun setInteractor(interactor: IPreferenceInteractor) {
        this.interactor = interactor
    }


    override fun onSetup(bundle: Bundle?) {
        callback?.setupApp()
        callback?.setupOther()

        if (interactor.isDeveloper) {
            callback?.setupDeveloper()
        }

        callback?.updateThemeSummary(interactor.getThemeSummary())
    }

    override fun onClickTheme() {
        callback?.showThemeDialog(interactor.theme)
    }

    override fun onResultTheme(@Theme value: Int) {
        callback?.updateThemeSummary(interactor.updateTheme(value))
    }


    override fun onUnlockDeveloper() {
        if (interactor.isDeveloper) {
            callback?.showToast(R.string.pref_toast_develop_already)
        } else {
            interactor.isDeveloper = true
            callback?.setupDeveloper()
            callback?.showToast(R.string.pref_toast_develop_unlock)
        }
    }
}