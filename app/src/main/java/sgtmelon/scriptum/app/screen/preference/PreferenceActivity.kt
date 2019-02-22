package sgtmelon.scriptum.app.screen.preference

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.screen.BaseActivityParent
import sgtmelon.scriptum.office.utils.ColorUtils.getDrawable

class PreferenceActivity : BaseActivityParent() {

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

    private fun setupToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar_container)

        toolbar.title = getString(R.string.title_preference)
        toolbar.navigationIcon = getDrawable(R.drawable.ic_cancel_exit, R.attr.clContent)
        toolbar.setNavigationOnClickListener { finish() }
    }

}