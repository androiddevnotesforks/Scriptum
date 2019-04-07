package sgtmelon.scriptum.app.screen.view.pref

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.screen.view.AppActivity
import sgtmelon.scriptum.office.utils.ColorUtils.getDrawable

/**
 * Активити для отображения [PrefFragment]
 *
 * @author SerjantArbuz
 */
class PrefActivity : AppActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preference)

        setupToolbar()

        // TODO исправить
        fragmentManager
                .beginTransaction()
                .replace(R.id.preference_fragment_container, PrefFragment())
                .commit()
    }

    private fun setupToolbar() = findViewById<Toolbar>(R.id.toolbar_container).apply {
        title = getString(R.string.title_preference)
        navigationIcon = getDrawable(R.drawable.ic_cancel_exit, R.attr.clContent)
        setNavigationOnClickListener { finish() }
    }

}