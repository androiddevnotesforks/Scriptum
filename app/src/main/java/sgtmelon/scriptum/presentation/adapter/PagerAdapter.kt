package sgtmelon.scriptum.presentation.adapter

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import sgtmelon.scriptum.screen.ui.intro.IntroActivity
import sgtmelon.scriptum.screen.ui.intro.IntroFragment
import java.util.*
import kotlin.math.max

/**
 * Adapter which displays pages of intro for [IntroActivity]
 */
class PagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val listItem = ArrayList<IntroFragment>()

    fun addItem(introFragment: IntroFragment) = listItem.add(introFragment)

    fun notifyItem(position: Int, positionOffset: Float) = getItem(position)
            .setChange(max(0.2F, positionOffset), max(0.75F, positionOffset))

    override fun getItem(position: Int) = listItem[position]

    override fun getCount() = listItem.size

}