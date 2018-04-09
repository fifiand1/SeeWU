package com.wzf.wucarryme.modules.main.adapter

import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import java.util.ArrayList

class HomePagerAdapter : FragmentPagerAdapter {

    private val fragments = ArrayList<Fragment>()
    private val titles = ArrayList<String>()

    constructor(fm: FragmentManager) : super(fm) {}

    constructor(fm: FragmentManager, tabLayout: TabLayout) : super(fm) {}

    fun addTab(fragment: Fragment, title: String) {
        fragments.add(fragment)
        titles.add(title)

    }

    /**
     * Return the Fragment associated with a specified position.
     */
    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    /**
     * Return the number of views available.
     */
    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return titles[position]
    }
}
