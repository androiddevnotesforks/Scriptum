package sgtmelon.scriptum.screen.view

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import sgtmelon.scriptum.R
import sgtmelon.scriptum.screen.callback.DevelopCallback
import sgtmelon.scriptum.screen.vm.DevelopViewModel

/**
 * Экран для подробного отображения информации из бд
 *
 * @author SerjantArbuz
 */
class DevelopActivity : AppCompatActivity(), DevelopCallback {

    private val viewModel: DevelopViewModel by lazy {
        ViewModelProviders.of(this).get(DevelopViewModel::class.java).apply {
            callback = this@DevelopActivity
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_develop)

        viewModel.onSetup()
    }

    override fun fillAboutNoteTable(data: String) {
        findViewById<TextView?>(R.id.note_text)?.text = data
    }

    override fun fillAboutRollTable(data: String) {
        findViewById<TextView?>(R.id.roll_text)?.text = data
    }

    override fun fillAboutRankTable(data: String) {
        findViewById<TextView?>(R.id.rank_text)?.text = data
    }

    override fun fillAboutPreference(data: String) {
        findViewById<TextView?>(R.id.preference_text)?.text = data
    }

}