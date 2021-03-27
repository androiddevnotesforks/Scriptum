package sgtmelon.scriptum.presentation.screen.ui.impl.preference

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.preference.Preference
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.model.key.PrintType
import sgtmelon.scriptum.extension.showToast
import sgtmelon.scriptum.presentation.screen.ui.ParentPreferenceFragment
import sgtmelon.scriptum.presentation.screen.ui.ScriptumApplication
import sgtmelon.scriptum.presentation.screen.ui.callback.preference.IDevelopFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.SplashActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.intro.IntroActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.IDevelopViewModel
import javax.inject.Inject

/**
 * Fragment of develop preferences.
 */
class DevelopFragment : ParentPreferenceFragment(), IDevelopFragment {

    @Inject internal lateinit var viewModel: IDevelopViewModel

    //region Preferences

    private val printNotePreference by lazy { findPreference<Preference>(getString(R.string.pref_key_print_note)) }
    private val printBinPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_print_bin)) }
    private val printRollPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_print_roll)) }
    private val printVisiblePreference by lazy { findPreference<Preference>(getString(R.string.pref_key_print_visible)) }
    private val printRankPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_print_rank)) }
    private val printAlarmPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_print_alarm)) }
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
        val context = context ?: return

        printNotePreference?.setOnPreferenceClickListener {
            startActivity(PrintActivity[context, PrintType.NOTE])
            return@setOnPreferenceClickListener true
        }
        printBinPreference?.setOnPreferenceClickListener {
            startActivity(PrintActivity[context, PrintType.BIN])
            return@setOnPreferenceClickListener true
        }
        printRollPreference?.setOnPreferenceClickListener {
            startActivity(PrintActivity[context, PrintType.ROLL])
            return@setOnPreferenceClickListener true
        }
        printVisiblePreference?.setOnPreferenceClickListener {
            startActivity(PrintActivity[context, PrintType.VISIBLE])
            return@setOnPreferenceClickListener true
        }
        printRankPreference?.setOnPreferenceClickListener {
            startActivity(PrintActivity[context, PrintType.RANK])
            return@setOnPreferenceClickListener true
        }
        printAlarmPreference?.setOnPreferenceClickListener {
            startActivity(PrintActivity[context, PrintType.ALARM])
            return@setOnPreferenceClickListener true
        }
        printPrefPreference?.setOnPreferenceClickListener {
            startActivity(PrintActivity[context, PrintType.PREFERENCE])
            return@setOnPreferenceClickListener true
        }
    }

    override fun setupScreens() {
        introPreference?.setOnPreferenceClickListener {
            val context = context
            if (context != null) {
                startActivity(IntroActivity[context])
            }

            return@setOnPreferenceClickListener true
        }
        alarmPreference?.setOnPreferenceClickListener {
            viewModel.onClickScreenAlarm()
            return@setOnPreferenceClickListener true
        }
    }

    override fun setupOther() {
        resetPreference?.setOnPreferenceClickListener {
            viewModel.onClickReset()
            return@setOnPreferenceClickListener true
        }
    }

    override fun showToast(@StringRes stringId: Int) {
        context?.showToast(stringId)
    }

    // TODO

    override fun openAlarmScreen(noteId: Long) {
        val context = context ?: return

        startActivity(SplashActivity.getAlarmInstance(context, noteId))
    }
}