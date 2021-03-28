package sgtmelon.scriptum.presentation.screen.ui.impl.preference

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.model.data.IntentData
import sgtmelon.scriptum.domain.model.key.PrintType
import sgtmelon.scriptum.extension.*
import sgtmelon.scriptum.presentation.adapter.NoteAdapter
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

    private val parentContainer by lazy { findViewById<ViewGroup>(R.id.print_parent_container) }
    private val toolbar by lazy { findViewById<Toolbar>(R.id.toolbar_container) }

    private val emptyInfoView by lazy { findViewById<View>(R.id.print_info_include) }
    private val progressBar by lazy { findViewById<View>(R.id.print_progress) }
    private val recyclerView by lazy { findViewById<RecyclerView>(R.id.print_recycler) }

    private val adapter: NoteAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        ScriptumApplication.component.getPrintBuilder().set(activity = this).build()
            .inject(activity = this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_print)

        viewModel.onSetup(bundle = savedInstanceState ?: intent.extras)
    }

    override fun onResume() {
        super.onResume()
        viewModel.onUpdateData()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.onSaveData(outState)
    }


    override fun setupView(type: PrintType) {
        val toolbar = toolbar ?: return
        val recyclerView = recyclerView ?: return

        val titleText = getString(when (type) {
            PrintType.NOTE, PrintType.BIN -> R.string.pref_title_prints_note
            PrintType.ROLL -> R.string.pref_title_prints_roll
            PrintType.VISIBLE -> R.string.pref_title_prints_visible
            PrintType.RANK -> R.string.pref_title_prints_rank
            PrintType.ALARM -> R.string.pref_title_prints_alarm
            PrintType.PREFERENCE -> R.string.pref_title_prints_pref
        }).toLowerCase()

        toolbar.title = getString(R.string.title_print, titleText)

        if (type == PrintType.NOTE) {
            toolbar.subtitle = getString(R.string.pref_summary_print_note)
        } else if (type == PrintType.BIN) {
            toolbar.subtitle = getString(R.string.pref_summary_print_bin)
        }

        toolbar.navigationIcon = getTintDrawable(R.drawable.ic_cancel_exit)
        toolbar.setNavigationOnClickListener { finish() }

        recyclerView.setHasFixedSize(true)
        // TODO
        //        recyclerView.adapter = adapter
    }

    override fun setupInsets() {
        parentContainer?.doOnApplyWindowInsets { view, insets, _, _, margin ->
            view.updateMargin(InsetsDir.LEFT, insets, margin)
            view.updateMargin(InsetsDir.TOP, insets, margin)
            view.updateMargin(InsetsDir.RIGHT, insets, margin)
            return@doOnApplyWindowInsets insets
        }

        recyclerView?.doOnApplyWindowInsets { view, insets, _, padding, _ ->
            view.updatePadding(InsetsDir.BOTTOM, insets, padding)
            return@doOnApplyWindowInsets insets
        }
    }

    /**
     * For first time [recyclerView] visibility flag set inside xml file.
     */
    override fun beforeLoad() {
        emptyInfoView?.visibility = View.GONE
        progressBar?.visibility = View.GONE
    }

    override fun showProgress() {
        progressBar?.visibility = View.VISIBLE
    }

    override fun onBindingList() {
        progressBar?.visibility = View.GONE

        if (adapter?.itemCount == 0) {
            emptyInfoView?.visibility = View.VISIBLE
            emptyInfoView?.alpha = 0f
            emptyInfoView?.animateAlpha(isVisible = true)
        } else {
            recyclerView?.visibility = View.VISIBLE
        }
    }

    override fun notifyList(list: List<Any>) {
        TODO("Not yet implemented")
    }

    companion object {
        operator fun get(context: Context, type: PrintType): Intent {
            return Intent(context, PrintActivity::class.java)
                .putExtra(IntentData.Print.Intent.TYPE, type.ordinal)
        }
    }
}