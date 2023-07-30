package com.paybacktraders.paybacktraders.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.paybacktraders.paybacktraders.fragments.WalletHistoryFragment
import com.paybacktraders.paybacktraders.fragments.WalletWithdrawlFragment

class ViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 2 // Number of tabs/pages

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> WalletHistoryFragment()
            1 -> WalletWithdrawlFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }


}