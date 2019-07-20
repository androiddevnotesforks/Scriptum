package sgtmelon.scriptum.screen.ui.preference

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import sgtmelon.scriptum.R
import sgtmelon.scriptum.extension.getTintDrawable
import sgtmelon.scriptum.screen.ui.AppActivity

/**
 * Активити для отображения [PreferenceFragment]
 *
 * @author SerjantArbuz
 */
class PreferenceActivity : AppActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preference)

        setupToolbar()

        // TODO исправить
        fragmentManager
                .beginTransaction()
                .replace(R.id.preference_fragment_container, PreferenceFragment())
                .commit()
    }

    private fun setupToolbar() = findViewById<Toolbar>(R.id.toolbar_container).apply {
        title = getString(R.string.title_preference)
        navigationIcon = getTintDrawable(R.drawable.ic_cancel_exit)
        setNavigationOnClickListener { finish() }
    }

    companion object {
        fun getInstance(context: Context) = Intent(context, PreferenceActivity::class.java)
    }

}