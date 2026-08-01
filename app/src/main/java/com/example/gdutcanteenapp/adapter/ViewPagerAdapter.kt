package com.example.gdutcanteenapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.gdutcanteenapp.ui.canteen.canteenlist.CanteenFragment
import com.example.gdutcanteenapp.ui.home.HomeFragment
import com.example.gdutcanteenapp.ui.profile.ProfileFragment

class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    private val fragments: List<Fragment> = listOf<Fragment>(
        HomeFragment(),
        CanteenFragment(),
        ProfileFragment()
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}