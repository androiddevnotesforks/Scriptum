package sgtmelon.scriptum.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import sgtmelon.scriptum.domain.model.data.IntroData
import sgtmelon.scriptum.presentation.screen.ui.impl.intro.IntroActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.intro.IntroFragment
import java.lang.ref.WeakReference

/**
 * Adapter which displays pages of intro for [IntroActivity].
 */
class IntroPageAdapter(
    fm: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fm, lifecycle) {

    private val fragmentMap = mutableMapOf<Int, WeakReference<IntroFragment>>()

    override fun getItemCount(): Int = IntroData.count

    override fun createFragment(position: Int): Fragment {
        val fragment = IntroFragment[position]

        fragmentMap[position] = WeakReference(fragment)

        return fragment
    }

    private fun getItem(position: Int): IntroFragment? = fragmentMap[position]?.get()

    fun notifyItem(position: Int, offset: Float) {
        getItem(position)?.setChange(offset)
    }

}