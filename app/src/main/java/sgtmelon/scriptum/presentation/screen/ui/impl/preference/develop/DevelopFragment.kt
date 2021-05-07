package sgtmelon.scriptum.presentation.screen.ui.impl.preference.develop

import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.preference.Preference
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.model.data.ReceiverData
import sgtmelon.scriptum.domain.model.key.DotAnimType
import sgtmelon.scriptum.domain.model.key.PrintType
import sgtmelon.scriptum.extension.showToast
import sgtmelon.scriptum.presentation.control.anim.DotAnimControl
import sgtmelon.scriptum.presentation.control.broadcast.BroadcastControl
import sgtmelon.scriptum.presentation.receiver.screen.DevelopScreenReceiver
import sgtmelon.scriptum.presentation.screen.ui.ParentPreferenceFragment
import sgtmelon.scriptum.presentation.screen.ui.ScriptumApplication
import sgtmelon.scriptum.presentation.screen.ui.callback.preference.develop.IDevelopFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.SplashActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.intro.IntroActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.develop.IDevelopViewModel
import sgtmelon.scriptum.presentation.service.EternalService
import javax.inject.Inject

/**
 * Fragment of develop preferences.
 */
class DevelopFragment : ParentPreferenceFragment(),
    IDevelopFragment,
    DotAnimControl.Callback {

    @Inject internal lateinit var viewModel: IDevelopViewModel

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

    private val refreshPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_service_refresh)) }
    private val runPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_service_run)) }
    private val killPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_service_kill)) }

    private val resetPreference by lazy { findPreference<Preference>(getString(R.string.pref_key_other_reset)) }

    //endregion

    private val broadcastControl by lazy { BroadcastControl[context] }
    private val dotAnimControl = DotAnimControl(DotAnimType.COUNT, callback = this)

    private val receiver by lazy { DevelopScreenReceiver[viewModel] }

    //region System

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_develop, rootKey)

        ScriptumApplication.component.getDevelopBuilder().set(fragment = this).build()
            .inject(fragment = this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.onSetup()
        context?.registerReceiver(receiver, IntentFilter(ReceiverData.Filter.DEVELOP))
    }

    override fun onDestroy() {
        super.onDestroy()

        viewModel.onDestroy()
        context?.unregisterReceiver(receiver)
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
                startActivity(IntroActivity[context])
            }

            return@setOnPreferenceClickListener true
        }
        alarmPreference?.setOnPreferenceClickListener {
            viewModel.onClickAlarm()
            return@setOnPreferenceClickListener true
        }
    }

    override fun setupEternal() {
        refreshPreference?.setOnPreferenceClickListener {
            viewModel.onClickEternalRefresh()
            return@setOnPreferenceClickListener true
        }
        runPreference?.setOnPreferenceClickListener {
            viewModel.onClickEternalRun()
            return@setOnPreferenceClickListener true
        }
        killPreference?.setOnPreferenceClickListener {
            viewModel.onClickEternalKill()
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


    override fun openPrintScreen(type: PrintType) {
        val context = context ?: return

        startActivity(PrintActivity[context, type])
    }

    override fun openAlarmScreen(noteId: Long) {
        val context = context ?: return

        startActivity(SplashActivity.getAlarmInstance(context, noteId))
    }

    //region Eternal functions

    override fun updateEternalRefreshEnabled(isEnabled: Boolean) {
        refreshPreference?.isEnabled = isEnabled
    }

    override fun resetEternalRefreshSummary() {
        refreshPreference?.summary = getString(R.string.pref_summary_eternal_refresh)
    }

    override fun updateEternalRunEnabled(isEnabled: Boolean) {
        runPreference?.isEnabled = isEnabled
    }

    override fun updateEternalKillEnabled(isEnabled: Boolean) {
        killPreference?.isEnabled = isEnabled
    }

    override fun startEternalPingSummary() {
        val context = context ?: return

        dotAnimControl.start(context, R.string.pref_summary_eternal_search)
    }

    override fun stopEternalPingSummary() = dotAnimControl.stop()

    override fun onDotAnimUpdate(text: CharSequence) {
        refreshPreference?.summary = text
    }

    override fun sendEternalPingBroadcast() = broadcastControl.sendEternalPing()

    override fun sendEternalKillBroadcast() = broadcastControl.sendEternalKill()

    override fun runEternalService() {
        val context = context?.applicationContext ?: return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            EternalService.start(context)
        }
    }

    //endregion

}