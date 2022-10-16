package sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.develop

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.preference.Preference
import javax.inject.Inject
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.domain.model.key.PreferenceScreen
import sgtmelon.scriptum.cleanup.domain.model.key.PrintType
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.preference.develop.IDevelopFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.develop.DevelopViewModel
import sgtmelon.scriptum.infrastructure.develop.screen.DevelopDataBinding
import sgtmelon.scriptum.infrastructure.factory.InstanceFactory
import sgtmelon.scriptum.infrastructure.screen.parent.ParentPreferenceFragment
import sgtmelon.scriptum.infrastructure.utils.setOnClickListener

/**
 * Fragment of develop preferences.
 */
class DevelopFragment : ParentPreferenceFragment(),
    IDevelopFragment {

    override val xmlId: Int = R.xml.preference_develop

    private val binding = DevelopDataBinding(lifecycle, fragment = this)

    @Inject lateinit var viewModel: DevelopViewModel

    //region System

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
    }

    override fun inject(component: ScriptumComponent) {
        component.getDevelopBuilder()
            .set(fragment = this)
            .build()
            .inject(fragment = this)
    }

    //endregion

    private fun setup() = binding.apply {
        printNoteButton?.setOnPrintClickListener(PrintType.NOTE)
        printBinButton?.setOnPrintClickListener(PrintType.BIN)
        printRollButton?.setOnPrintClickListener(PrintType.ROLL)
        printVisibleButton?.setOnPrintClickListener(PrintType.VISIBLE)
        printRankButton?.setOnPrintClickListener(PrintType.RANK)
        printAlarmButton?.setOnPrintClickListener(PrintType.ALARM)
        printKeyButton?.setOnPrintClickListener(PrintType.KEY)
        printFileButton?.setOnPrintClickListener(PrintType.FILE)

        introButton?.setOnClickListener { startActivity(InstanceFactory.Intro[it.context]) }
        alarmButton?.setOnClickListener { viewModel.onClickAlarm() }

        eternalButton?.setOnClickListener {
            startActivity(InstanceFactory.Preference[it.context, PreferenceScreen.SERVICE])
        }

        resetButton?.setOnClickListener { viewModel.onClickReset() }
    }

    private fun Preference.setOnPrintClickListener(type: PrintType) {
        setOnClickListener {
            startActivity(InstanceFactory.Preference.Develop.Print[it.context, type])
        }
    }


    override fun showToast(@StringRes stringId: Int) = delegators.toast.show(context, stringId)

    override fun openAlarmScreen(noteId: Long) {
        val context = context ?: return
        startActivity(InstanceFactory.Splash.getAlarm(context, noteId))
    }
}