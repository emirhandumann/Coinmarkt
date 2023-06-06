package com.emirhanduman.coinmarkt.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.emirhanduman.coinmarkt.adapter.TopLossGainAdapter
import com.emirhanduman.coinmarkt.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)


        //set tab layout
        setTabLayout()


        return binding.root
    }

    //which tab is selected
    private fun setTabLayout() {

        val adapter = TopLossGainAdapter(this)
        binding.contentViewPager.adapter = adapter

        //when tab is selected, change the indicator
        binding.contentViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                //if position is 0, it means top gainers tab is selected
                if (position == 0) {
                    binding.topGainIndicator.visibility = VISIBLE
                    binding.topLoseIndicator.visibility = GONE
                }
                //if position is 1, it means top losers tab is selected
                else {
                    binding.topGainIndicator.visibility = GONE
                    binding.topLoseIndicator.visibility = VISIBLE
                }

            }
        })


        //set tab titles
        TabLayoutMediator(binding.tabLayout, binding.contentViewPager) { tab, position ->
            var title = ""
            if (position == 0) {
                title = "Top Gainers"
            }
            else {
                title = "Top Losers"
            }
            tab.text = title
        }.attach()

    }

}


