package com.example.agriconnect.adapters

import android.annotation.SuppressLint
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.agriconnect.R
import com.example.agriconnect.models.Listing

class ListingViewHolder(view: View): RecyclerView.ViewHolder(view) {
    @SuppressLint("SetTextI18n")
    fun bind(listing: Listing, userRole: String, onClick: (String) -> Unit) {
        itemView.findViewById<TextView>(R.id.cropTextView).text = "Crop: ${listing.crop}"
        itemView.findViewById<TextView>(R.id.quantityTextView).text = "Quantity: ${listing.quantity}"
        itemView.findViewById<TextView>(R.id.locationTextView).text = "Location: ${listing.location}"
        itemView.findViewById<TextView>(R.id.priceTextView).text = "Price: ${listing.price}"
        itemView.findViewById<Button>(R.id.acceptButton).setOnClickListener {
            onClick(listing.id)
        }
        val acceptButton = itemView.findViewById<Button>(R.id.acceptButton)
        if (userRole == "Farmer") {
            acceptButton.visibility = View.GONE // Hide button for farmers
        } else if (userRole == "Merchant") {
            acceptButton.visibility = View.VISIBLE // Show button for merchants
            acceptButton.setOnClickListener { onClick(listing.id) }
        }
    }
}
