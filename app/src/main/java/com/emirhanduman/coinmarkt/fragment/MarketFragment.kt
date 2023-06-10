package com.emirhanduman.coinmarkt.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.emirhanduman.coinmarkt.adapter.MarketAdapter
import com.emirhanduman.coinmarkt.apis.ApiInterface
import com.emirhanduman.coinmarkt.apis.ApiUtilities
import com.emirhanduman.coinmarkt.databinding.FragmentMarketBinding
import com.emirhanduman.coinmarkt.models.CryptoCurrency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList


class MarketFragment : Fragment() {

    private lateinit var binding : FragmentMarketBinding

    //list of crypto currencies
    private lateinit var list: List<CryptoCurrency>

    private lateinit var adapter: MarketAdapter

    private lateinit var search : String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMarketBinding.inflate(layoutInflater)

        list = listOf()

        //set adapter
        adapter = MarketAdapter(requireContext(), list,"market")
        binding.currencyRecyclerView.adapter = adapter

        //get data from api
        lifecycleScope.launch(Dispatchers.IO) {
            val result = ApiUtilities.getInstance().create(ApiInterface::class.java).getMarketData()

            if (result.body() != null){
                withContext(Dispatchers.Main){
                    list = result.body()!!.data.cryptoCurrencyList

                    adapter.updateData(list)
                }
            }
        }

        searchCrypto()

        return binding.root
    }

    //search crypto
    private fun searchCrypto() {
        binding.searchEditText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }


            override fun afterTextChanged(s: Editable?) {
                search = s.toString().lowercase()

                updateRecView()
            }
        })
    }

    //update recyclerview according to search
    private fun updateRecView() {
        val data = ArrayList<CryptoCurrency>()

        for (item in list){
            val cryptoName = item.name.lowercase(Locale.getDefault())
            val cryptoSymbol = item.symbol.lowercase(Locale.getDefault())

            if (cryptoName.contains(search) || cryptoSymbol.contains(search)){
                data.add(item)
            }
        }

        adapter.updateData(data)
    }

}