package com.emirhanduman.coinmarkt.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emirhanduman.coinmarkt.R
import com.emirhanduman.coinmarkt.databinding.CurrencyItemLayoutBinding
import com.emirhanduman.coinmarkt.fragment.HomeFragmentDirections
import com.emirhanduman.coinmarkt.fragment.MarketFragmentDirections
import com.emirhanduman.coinmarkt.fragment.WatchlistFragmentDirections
import com.emirhanduman.coinmarkt.models.CryptoCurrency

class MarketAdapter(var context: Context, var list: List<CryptoCurrency>, var type: String) : RecyclerView.Adapter<MarketAdapter.MarketViewHolder>() {

    //view holder
    inner class MarketViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding = CurrencyItemLayoutBinding.bind(view)
    }

    //create view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketViewHolder {
        return MarketViewHolder(LayoutInflater.from(context).inflate(R.layout.currency_item_layout, parent, false))
    }

    //bind view holder
    override fun onBindViewHolder(holder: MarketViewHolder, position: Int) {
        val item = list[position]
        holder.binding.currencyNameTextView.text = item.name
        holder.binding.currencySymbolTextView.text = item.symbol

        Glide.with(context).load(
            "https://s2.coinmarketcap.com/static/img/coins/64x64/" + item.id + ".png"
        ).thumbnail(Glide.with(context).load(R.drawable.spinner)).into(holder.binding.currencyImageView)

        //set price
        holder.binding.currencyPriceTextView.text = String.format("%.5f", item.quotes[0].price)

        //set change
        //if change is negative, set text color red, else set text color green
        if(item.quotes[0].percentChange24h < 0){
            holder.binding.currencyChangeTextView.setTextColor(context.resources.getColor(R.color.red))
            holder.binding.currencyChangeTextView.text = "+ ${String.format("%.2f", item.quotes[0].percentChange24h)} %"
        }else{
            holder.binding.currencyChangeTextView.setTextColor(context.resources.getColor(R.color.green))
            holder.binding.currencyChangeTextView.text = "${String.format("%.2f", item.quotes[0].percentChange24h)} %"
        }

        //nav from home or market fragment to details fragment
        holder.itemView.setOnClickListener {

            if (type == "home"){
                findNavController(it).navigate(
                    HomeFragmentDirections.actionHomeFragmentToDetailsFragment(item)
                )
            }else if (type == "market"){
                findNavController(it).navigate(
                    MarketFragmentDirections.actionMarketFragmentToDetailsFragment(item)
                )
            }else {
                findNavController(it).navigate(
                    WatchlistFragmentDirections.actionWatchlistFragmentToDetailsFragment(item)
                )
            }

        }
    }

    //get item count
    override fun getItemCount(): Int {
        return list.size
    }

    //update list
    fun updateData(dataItem: List<CryptoCurrency>){
        list = dataItem
        notifyDataSetChanged()
    }


}