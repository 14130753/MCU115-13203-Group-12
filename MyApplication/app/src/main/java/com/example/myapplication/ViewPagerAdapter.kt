package com.example.myapplication

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MainCourseFragment()
            1 -> SideDishFragment()
            2 -> BeverageFragment()
            3 -> ConfirmFragment()
            else -> MainCourseFragment()
        }
    }
}