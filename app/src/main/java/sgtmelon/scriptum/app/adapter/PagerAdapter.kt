package sgtmelon.scriptum.app.adapter

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import sgtmelon.scriptum.app.screen.view.intro.IntroActivity
import sgtmelon.scriptum.app.screen.view.intro.IntroFragment
import java.util.*

/**
 * Адаптер страниц для вступления [IntroActivity]
 */
class PagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val listItem = ArrayList<IntroFragment>()

    fun addItem(introFragment: IntroFragment) = listItem.add(introFragment)

    fun notifyItem(position: Int, positionOffset: Float) = getItem(position)
            .setChange(Math.max(0.2F, positionOffset), Math.max(0.75F, positionOffset))

    override fun getItem(position: Int) = listItem[position]

    override fun getCount() = listItem.size

}