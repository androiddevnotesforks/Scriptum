package sgtmelon.scriptum.screen.view.main

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.control.notification.broadcast.UnbindReceiver
import sgtmelon.scriptum.factory.DialogFactory
import sgtmelon.scriptum.factory.FragmentFactory
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.key.MainPage
import sgtmelon.scriptum.model.state.OpenState
import sgtmelon.scriptum.office.annot.def.DialogDef
import sgtmelon.scriptum.screen.callback.main.MainCallback
import sgtmelon.scriptum.screen.view.AppActivity
import sgtmelon.scriptum.screen.view.note.NoteActivity.Companion.getNoteIntent
import sgtmelon.scriptum.screen.vm.main.MainViewModel

/**
 * Экран отвечающий за отображение главного меню и фрагментов
 * [RankFragment], [NotesFragment], [BinFragment]
 *
 * @author SerjantArbuz
 */
class MainActivity : AppActivity(), MainCallback {

    // TODO Добавить перескакивание курсора при старте редактирования в нужное место
    // TODO setHasFixedSize recyclerView

    // TODO если очистить базу данных то закреплённые уведомления останутся

    // TODO при создании заметки фокус на поле вводе (разве не должен он быть на названнии?)

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java).apply {
            callback = this@MainActivity
        }
    }

    private val unbindReceiver by lazy { UnbindReceiver(viewModel) }

    private val rankFragment by lazy { FragmentFactory.getRankFragment(supportFragmentManager) }
    private val notesFragment by lazy { FragmentFactory.getNotesFragment(supportFragmentManager) }
    private val binFragment by lazy { FragmentFactory.getBinFragment(supportFragmentManager) }

    private val openState = OpenState()
    private val sheetDialog by lazy { DialogFactory.getSheetDialog(supportFragmentManager) }

    private val fab by lazy { findViewById<FloatingActionButton>(R.id.main_add_fab) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            openState.value = savedInstanceState.getBoolean(OpenState.KEY)
        }

        viewModel.onSetupData(savedInstanceState)

        registerReceiver(unbindReceiver, IntentFilter())
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(unbindReceiver)
    }

    override fun onSaveInstanceState(outState: Bundle) =
            super.onSaveInstanceState(outState.apply {
                putBoolean(OpenState.KEY, openState.value)
                viewModel.onSaveData(bundle = this)
            })

    override fun setupNavigation(@IdRes itemId: Int) {
        fab.setOnClickListener {
            openState.tryInvoke { sheetDialog.show(supportFragmentManager, DialogDef.SHEET) }
        }

        findViewById<BottomNavigationView>(R.id.main_menu_navigation).apply {
            setOnNavigationItemSelectedListener { viewModel.onSelectItem(it.itemId) }
            selectedItemId = itemId
        }

        sheetDialog.itemSelectedListener = NavigationView.OnNavigationItemSelectedListener {
            sheetDialog.dismiss()

            startActivity(getNoteIntent(NoteData.getTypeById(it.itemId)))
            return@OnNavigationItemSelectedListener true
        }
        sheetDialog.dismissListener = DialogInterface.OnDismissListener { openState.clear() }
    }

    override fun changeFabState(state: Boolean) = fab.setState(state)

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

    override fun resumeNotesPage() = notesFragment.onResume()

    companion object {
        fun getIntent(context: Context) = Intent(context, MainActivity::class.java)

        private fun FloatingActionButton.setState(state: Boolean) {
            if (state) show() else hide()
            isEnabled = state
        }
    }

}