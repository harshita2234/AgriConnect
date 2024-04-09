package com.example.agriconnect

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.agriconnect.adapters.YourListingsAdapter
import com.example.agriconnect.models.Listing
import com.google.firebase.firestore.FirebaseFirestore

class YourListingsActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_your_listings)
        recyclerView = findViewById(R.id.yourListingsRecyclerView)

        val prefs = getSharedPreferences("User", MODE_PRIVATE)
        val phoneNumber = prefs.getString("Phone Number", "") ?: ""
        val userRole = prefs.getString("Role","")?:""

        db.collection("listings").whereEqualTo("Phone Number", phoneNumber).get()
            .addOnSuccessListener { documents ->
                val listings = documents.map { doc ->
                    Listing(
                        id = doc.id,
                        phoneNumber = doc.getString("Phone Number") ?: "",
                        role = doc.getString("Role") ?: "",
                        crop = doc.getString("Crop") ?: "",
                        quantity = doc.getString("Quantity") ?: "",
                        location = doc.getString("Location") ?: "",
                        price = doc.getString("Price") ?: "",
                        transactionId = doc.getString("Transaction ID") ?: "",
                        merchantPhoneNumber = doc.getString("Merchant Phone Number") ?: ""
                    )
                }
                recyclerView.adapter = YourListingsAdapter(listings,userRole)
            }
    }
}
