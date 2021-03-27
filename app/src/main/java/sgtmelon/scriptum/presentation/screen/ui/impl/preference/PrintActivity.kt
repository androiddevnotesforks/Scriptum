package sgtmelon.scriptum.presentation.screen.ui.impl.preference

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.model.data.IntentData
import sgtmelon.scriptum.domain.model.key.PrintType
import sgtmelon.scriptum.extension.getTintDrawable
import sgtmelon.scriptum.presentation.screen.ui.ScriptumApplication
import sgtmelon.scriptum.presentation.screen.ui.callback.preference.IPrintActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.AppActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.IPrintViewModel
import javax.inject.Inject

/**
 * Screen for print data of data base and preference
 */
class PrintActivity : AppActivity(), IPrintActivity {

    @Inject internal lateinit var viewModel: IPrintViewModel

    // TODO insets

    private val parentContainer by lazy { findViewById<ViewGroup>(R.id.print_parent_container) }
    private val toolbar by lazy { findViewById<Toolbar>(R.id.toolbar_container) }
    private val text by lazy { findViewById<TextView>(R.id.print_text) }

    override fun onCreate(savedInstanceState: Bundle?) {
        ScriptumApplication.component.getPrintBuilder().set(activity = this).build()
            .inject(activity = this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_print)

        val typeOrdinal = intent.extras?.getInt(
            IntentData.Print.Intent.TYPE, IntentData.Print.Default.TYPE
        ) ?: IntentData.Note.Default.TYPE

        val type = PrintType.values().getOrNull(typeOrdinal)

        text.text = type?.name

        //        viewModel.onSetup(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    private fun setupView() {
        toolbar?.apply {
            // TODO different title of screen
            title = getString(R.string.title_print)
            navigationIcon = getTintDrawable(R.drawable.ic_cancel_exit)
            setNavigationOnClickListener { finish() }
        }
    }

    companion object {
        operator fun get(context: Context, type: PrintType): Intent {
            return Intent(context, PrintActivity::class.java)
                .putExtra(IntentData.Print.Intent.TYPE, type.ordinal)
        }
    }
}