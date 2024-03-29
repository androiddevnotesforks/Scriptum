package sgtmelon.scriptum.infrastructure.screen.main

import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import sgtmelon.safedialog.dialog.MessageDialog
import sgtmelon.safedialog.utils.DialogStorage
import javax.inject.Inject
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.infrastructure.dialogs.sheet.AddSheetDialog
import sgtmelon.scriptum.databinding.ActivityMainBinding
import sgtmelon.scriptum.infrastructure.converter.MainPageConverter
import sgtmelon.scriptum.infrastructure.converter.dialog.AddSheetData
import sgtmelon.scriptum.infrastructure.factory.DialogFactory
import sgtmelon.scriptum.infrastructure.factory.FragmentFactory
import sgtmelon.scriptum.infrastructure.model.key.MainPage
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.screen.Screens
import sgtmelon.scriptum.infrastructure.screen.main.callback.ScrollTopCallback
import sgtmelon.scriptum.infrastructure.screen.main.rank.RankFragment
import sgtmelon.scriptum.infrastructure.screen.theme.ThemeActivity
import sgtmelon.scriptum.infrastructure.utils.ShowPlaceholder
import sgtmelon.scriptum.infrastructure.utils.extensions.hideKeyboard
import sgtmelon.scriptum.infrastructure.utils.extensions.insets.InsetsDir
import sgtmelon.scriptum.infrastructure.utils.extensions.insets.addSystemInsetsMargin
import sgtmelon.scriptum.infrastructure.utils.extensions.insets.doOnApplyWindowInsets
import sgtmelon.scriptum.infrastructure.utils.extensions.insets.updateMargin
import sgtmelon.scriptum.infrastructure.utils.extensions.onView
import sgtmelon.scriptum.infrastructure.utils.extensions.startSettingsActivity
import sgtmelon.scriptum.infrastructure.utils.extensions.startSettingsChannelActivity
import sgtmelon.scriptum.infrastructure.widgets.GradientFab
import sgtmelon.scriptum.infrastructure.widgets.recycler.RecyclerMainFabListener

/**
 * Screen which displays main menu.
 */
