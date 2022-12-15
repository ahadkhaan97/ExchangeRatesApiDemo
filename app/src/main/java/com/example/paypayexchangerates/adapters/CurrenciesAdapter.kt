package com.example.paypayexchangerates.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.paypayexchangerates.databinding.RowBinding
import com.example.paypayexchangerates.model.recycler.RecyclerItem

class CurrenciesAdapter(private val currencyList: ArrayList<RecyclerItem>) :
    RecyclerView.Adapter<CurrenciesAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: RowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = RowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrenciesAdapter.ViewHolder, position: Int) {
        with(holder) {
            with(currencyList[position]) {
                binding.currencyShort.text = this.short
                binding.currencyLong.text = this.name
                binding.value.text = this.value.toString()
            }
        }
    }

    override fun getItemCount(): Int {
        return currencyList.size
    }
}