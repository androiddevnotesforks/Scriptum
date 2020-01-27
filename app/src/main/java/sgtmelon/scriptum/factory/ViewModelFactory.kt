package sgtmelon.scriptum.factory

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import sgtmelon.scriptum.screen.ui.AppActivity
import sgtmelon.scriptum.screen.ui.callback.IAppActivity
import sgtmelon.scriptum.screen.ui.callback.IPreferenceFragment
import sgtmelon.scriptum.screen.vm.AppViewModel
import sgtmelon.scriptum.screen.vm.PreferenceViewModel
import sgtmelon.scriptum.screen.vm.callback.IAppViewModel
import sgtmelon.scriptum.screen.vm.callback.IPreferenceViewModel

/**
 * Factory for create viewModel's.
 */
object ViewModelFactory {

    fun get(activity: AppActivity): IAppViewModel {
        return ViewModelProvider(activity).get(AppViewModel::class.java).apply {
            setCallback(activity as? IAppActivity)
        }
    }

    fun get(context: Context, callback: IPreferenceFragment): IPreferenceViewModel {
        return PreferenceViewModel(context, callback)
    }

}