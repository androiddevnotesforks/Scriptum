package sgtmelon.scriptum.presentation.screen.ui.impl.preference

import android.content.Context
import android.content.Intent
import android.os.Bundle
import sgtmelon.scriptum.R
import sgtmelon.scriptum.presentation.screen.ui.ParentActivity

class DescriptionActivity : ParentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)
    }


    companion object {
        operator fun get(context: Context): Intent {
            return Intent(context, DescriptionActivity::class.java)
        }
    }
}