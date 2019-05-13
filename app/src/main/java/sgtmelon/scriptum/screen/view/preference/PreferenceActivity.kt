package sgtmelon.scriptum.screen.view.preference

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import sgtmelon.scriptum.R
import sgtmelon.scriptum.office.utils.ColorUtils.getDrawable
import sgtmelon.scriptum.screen.view.AppActivity

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
        navigationIcon = getDrawable(R.drawable.ic_cancel_exit, R.attr.clContent)
        setNavigationOnClickListener { finish() }
    }

}