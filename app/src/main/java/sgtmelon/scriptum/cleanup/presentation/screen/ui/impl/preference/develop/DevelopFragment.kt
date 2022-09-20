package sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.develop

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.preference.Preference
import javax.inject.Inject
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.key.PreferenceScreen
import sgtmelon.scriptum.cleanup.domain.model.key.PrintType
import sgtmelon.scriptum.cleanup.presentation.screen.ui.ParentPreferenceFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.ScriptumApplication
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.preference.develop.IDevelopFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.PreferenceActivity
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.develop.IDevelopViewModel
import sgtmelon.scriptum.infrastructure.factory.InstanceFactory

/**
 * Fragment of develop preferences.
 */
class DevelopFragment : ParentPreferenceFragment(),
    IDevelopFragment {

    @Inject lateinit var viewModel: IDevelopViewModel

    //region Preferences

    private val printNotePreference by lazy { findPreference<Preference>(getString(R.string.pref_key_print_note)) }
    private val printBinPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_print_bin)) }
    private val printRollPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_print_roll)) }
    private val printVisiblePreference by lazy { findPreference<Preference>(getString(R.string.pref_key_print_visible)) }
    private val printRankPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_print_rank)) }
    private val printAlarmPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_print_alarm)) }

    private val printKeyPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_print_key)) }
    private val printFilePreference by lazy { findPreference<Preference>(getString(R.string.pref_key_print_file)) }

    private val introPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_screen_intro)) }
    private val alarmPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_screen_alarm)) }

    private val eternalPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_service_eternal)) }

    private val resetPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_other_reset)) }

    //endregion

    //region System

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_develop, rootKey)

        ScriptumApplication.component.getDevelopBuilder().set(fragment = this).build()
            .inject(fragment = this)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.onSetup()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    //endregion

    override fun setupPrints() {
        printNotePreference?.setOnPreferenceClickListener {
            viewModel.onClickPrint(PrintType.NOTE)
            return@setOnPreferenceClickListener true
        }
        printBinPreference?.setOnPreferenceClickListener {
            viewModel.onClickPrint(PrintType.BIN)
            return@setOnPreferenceClickListener true
        }
        printRollPreference?.setOnPreferenceClickListener {
            viewModel.onClickPrint(PrintType.ROLL)
            return@setOnPreferenceClickListener true
        }
        printVisiblePreference?.setOnPreferenceClickListener {
            viewModel.onClickPrint(PrintType.VISIBLE)
            return@setOnPreferenceClickListener true
        }
        printRankPreference?.setOnPreferenceClickListener {
            viewModel.onClickPrint(PrintType.RANK)
            return@setOnPreferenceClickListener true
        }
        printAlarmPreference?.setOnPreferenceClickListener {
            viewModel.onClickPrint(PrintType.ALARM)
            return@setOnPreferenceClickListener true
        }
        printKeyPreference?.setOnPreferenceClickListener {
            viewModel.onClickPrint(PrintType.KEY)
            return@setOnPreferenceClickListener true
        }
        printFilePreference?.setOnPreferenceClickListener {
            viewModel.onClickPrint(PrintType.FILE)
            return@setOnPreferenceClickListener true
        }
    }

    override fun setupScreens() {
        introPreference?.setOnPreferenceClickListener {
            val context = context
            if (context != null) {
                startActivity(InstanceFactory.Intro[context])
            }

            return@setOnPreferenceClickListener true
        }
        alarmPreference?.setOnPreferenceClickListener {
            viewModel.onClickAlarm()
            return@setOnPreferenceClickListener true
        }
    }

    override fun setupService() {
        eternalPreference?.setOnPreferenceClickListener {
            val context = context
            if (context != null) {
                startActivity(PreferenceActivity[context, PreferenceScreen.SERVICE])
            }

            return@setOnPreferenceClickListener true
        }
    }

    override fun setupOther() {
        resetPreference?.setOnPreferenceClickListener {
            viewModel.onClickReset()
            return@setOnPreferenceClickListener true
        }
    }

    override fun showToast(@StringRes stringId: Int) = toast.show(context, stringId)


    override fun openPrintScreen(type: PrintType) {
        val context = context ?: return

        startActivity(PrintDevelopActivity[context, type])
    }

    override fun openAlarmScreen(noteId: Long) {
        val context = context ?: return
        startActivity(InstanceFactory.Splash.getAlarm(context, noteId))
    }
}