package sgtmelon.scriptum.presentation.screen.ui.impl

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import sgtmelon.scriptum.R
import sgtmelon.scriptum.presentation.screen.ui.ScriptumApplication
import sgtmelon.scriptum.presentation.screen.ui.callback.IDevelopActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.intro.IntroActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.IDevelopViewModel
import javax.inject.Inject

/**
 * Screen which displays information for developer
 */
class DevelopActivity : AppCompatActivity(), IDevelopActivity {

    // TODO #RELEASE2 Add different windows for every table
    // TODO #RELEASE2 for example: note+roll, note+alarm, note+rank, preference, other
    // TODO #RELEASE2 in note+roll/alarm/rank: textView with dataBase
    // TODO #RELEASE2 in preference: textView with data, button for reset all keys
    // TODO #RELEASE2 in other: open intro, open alarm

    // TODO #RELEASE2 activity: toolbar, fragment
    // TODO #RELEASE2 change toolbar text when change fragment

    @Inject internal lateinit var viewModel: IDevelopViewModel

    private val introButton by lazy { findViewById<Button?>(R.id.develop_intro_button) }

    override fun onCreate(savedInstanceState: Bundle?) {
        ScriptumApplication.component.getDevelopBuilder().set(activity = this).build()
                .inject(activity = this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_develop)

        viewModel.onSetup()

        introButton?.setOnClickListener { viewModel.onIntroClick() }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    override fun fillAboutNoteTable(data: String) {
        findViewById<ProgressBar?>(R.id.develop_note_progress)?.visibility = View.GONE
        findViewById<ScrollView?>(R.id.develop_note_scroll)?.visibility = View.VISIBLE

        findViewById<TextView?>(R.id.develop_note_text)?.text = data
    }

    override fun fillAboutRollTable(data: String) {
        findViewById<ProgressBar?>(R.id.develop_roll_progress)?.visibility = View.GONE
        findViewById<ScrollView?>(R.id.develop_roll_scroll)?.visibility = View.VISIBLE

        findViewById<TextView?>(R.id.develop_roll_text)?.text = data
    }

    override fun fillAboutRankTable(data: String) {
        findViewById<ProgressBar?>(R.id.develop_rank_progress)?.visibility = View.GONE
        findViewById<ScrollView?>(R.id.develop_rank_scroll)?.visibility = View.VISIBLE

        findViewById<TextView?>(R.id.develop_rank_text)?.text = data
    }

    override fun fillAboutPreference(data: String) {
        findViewById<ProgressBar?>(R.id.develop_preference_progress)?.visibility = View.GONE
        findViewById<ScrollView?>(R.id.develop_preference_scroll)?.visibility = View.VISIBLE

        findViewById<TextView?>(R.id.develop_preference_text)?.text = data
    }

    override fun startIntroActivity() = startActivity(IntroActivity[this])

}