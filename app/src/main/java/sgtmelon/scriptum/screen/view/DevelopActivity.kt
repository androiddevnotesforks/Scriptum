package sgtmelon.scriptum.screen.view

import android.os.Bundle
import android.widget.Button
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

    private val introButton: Button? by lazy { findViewById<Button>(R.id.develop_intro_button) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_develop)

        viewModel.onSetup()

        introButton?.setOnClickListener { viewModel.onIntroClick() }
    }

    override fun fillAboutNoteTable(data: String) {
        findViewById<TextView?>(R.id.develop_note_text)?.text = data
    }

    override fun fillAboutRollTable(data: String) {
        findViewById<TextView?>(R.id.develop_roll_text)?.text = data
    }

    override fun fillAboutRankTable(data: String) {
        findViewById<TextView?>(R.id.develop_rank_text)?.text = data
    }

    override fun fillAboutPreference(data: String) {
        findViewById<TextView?>(R.id.develop_preference_text)?.text = data
    }

}