package com.emirhanduman.coinmarkt.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.emirhanduman.coinmarkt.R
import com.emirhanduman.coinmarkt.databinding.FragmentDetailsBinding
import com.emirhanduman.coinmarkt.models.CryptoCurrency
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import kotlin.math.log

class DetailsFragment : Fragment() {

    lateinit var binding : FragmentDetailsBinding

    private val item : DetailsFragmentArgs by navArgs()

    var watchList : ArrayList<String>? = null

    var watchListIsChecked = false



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDetailsBinding.inflate(layoutInflater)

        val data : CryptoCurrency = item.data!!

        setUpDetails(data)

        loadChart(data)

        addWatchlist(data)

        return binding.root
    }



    //set up details
    private fun setUpDetails(data: CryptoCurrency) {

        //name
        binding.detailSymbolTextView.text = data.symbol

        Glide.with(requireContext()).load(
            "https://s2.coinmarketcap.com/static/img/coins/64x64/" + data.id + ".png"
        ).thumbnail(Glide.with(requireContext()).load(R.drawable.spinner)).into(binding.detailImageView)

        //price
        binding.detailPriceTextView.text = "${String.format("$%.4f",data.quotes[0].price)}"

        //percentchange24h
        if(data.quotes[0].percentChange24h < 0){
            binding.detailChangeTextView.setTextColor(requireContext().resources.getColor(R.color.red))
            binding.detailChangeImageView.setImageResource(R.drawable.ic_arrow_up)
            binding.detailChangeTextView.text = "+ ${String.format("%.2f", data.quotes[0].percentChange24h)} %"
        }else{
            binding.detailChangeTextView.setTextColor(requireContext().resources.getColor(R.color.green))
            binding.detailChangeImageView.setImageResource(R.drawable.ic_arrow_down)
            binding.detailChangeTextView.text = "${String.format("%.2f", data.quotes[0].percentChange24h)} %"
        }

        //marketcap
        binding.detailMarketCapValueTextView.text = "${String.format("$%.2f",data.quotes[0].marketCap)}"
        //percentchange30d
        binding.detailPercentChange30dValueTextView.text = "${String.format("%.2f", data.quotes[0].percentChange30d)} %"
        //volume24h
        binding.detailVolume24hValueTextView.text = "${String.format("$%.2f",data.quotes[0].volume24h)}"


    }


    //load chart from Api
    @SuppressLint("SetJavaScriptEnabled")
    private fun loadChart(item: CryptoCurrency) {
        binding.detailChartWebView.settings.javaScriptEnabled = true
        binding.detailChartWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        Log.d("TAG", "loadChart: " + item.symbol)
        binding.detailChartWebView.loadUrl(
            "https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol=" +  item.symbol +
                    "/USDT&interval=&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=" +
                    "[]&hideideas=1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides=" +
                    "{}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=" +
                    "coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT"
        )
    }

    private fun addWatchlist(data: CryptoCurrency) {

        readData()

        // check if crypto is in watchlist
        watchListIsChecked = if (watchList!!.contains(data.symbol)) {
            // if crypto is in watchlist, set ic_star
            binding.addWatchlistButton.setImageResource(R.drawable.ic_star)
            true
        } else {
            // if crypto is not in watchlist, set ic_star_outline
            binding.addWatchlistButton.setImageResource(R.drawable.ic_star_outline)
            false
        }

        binding.addWatchlistButton.setOnClickListener {
            // if crypto is not in watchlist, add crypto to watchlist and set ic_star
            watchListIsChecked =
                if (!watchListIsChecked) {
                    if (!watchList!!.contains(data.symbol)) {
                        watchList!!.add(data.symbol)
                    }
                    storeData()
                    binding.addWatchlistButton.setImageResource(R.drawable.ic_star)
                    true
                }
                // if crypto is in watchlist, remove crypto from watchlist and set ic_star_outline
                else {
                    binding.addWatchlistButton.setImageResource(R.drawable.ic_star_outline)
                    watchList!!.remove(data.symbol)
                    storeData()
                    false
                }
        }
    }

    // read data from shared preferences and convert to ArrayList
    private fun readData() {
        val sharedPreferences = requireContext().getSharedPreferences("watchlist", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("watchlist", ArrayList<String>().toString())
        val type = object : TypeToken<ArrayList<String>>() {}.type
        watchList = gson.fromJson(json, type)
    }

    // store data to shared preferences as json
    private fun storeData() {
        val sharedPreferences = requireContext().getSharedPreferences("watchlist", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(watchList)
        editor.putString("watchlist", json)
        editor.apply()
    }

}