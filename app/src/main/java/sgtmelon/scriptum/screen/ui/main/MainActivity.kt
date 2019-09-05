package sgtmelon.scriptum.screen.ui.main

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.extension.hideKeyboard
import sgtmelon.scriptum.factory.DialogFactory
import sgtmelon.scriptum.factory.FragmentFactory
import sgtmelon.scriptum.factory.VmFactory
import sgtmelon.scriptum.model.data.ReceiverData
import sgtmelon.scriptum.model.key.MainPage
import sgtmelon.scriptum.model.state.OpenState
import sgtmelon.scriptum.receiver.MainReceiver
import sgtmelon.scriptum.screen.ui.AppActivity
import sgtmelon.scriptum.screen.ui.callback.main.IMainActivity

/**
 * Screen which displays main menu and fragments: [RankFragment], [NotesFragment], [BinFragment]
 *
 * @author SerjantArbuz
 */
class MainActivity : AppActivity(), IMainActivity {

    private val iViewModel by lazy { VmFactory.getMainViewModel(activity = this) }

    private val mainReceiver by lazy { MainReceiver(iViewModel) }

    private val rankFragment by lazy { FragmentFactory.getRankFragment(supportFragmentManager) }
    private val notesFragment by lazy { FragmentFactory.getNotesFragment(supportFragmentManager) }
    private val binFragment by lazy { FragmentFactory.getBinFragment(supportFragmentManager) }

    private val openState = OpenState()
    private val addDialog by lazy { DialogFactory.Main.getAddDialog(supportFragmentManager) }

    private val fab by lazy { findViewById<FloatingActionButton?>(R.id.main_add_fab) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        openState.get(savedInstanceState)

        iViewModel.onSetup(savedInstanceState)

        registerReceiver(mainReceiver, IntentFilter(ReceiverData.Filter.MAIN))
    }

    override fun onDestroy() {
        super.onDestroy()

        iViewModel.onDestroy()
        unregisterReceiver(mainReceiver)
    }

    override fun onSaveInstanceState(outState: Bundle) =
            super.onSaveInstanceState(outState.apply {
                openState.save(bundle = this)
                iViewModel.onSaveData(bundle = this)
            })

    /**
     * Если нажатие произошло за пределами контейнера [RankFragment.enterCard], то нужно
     * скрыть клавиатуру
     */
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action != MotionEvent.ACTION_DOWN) return super.dispatchTouchEvent(ev)

        rankFragment.enterCard?.let { if (!ev.onView(it)) hideKeyboard() }

        return super.dispatchTouchEvent(ev)
    }

    override fun setupNavigation(@IdRes itemId: Int) {
        fab?.setOnClickListener {
            openState.tryInvoke { addDialog.show(supportFragmentManager, DialogFactory.Main.ADD) }
        }

        findViewById<BottomNavigationView>(R.id.main_menu_navigation).apply {
            setOnNavigationItemSelectedListener { iViewModel.onSelectItem(it.itemId) }
            selectedItemId = itemId
        }

        addDialog.itemSelectedListener = NavigationView.OnNavigationItemSelectedListener {
            addDialog.dismiss()
            iViewModel.onResultAddDialog(it)
            return@OnNavigationItemSelectedListener true
        }
        addDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }
    }

    override fun changeFabState(state: Boolean) {
        fab?.setState(state)
    }

    override fun scrollTop(mainPage: MainPage) = when (mainPage) {
        MainPage.RANK -> rankFragment.scrollTop()
        MainPage.NOTES -> notesFragment.scrollTop()
        MainPage.BIN -> binFragment.scrollTop()
    }

    override fun showPage(pageFrom: MainPage, pageTo: MainPage) {
        with(supportFragmentManager) {
            beginTransaction().apply {
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)

                if (findFragmentByTag(pageFrom.name) != null) {
                    hide(pageFrom.getFragmentByName())
                }

                val fragmentTo = pageTo.getFragmentByName()
                if (findFragmentByTag(pageTo.name) != null) {
                    show(fragmentTo)
                    fragmentTo.onResume()
                } else {
                    add(R.id.main_fragment_container, fragmentTo, pageTo.name)
                }

                commit()
            }
        }
    }

    private fun MainPage.getFragmentByName(): Fragment = when (this) {
        MainPage.RANK -> rankFragment
        MainPage.NOTES -> notesFragment
        MainPage.BIN -> binFragment
    }

    override fun onCancelNoteBind(id: Long) = notesFragment.onCancelNoteBind(id)

    companion object {
        fun getInstance(context: Context) = Intent(context, MainActivity::class.java)

        private fun FloatingActionButton.setState(state: Boolean) {
            if (state) show() else hide()
            isEnabled = state
        }

        /**
         * Функция определяет произошёл ли [MotionEvent] на объекте
         */
        private fun MotionEvent?.onView(view: View?) = if (view != null && this != null) {
            Rect().apply {
                view.getGlobalVisibleRect(this)
            }.contains(rawX.toInt(), rawY.toInt())
        } else {
            false
        }
    }

}