package sgtmelon.scriptum.develop.infrastructure.screen.develop

import android.content.Context
import androidx.preference.Preference
import sgtmelon.extensions.collect
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.develop.infrastructure.model.PrintType
import sgtmelon.scriptum.develop.infrastructure.screen.print.PrintDevelopActivity
import sgtmelon.scriptum.infrastructure.screen.Screens
import sgtmelon.scriptum.infrastructure.screen.parent.PreferenceFragment
import sgtmelon.scriptum.infrastructure.screen.preference.PreferenceScreen
import sgtmelon.scriptum.infrastructure.utils.extensions.setOnClickListener
import javax.inject.Inject

/**
 * Fragment of develop preferences.
 */
class DevelopFragment : PreferenceFragment<DevelopBinding>() {

    override val xmlId: Int = R.xml.preference_develop

    override fun createBinding(): DevelopBinding = DevelopBinding(fragment = this)

    @Inject lateinit var viewModel: DevelopViewModel

    override fun inject(component: ScriptumComponent) {
        component.getPreferenceDevBuilder()
            .set(owner = this)
            .build()
            .inject(fragment = this)
    }

    override fun setupView() {
        binding?.apply {
            printNoteButton?.setOnPrintClickListener(PrintType.NOTE)
            printBinButton?.setOnPrintClickListener(PrintType.BIN)
            printRollButton?.setOnPrintClickListener(PrintType.ROLL)
            printVisibleButton?.setOnPrintClickListener(PrintType.VISIBLE)
            printRankButton?.setOnPrintClickListener(PrintType.RANK)
            printAlarmButton?.setOnPrintClickListener(PrintType.ALARM)
            printKeyButton?.setOnPrintClickListener(PrintType.KEY)
            printFileButton?.setOnPrintClickListener(PrintType.FILE)

            alarmButton?.setOnClickListener { openRandomAlarm(it.context) }

            eternalButton?.setOnClickListener {
                startActivity(Screens.toPreference(it.context, PreferenceScreen.SERVICE))
            }

            resetButton?.setOnClickListener {
                viewModel.resetPreferences()
                system?.toast?.show(it.context, R.string.toast_dev_clear)
            }
        }
    }

    private fun Preference.setOnPrintClickListener(type: PrintType) {
        setOnClickListener {
            startActivity(PrintDevelopActivity[it.context, type])
        }
    }

    private fun openRandomAlarm(context: Context) {
        viewModel.randomNoteId.collect(owner = this) { id ->
            if (id == null) {
                system?.toast?.show(context, text = "You don't have notes")
            } else {
                startActivity(Screens.Splash.toAlarm(context, id))
            }
        }
    }
}