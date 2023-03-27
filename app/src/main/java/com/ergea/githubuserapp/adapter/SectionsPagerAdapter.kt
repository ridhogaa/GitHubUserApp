package com.ergea.githubuserapp.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ergea.githubuserapp.ui.follows.followers.FollowersFragment
import com.ergea.githubuserapp.ui.follows.following.FollowingFragment

/**
 * @Author: ridhogymnastiar
 * Github: https://github.com/ridhogaa
 */

class SectionsPagerAdapter(
    fragment: Fragment,
    bundle: Bundle
) : FragmentStateAdapter(fragment) {

    private var fragmentBundle: Bundle = bundle

    init {
        fragmentBundle = bundle
    }

    override fun getItemCount(): Int = 2

    override fun createFragment(
        position: Int
    ): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowersFragment()
            1 -> fragment = FollowingFragment()
        }
        fragment?.arguments = this.fragmentBundle
        return fragment as Fragment
    }
}