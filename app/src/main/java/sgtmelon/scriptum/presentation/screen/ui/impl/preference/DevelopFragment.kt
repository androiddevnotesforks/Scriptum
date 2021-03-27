package sgtmelon.scriptum.presentation.screen.ui.impl.preference

import android.os.Bundle
import androidx.preference.Preference
import sgtmelon.scriptum.R
import sgtmelon.scriptum.extension.showToast
import sgtmelon.scriptum.presentation.screen.ui.ParentPreferenceFragment
import sgtmelon.scriptum.presentation.screen.ui.ScriptumApplication
import sgtmelon.scriptum.presentation.screen.ui.callback.preference.IDevelopFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.IDevelopViewModel
import javax.inject.Inject

/**
 * Fragment of develop preferences.
 */
class DevelopFragment : ParentPreferenceFragment(), IDevelopFragment {

    @Inject internal lateinit var viewModel: IDevelopViewModel

    //region Preferences

    private val printRankPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_print_rank)) }
    private val printNotePreference by lazy { findPreference<Preference>(getString(R.string.pref_key_print_note)) }
    private val printRollPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_print_roll)) }
    private val printAlarmPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_print_alarm)) }
    private val printVisiblePreference by lazy { findPreference<Preference>(getString(R.string.pref_key_print_visible)) }
    private val printPrefPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_print_pref)) }

    private val introPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_screen_intro)) }
    private val alarmPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_screen_alarm)) }

    private val resetPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_other_reset)) }

    //endregion

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_develop, rootKey)

        ScriptumApplication.component.getDevelopBuilder().set(fragment = this).build()
            .inject(fragment = this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.onSetup()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    override fun setupPrints() {
        printRankPreference?.setOnPreferenceClickListener {
            context?.showToast("click")
            return@setOnPreferenceClickListener true
        }
        printNotePreference?.setOnPreferenceClickListener {
            context?.showToast("click")
            return@setOnPreferenceClickListener true
        }
        printRollPreference?.setOnPreferenceClickListener {
            context?.showToast("click")
            return@setOnPreferenceClickListener true
        }
        printAlarmPreference?.setOnPreferenceClickListener {
            context?.showToast("click")
            return@setOnPreferenceClickListener true
        }
        printVisiblePreference?.setOnPreferenceClickListener {
            context?.showToast("click")
            return@setOnPreferenceClickListener true
        }
        printPrefPreference?.setOnPreferenceClickListener {
            context?.showToast("click")
            return@setOnPreferenceClickListener true
        }
    }

    override fun setupScreens() {
        introPreference?.setOnPreferenceClickListener {
            context?.showToast("click")
            return@setOnPreferenceClickListener true
        }
        alarmPreference?.setOnPreferenceClickListener {
            context?.showToast("click")
            return@setOnPreferenceClickListener true
        }
    }

    override fun setupOther() {
        resetPreference?.setOnPreferenceClickListener {
            context?.showToast("click")
            return@setOnPreferenceClickListener true
        }
    }
}