package sgtmelon.scriptum.infrastructure.screen.main

import android.content.IntentFilter
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import javax.inject.Inject
import sgtmelon.safedialog.utils.safeShow
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.presentation.control.toolbar.show.HolderShowControl
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.main.RankFragment
import sgtmelon.scriptum.databinding.ActivityMainBinding
import sgtmelon.scriptum.infrastructure.converter.MainPageConverter
import sgtmelon.scriptum.infrastructure.converter.dialog.AddSheetData
import sgtmelon.scriptum.infrastructure.factory.DialogFactory
import sgtmelon.scriptum.infrastructure.factory.FragmentFactory
import sgtmelon.scriptum.infrastructure.factory.InstanceFactory
import sgtmelon.scriptum.infrastructure.model.data.IdlingTag
import sgtmelon.scriptum.infrastructure.model.data.ReceiverData.Filter
import sgtmelon.scriptum.infrastructure.model.key.MainPage
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.receiver.screen.UnbindNoteReceiver
import sgtmelon.scriptum.infrastructure.screen.main.callback.FabShowCallback
import sgtmelon.scriptum.infrastructure.screen.main.callback.ScrollTopCallback
import sgtmelon.scriptum.infrastructure.screen.theme.ThemeActivity
import sgtmelon.scriptum.infrastructure.utils.InsetsDir
import sgtmelon.scriptum.infrastructure.utils.addSystemInsetsMargin
import sgtmelon.scriptum.infrastructure.utils.doOnApplyWindowInsets
import sgtmelon.scriptum.infrastructure.utils.hideKeyboard
import sgtmelon.scriptum.infrastructure.utils.onView
import sgtmelon.scriptum.infrastructure.utils.updateMargin
import sgtmelon.scriptum.infrastructure.widgets.delegators.GradientFabDelegator
import sgtmelon.test.idling.getIdling

/**
 * Screen which displays main menu.
 */
class MainActivity : ThemeActivity<ActivityMainBinding>(),
    UnbindNoteReceiver.Callback,
    FabShowCallback {

    override val layoutId: Int = R.layout.activity_main

    @Inject lateinit var viewModel: MainViewModel

    private val unbindNoteReceiver by lazy { UnbindNoteReceiver[this] }

    private val fragments = FragmentFactory.Main(fm)
    private val rankFragment by lazy { fragments.getRank() }
    private val notesFragment by lazy { fragments.getNotes() }
    private val binFragment by lazy { fragments.getBin() }

    private val addDialog by lazy { DialogFactory.Main(context = this, fm).getAdd() }

    private val holderControl by lazy { HolderShowControl[binding?.toolbarHolder] }
    private var fabDelegator: GradientFabDelegator? = null

    private val pageConverter = MainPageConverter()

    //region System

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fabDelegator = GradientFabDelegator(activity = this) { openAddDialog() }

        getIdling().stop(IdlingTag.Intro.FINISH)
    }

    override fun inject(component: ScriptumComponent) {
        component.getMainBuilder()
            .set(activity = this)
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
             * inside [RankFragment] will handle this inset (and this cause strange bottom padding)
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

        // TODO check how will work reselect
        // tODO check set current item
        // TODO блокировать "open" только если смена страницы (не при скроллинге)
        val changePageTime = resources.getInteger(R.integer.fragment_fade_time).toLong()
        binding?.menuNavigation?.setOnItemSelectedListener {
            val page = pageConverter.convert(it) ?: return@setOnItemSelectedListener false

            open.returnAttempt {
                open.block(changePageTime)
                viewModel.changePage(page)
                return@returnAttempt true
            } ?: false
        }
        binding?.menuNavigation?.setOnItemReselectedListener {
            val page = viewModel.currentPage.value ?: return@setOnItemReselectedListener
            scrollTop(page)
        }
        binding?.menuNavigation?.selectedItemId = pageConverter.convert(viewModel.currentPage.value)

        addDialog.onItemSelected(owner = this) {
            val type = AddSheetData().convert(it.itemId) ?: return@onItemSelected
            openNoteScreen(type)
        }
        addDialog.onDismiss { open.clear() }
    }

    override fun registerReceivers() {
        super.registerReceivers()
        registerReceiver(unbindNoteReceiver, IntentFilter(Filter.MAIN))
    }

    override fun unregisterReceivers() {
        super.unregisterReceivers()
        unregisterReceiver(unbindNoteReceiver)
    }

    override fun onResume() {
        super.onResume()

        /** Clear [open] after click on item container. */
        open.isChangeEnabled = true
        open.clear()

        /** Show FAB (if it is possible) after returning to the screen. */
        changeFabVisibility()
    }

    override fun onDestroy() {
        super.onDestroy()
        fabDelegator = null

        holderControl.onDestroy()
    }

    //endregion

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action != MotionEvent.ACTION_DOWN) return super.dispatchTouchEvent(ev)

        // TODO check work
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
            holderControl.display()

            fm.beginTransaction()
                .setCustomAnimations(R.anim.fragment_fade_in, R.anim.fragment_fade_out)
                .hidePreviousFragment(pageFrom)
                .showNextFragment(pageTo)
                .commit()
        }
    }

    // TODO check scroll
    private fun scrollTop(mainPage: MainPage) {
        ifFragmentAdded(mainPage) { (it as? ScrollTopCallback)?.scrollTop() }
    }

    //region Cleanup

    private fun openAddDialog() = open.attempt {
        addDialog.safeShow(DialogFactory.Main.ADD, owner = this)
    }

    private fun openNoteScreen(noteType: NoteType) = open.attempt {
        startActivity(InstanceFactory.Note[this, noteType.ordinal])
    }

    //
    //    /**
    //     * Change FAB state consider on current page.
    //     */
    //    override fun onFabStateChange(isVisible: Boolean, withGap: Boolean) {
    //        changeFabVisible(isVisible = isVisible && viewModel.isStartPage, withGap)
    //    }
    //
    //    private fun changeFabVisible(isVisible: Boolean, withGap: Boolean) {
    //        fabDelegator?.changeVisibility(isVisible = isVisible && viewModel.isFabPage, withGap)
    //    }

    override fun onReceiveUnbindNote(noteId: Long) {
        ifFragmentAdded(MainPage.RANK) { rankFragment.onReceiveUnbindNote(noteId) }
        ifFragmentAdded(MainPage.NOTES) { notesFragment.onReceiveUnbindNote(noteId) }
    }

    /**
     * Call [func] only if [page] fragment added.
     */
    private inline fun ifFragmentAdded(page: MainPage, func: (fragment: Fragment) -> Unit) {
        val fragment = page.findFragment() ?: return
        func(fragment)
    }

    //endregion

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
            fragment.dismissSnackbar()
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


    override fun changeFabVisibility(isVisible: Boolean, withGap: Boolean) {
        fabDelegator?.changeVisibility(isVisible = isVisible && viewModel.isFabPage, withGap)
    }
}