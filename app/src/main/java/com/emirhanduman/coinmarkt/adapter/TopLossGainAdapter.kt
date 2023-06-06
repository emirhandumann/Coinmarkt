package com.emirhanduman.coinmarkt.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.emirhanduman.coinmarkt.fragment.TopLossGainFragment

class TopLossGainAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    // there will be 2 tabs which are Top Gainers and Top Losers
    override fun getItemCount(): Int {
        return 2
    }

    // create fragment for each tab
    override fun createFragment(position: Int): Fragment {
        val fragment = TopLossGainFragment()
        val bundle = Bundle()
        bundle.putInt("position", position)
        fragment.arguments = bundle
        return fragment

    }

}