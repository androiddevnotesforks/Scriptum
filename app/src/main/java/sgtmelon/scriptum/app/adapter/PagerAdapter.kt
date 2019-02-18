package sgtmelon.scriptum.app.adapter

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import sgtmelon.scriptum.app.ui.intro.IntroActivity
import sgtmelon.scriptum.app.ui.intro.IntroFragment
import java.util.*

/**
 * Адаптер страниц для вступления [IntroActivity]
 */
class PagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val itemList = ArrayList<IntroFragment>()

    fun addItem(introFragment: IntroFragment) {
        itemList.add(introFragment)
    }

    fun notifyItem(position: Int, positionOffset: Float) {
        val alpha = Math.max(0.2F, positionOffset)
        val scale = Math.max(0.75F,  positionOffset)

        getItem(position).setChange(alpha, scale)
    }

    override fun getItem(position: Int): IntroFragment {
        return itemList[position]
    }

    override fun getCount(): Int {
        return itemList.size
    }

}