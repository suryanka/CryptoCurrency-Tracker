package com.ghosh.cryptocurrencytracker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ghosh.cryptocurrencytracker.databinding.CryptoRvItemBinding

class CryptoAdapter(val context: Context, var cryptoList:ArrayList<CryptoRvModel>):
    RecyclerView.Adapter<CryptoAdapter.cryptoViewHolder> (){

    inner class cryptoViewHolder(val adapterBinding: CryptoRvItemBinding) :RecyclerView.ViewHolder(adapterBinding.root)
    {

    }
    fun filterList(filtList:ArrayList<CryptoRvModel>)
    {
        cryptoList=filtList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): cryptoViewHolder {
        val binding=CryptoRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return cryptoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: cryptoViewHolder, position: Int) {
        holder.adapterBinding.name.setText(cryptoList[position].name)
        holder.adapterBinding.price.setText(cryptoList[position].price)
        holder.adapterBinding.symbol.setText(cryptoList[position].symbol)
    }

    override fun getItemCount(): Int {
        return cryptoList.size
    }
}