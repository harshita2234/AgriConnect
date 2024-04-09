package com.example.agriconnect.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.agriconnect.R
import com.example.agriconnect.models.Listing


class YourListingsAdapter(private val listings: List<Listing>, private val userRole: String) : RecyclerView.Adapter<YourListingsAdapter.YourListingViewHolder>() {

    inner class YourListingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val cropTextView: TextView = view.findViewById(R.id.cropTextView)
        private val quantityTextView: TextView = view.findViewById(R.id.quantityTextView)
        private val locationTextView: TextView = view.findViewById(R.id.locationTextView)
        private val priceTextView: TextView = view.findViewById(R.id.priceTextView)
        private val statusTextView: TextView = view.findViewById(R.id.statusTextView)

        fun bind(listing: Listing) {
            cropTextView.text = "Crop: ${listing.crop}"
            quantityTextView.text = "Quantity: ${listing.quantity}"
            locationTextView.text = "Location: ${listing.location}"
            priceTextView.text = "Price: ${listing.price}"

            if (userRole == "Merchant") {
                statusTextView.visibility = View.GONE // Hide status for merchants
            } else {
                if (listing.transactionId.isEmpty()) {
                    statusTextView.text = "Not Accepted Yet"
                    statusTextView.setTextColor(itemView.context.resources.getColor(R.color.grey, null))
                } else {
                    val statusText = if (listing.merchantPhoneNumber.isNotEmpty()) {
                        "Accepted by ${listing.merchantPhoneNumber}"
                    } else {
                        "Accepted"
                    }
                    statusTextView.text = statusText
                    statusTextView.setTextColor(itemView.context.resources.getColor(R.color.green, null))
                }
                statusTextView.visibility = View.VISIBLE // Show status for other roles
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YourListingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.your_listing_card, parent, false)
        return YourListingViewHolder(view)
    }

    override fun onBindViewHolder(holder: YourListingViewHolder, position: Int) {
        holder.bind(listings[position])
    }

    override fun getItemCount() = listings.size
}
