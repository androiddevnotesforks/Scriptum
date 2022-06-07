package sgtmelon.scriptum.presentation.screen.ui.impl.preference.develop

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.databinding.ActivityDevelopPrintBinding
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.data.IntentData
import sgtmelon.scriptum.domain.model.item.PrintItem
import sgtmelon.scriptum.domain.model.key.PrintType
import sgtmelon.scriptum.extension.*
import sgtmelon.scriptum.presentation.adapter.PrintAdapter
import sgtmelon.scriptum.presentation.screen.ui.ScriptumApplication
import sgtmelon.scriptum.presentation.screen.ui.callback.preference.develop.IPrintDevelopActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.AppActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.develop.IPrintDevelopViewModel
import javax.inject.Inject

/**
 * Screen for print data of data base and preference.
 */
class PrintDevelopActivity : AppActivity(), IPrintDevelopActivity {

    private var binding: ActivityDevelopPrintBinding? = null

    @Inject internal lateinit var viewModel: IPrintDevelopViewModel

    private val parentContainer by lazy { findViewById<ViewGroup>(R.id.print_parent_container) }
    private val toolbar by lazy { findViewById<Toolbar>(R.id.toolbar_container) }

    private val emptyInfoView by lazy { findViewById<View>(R.id.print_info_include) }
    private val progressBar by lazy { findViewById<View>(R.id.print_progress) }
    private val recyclerView by lazy { findViewById<RecyclerView>(R.id.print_recycler) }

    private val adapter = PrintAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        ScriptumApplication.component.getPrintBuilder().set(activity = this).build()
            .inject(activity = this)

        super.onCreate(savedInstanceState)
        binding = inflateBinding(R.layout.activity_develop_print)

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

    /**
     * Make navigation translucent in portrait orientation.
     */
    override fun setNavigationColor(@Theme theme: Int) {
        if (isPortraitMode()) {
            window.navigationBarColor = getColorAttr(R.attr.clNavigationBar)
        } else {
            super.setNavigationColor(theme)
        }
    }

    /**
     * Make navigation translucent in portrait orientation.
     */
    @RequiresApi(Build.VERSION_CODES.P)
    override fun setNavigationDividerColor(@Theme theme: Int) {
        if (isPortraitMode()) {
            window.navigationBarDividerColor = getColorAttr(R.attr.clNavigationBarDivider)
        } else {
            super.setNavigationDividerColor(theme)
        }
    }


    override fun setupView(type: PrintType) {
        val toolbar = toolbar ?: return
        val recyclerView = recyclerView ?: return

        val titleText = getString(when (type) {
            PrintType.NOTE, PrintType.BIN -> R.string.pref_title_print_note
            PrintType.ROLL -> R.string.pref_title_print_roll
            PrintType.VISIBLE -> R.string.pref_title_print_visible
            PrintType.RANK -> R.string.pref_title_print_rank
            PrintType.ALARM -> R.string.pref_title_print_alarm
            PrintType.KEY -> R.string.pref_title_print_key
            PrintType.FILE -> R.string.pref_title_print_file
        }).lowercase()

        toolbar.title = getString(R.string.title_print, titleText)

        if (type == PrintType.NOTE) {
            toolbar.subtitle = getString(R.string.pref_summary_print_note)
        } else if (type == PrintType.BIN) {
            toolbar.subtitle = getString(R.string.pref_summary_print_bin)
        }

        toolbar.navigationIcon = getTintDrawable(R.drawable.ic_cancel_exit)
        toolbar.setNavigationOnClickListener { finish() }

        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
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

        if (adapter.itemCount == 0) {
            emptyInfoView?.visibility = View.VISIBLE
            recyclerView?.visibility = View.INVISIBLE

            emptyInfoView?.alpha = 0f
            emptyInfoView?.animateAlpha(isVisible = true)
        } else {
            recyclerView?.visibility = View.VISIBLE

            emptyInfoView?.animateAlpha(isVisible = false) {
                emptyInfoView?.visibility = View.GONE
            }
        }
    }

    override fun notifyList(list: List<PrintItem>) {
        adapter.setList(list).notifyDataSetChanged()
    }

    companion object {
        operator fun get(context: Context, type: PrintType): Intent {
            return Intent(context, PrintDevelopActivity::class.java)
                .putExtra(IntentData.Print.Intent.TYPE, type.ordinal)
        }
    }
}