class MainActivity : ThemeActivity<ActivityMainBinding>(),
    RecyclerMainFabListener.Callback {

    // TODO двинуть в другой модуль EdgeDragHelper, RecyclerviewOverscroll!
    // TODO выносить из cleanup ui тесты

    override val layoutId: Int = R.layout.activity_main

    @Inject lateinit var viewModel: MainViewModel

    private val fragments = FragmentFactory.Main(fm)
    private val rankFragment by lazy { fragments.getRank() }
    private val notesFragment by lazy { fragments.getNotes() }
    private val binFragment by lazy { fragments.getBin() }

    private val dialogs by lazy { DialogFactory.Main(resources) }
    private val notificationsHelpDialog = DialogStorage(
        DialogFactory.Main.NOTIFICATIONS, owner = this,
        create = { dialogs.getNotificationsHelp() },
        setup = { setupNotificationsHelpDialog(it) }
    )
    private val addDialog = DialogStorage(
        DialogFactory.Main.ADD, owner = this,
        create = { dialogs.getAdd() },
        setup = { setupAddDialog(it) }
    )

    private val showHolder = ShowPlaceholder(lifecycle, context = this)

    private var gradientFab: GradientFab? = null

    private val pageConverter = MainPageConverter()

    //region System

    override fun inject(component: ScriptumComponent) {
        component.getMainBuilder()
            .set(owner = this)
            .build()
            .inject(activity = this)
    }

    override fun setupInsets() {
        super.setupInsets()

        binding?.parentContainer?.doOnApplyWindowInsets { view, insets, _, _, margin ->
            view.updateMargin(InsetsDir.LEFT, insets, margin)
            view.updateMargin(InsetsDir.TOP, insets, margin)
            view.updateMargin(InsetsDir.RIGHT, insets, margin)

            /**
             * Need use this function (not [View.updateMargin]), because otherwise snackbar
             * inside [RankFragment] will handle this inset and this cause strange bottom padding.
             */
            view.addSystemInsetsMargin(InsetsDir.BOTTOM, binding?.menuNavigation)

            return@doOnApplyWindowInsets insets
        }
    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.currentPage.observe(this) {
            showPage(viewModel.previousPage, it)
            changeFabVisibility()
        }
    }

    override fun setupView() {
        super.setupView()

        gradientFab = GradientFab(activity = this) { showAddDialog() }

        /** Setup of selected item must be before setting navigation item selected listener */
        binding?.menuNavigation?.selectedItemId = pageConverter.convert(viewModel.currentPage.value)

        /** Block [open] for fade time, this is needed to prevent fast switching between pages. */
        val changePageTime = resources.getInteger(R.integer.fragment_fade_time).toLong()
        binding?.menuNavigation?.setOnItemSelectedListener {
            val page = pageConverter.convert(it) ?: return@setOnItemSelectedListener false

            var result = false
            open.attempt {
                open.block(changePageTime)
                viewModel.changePage(page)
                result = true
            }

            return@setOnItemSelectedListener result
        }
        binding?.menuNavigation?.setOnItemReselectedListener {
            val page = viewModel.currentPage.value ?: return@setOnItemReselectedListener
            scrollTop(page)
        }
    }

    override fun setupDialogs() {
        super.setupDialogs()
        notificationsHelpDialog.restore()
        addDialog.restore()
    }

    private fun setupNotificationsHelpDialog(dialog: MessageDialog): Unit = with(dialog) {
        themeId = R.style.App_Dialog_Alert_NotificationsHelp
        onPositiveClick { startSettingsActivity(system?.toast) }
        onNegativeClick {
            startSettingsChannelActivity(system?.toast, R.string.notification_eternal_channel_id)
        }
        onNeutralClick { viewModel.hideNotificationsHelp() }
        onDismiss { notificationsHelpDialog.release() }
    }

    private fun setupAddDialog(dialog: AddSheetDialog): Unit = with(dialog) {
        onItemSelected(owner = this) {
            val type = AddSheetData().convert(it.itemId) ?: return@onItemSelected
            openNoteScreen(type)
        }
        onDismiss {
            addDialog.release()
            open.clear()
        }
    }

    override fun onResume() {
        super.onResume()

        /** Need clear [open], because may be case when open new screens. */
        open.isChangeEnabled = true
        open.clear()

        /** Show FAB (if it is possible) after returning to the screen. */
        changeFabVisibility()

        if (viewModel.showNotificationsHelp) {
            notificationsHelpDialog.show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        gradientFab = null
    }

    //endregion

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action != MotionEvent.ACTION_DOWN) return super.dispatchTouchEvent(ev)

        /** If touch action was outside of enter field, when hide keyboard. */
        if (viewModel.currentPage.value == MainPage.RANK) {
            if (!ev.onView(rankFragment.enterCard)) {
                hideKeyboard()
            }
        }

        return super.dispatchTouchEvent(ev)
    }

    private fun showPage(pageFrom: MainPage?, pageTo: MainPage) {
        lifecycleScope.launchWhenResumed {
            showHolder.start(binding?.toolbarHolder)

            fm.beginTransaction()
                .setCustomAnimations(R.anim.fragment_fade_in, R.anim.fragment_fade_out)
                .hidePreviousFragment(pageFrom)
                .showNextFragment(pageTo)
                .commit()
        }
    }

    private fun scrollTop(mainPage: MainPage) = ifFragmentAdded(mainPage) {
        (it as? ScrollTopCallback)?.scrollTop()
    }

    private fun showAddDialog() = open.attempt { addDialog.show() }

    private fun openNoteScreen(type: NoteType) = open.attempt {
        startActivity(Screens.Note.toNew(context = this, viewModel.getNewNote(type)))
    }

    //region Fragment transaction staff

    private fun FragmentTransaction.hidePreviousFragment(page: MainPage?) = apply {
        if (page == null || page.findFragment() == null) return@apply

        val fragment = page.getFragment()

        hide(fragment)
        fragment.onPause()

        /**
         * Dismiss without callback happen inside [Fragment.onStop] function.
         * So be careful when call it manually.
         */
        if (fragment is RankFragment) {
            fragment.snackbar.hide()
        }
    }

    private fun FragmentTransaction.showNextFragment(page: MainPage) = apply {
        val fragment = page.getFragment()

        if (page.findFragment() != null) {
            show(fragment)
            fragment.onResume()
        } else {
            add(R.id.fragment_container, fragment, page.getFragmentTag())
        }
    }

    private fun MainPage.findFragment(): Fragment? = fm.findFragmentByTag(getFragmentTag())

    private fun MainPage.getFragmentTag(): String {
        return when (this) {
            MainPage.RANK -> FragmentFactory.Main.Tag.RANK
            MainPage.NOTES -> FragmentFactory.Main.Tag.NOTES
            MainPage.BIN -> FragmentFactory.Main.Tag.BIN
        }
    }

    private fun MainPage.getFragment(): Fragment {
        return when (this) {
            MainPage.RANK -> rankFragment
            MainPage.NOTES -> notesFragment
            MainPage.BIN -> binFragment
        }
    }

    /**
     * Call [func] only if fragment related with this [page] is already added.
     */
    private inline fun ifFragmentAdded(page: MainPage, func: (fragment: Fragment) -> Unit) {
        val fragment = page.findFragment() ?: return
        func(fragment)
    }

    //endregion

    override fun changeFabVisibility(isVisible: Boolean, withGap: Boolean) {
        gradientFab?.changeVisibility(isVisible = isVisible && viewModel.isFabPage, withGap)
    }
}