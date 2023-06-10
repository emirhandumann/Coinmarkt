package com.emirhanduman.coinmarkt.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.emirhanduman.coinmarkt.adapter.MarketAdapter
import com.emirhanduman.coinmarkt.apis.ApiInterface
import com.emirhanduman.coinmarkt.apis.ApiUtilities
import com.emirhanduman.coinmarkt.databinding.FragmentTopLossGainBinding
import com.emirhanduman.coinmarkt.models.CryptoCurrency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Collections

class TopLossGainFragment : Fragment() {

    lateinit var binding : FragmentTopLossGainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding  = FragmentTopLossGainBinding.inflate(layoutInflater)

        getMarketData()

        return binding.root
    }

    private fun getMarketData() {

        val position = requireArguments().getInt("position")

        lifecycleScope.launch(Dispatchers.IO){

            //get data from api
            val result = ApiUtilities.getInstance().create(ApiInterface::class.java).getMarketData()

            if (result.body() != null) {
                withContext(Dispatchers.Main){
                    val dataItem = result.body()!!.data.cryptoCurrencyList

                    //sort data by percentChange24h
                    Collections.sort(dataItem) { o1, o2 ->
                        (o2.quotes[0].percentChange24h.toInt()).compareTo(o1.quotes[0].percentChange24h.toInt())
                    }

                    val list = ArrayList<CryptoCurrency>()

                    //if position is 0, get top 10 gainers, else get top 10 losers
                    if (position == 0) {
                        list.clear()
                        for (i in 0..9) {
                            list.add(dataItem[i])
                        }

                        binding.topGainLoseRecyclerView.adapter = MarketAdapter(requireContext(), list, "home")
                    } else {
                        list.clear()
                        for (i in 0..9) {
                            list.add(dataItem[dataItem.size - 1 - i])
                        }

                        binding.topGainLoseRecyclerView.adapter = MarketAdapter(requireContext(), list, "home")
                    }

                }
            }

        }

    }

}