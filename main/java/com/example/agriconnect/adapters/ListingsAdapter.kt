package com.example.agriconnect.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.agriconnect.R
import com.example.agriconnect.models.Listing

class ListingsAdapter(
    private val listings: List<Listing>,
    private val userRole: String,
    private val onClick: (String) -> Unit
): RecyclerView.Adapter<ListingViewHolder>() {    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listing_card, parent, false)
        return ListingViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListingViewHolder, position: Int) {
        holder.bind(listings[position], userRole, onClick)
    }

    override fun getItemCount(): Int = listings.size
}
