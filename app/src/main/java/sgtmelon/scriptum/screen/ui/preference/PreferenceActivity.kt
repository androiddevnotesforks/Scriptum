package sgtmelon.scriptum.screen.ui.preference

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import sgtmelon.scriptum.R
import sgtmelon.scriptum.dagger.component.ScriptumComponent
import sgtmelon.scriptum.extension.getTintDrawable
import sgtmelon.scriptum.screen.ui.AppActivity
import sgtmelon.scriptum.screen.ui.ScriptumApplication
import sgtmelon.scriptum.screen.vm.callback.IAppViewModel

/**
 * Screen for display [PreferenceFragment].
 */
class PreferenceActivity : AppActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        ScriptumApplication.component.getAppBuilder().set(activity = this).build()
                .inject(activity = this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preference)

        setupToolbar()

        fm.beginTransaction()
                .replace(R.id.preference_fragment_container, PreferenceFragment())
                .commit()
    }

    private fun setupToolbar() = findViewById<Toolbar>(R.id.toolbar_container).apply {
        title = getString(R.string.title_preference)
        navigationIcon = getTintDrawable(R.drawable.ic_cancel_exit)
        setNavigationOnClickListener { finish() }
    }

    companion object {
        operator fun get(context: Context) = Intent(context, PreferenceActivity::class.java)
    }

}