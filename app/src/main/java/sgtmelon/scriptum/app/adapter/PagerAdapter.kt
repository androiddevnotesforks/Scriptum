package sgtmelon.scriptum.app.adapter

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import sgtmelon.scriptum.app.view.activity.IntroActivity
import sgtmelon.scriptum.app.view.fragment.IntroFragment
import java.util.*

/**
 * Адаптер страниц для вступления [IntroActivity]
 */
class PagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val itemList = ArrayList<IntroFragment>()

    fun addItem(introFragment: IntroFragment) {
        itemList.add(introFragment)
    }

    override fun getItem(position: Int): IntroFragment {
        return itemList[position]
    }

    override fun getCount(): Int {
        return itemList.size
    }

}