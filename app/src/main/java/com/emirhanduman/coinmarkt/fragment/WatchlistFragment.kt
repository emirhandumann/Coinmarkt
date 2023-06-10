package com.emirhanduman.coinmarkt.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.emirhanduman.coinmarkt.R
import com.emirhanduman.coinmarkt.adapter.MarketAdapter
import com.emirhanduman.coinmarkt.apis.ApiInterface
import com.emirhanduman.coinmarkt.apis.ApiUtilities
import com.emirhanduman.coinmarkt.databinding.FragmentWatchlistBinding
import com.emirhanduman.coinmarkt.models.CryptoCurrency
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WatchlistFragment : Fragment() {

    private lateinit var binding: FragmentWatchlistBinding
    private lateinit var watchList: ArrayList<String>
    private lateinit var watchListData: ArrayList<CryptoCurrency>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentWatchlistBinding.inflate(layoutInflater)

        readData()

        // get data from api and set up watchlistRecyclerView

        lifecycleScope.launch(Dispatchers.IO) {
            val result = ApiUtilities.getInstance().create(ApiInterface::class.java).getMarketData()

            if (result.body() != null) {
                withContext(Dispatchers.Main) {
                    watchListData = ArrayList()
                    watchListData.clear()

                    for(watchData in watchList) {
                        for (item in result.body()!!.data.cryptoCurrencyList) {
                            if (watchData == item.symbol) {
                                watchListData.add(item)
                            }
                        }
                    }

                    binding.watchlistRecyclerView.adapter =  MarketAdapter(requireContext(),watchListData,"watchlist")
                }
            }
        }
        return binding.root
    }

    private fun readData() {
        val sharedPreferences = requireContext().getSharedPreferences("watchlist", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("watchlist", ArrayList<String>().toString())
        val type = object : TypeToken<ArrayList<String>>() {}.type
        watchList = gson.fromJson(json, type)
    }

}