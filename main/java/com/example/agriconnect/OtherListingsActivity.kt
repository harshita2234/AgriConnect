package com.example.agriconnect

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.agriconnect.adapters.ListingsAdapter
import com.example.agriconnect.models.Listing
import com.google.firebase.firestore.FirebaseFirestore

class OtherListingsActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_listings)
        recyclerView = findViewById(R.id.listingsRecyclerView)

        val prefs = getSharedPreferences("User", MODE_PRIVATE)
        val phoneNumber = prefs.getString("Phone Number", "") ?: ""
        val role = prefs.getString("Role", "") ?: ""

        db.collection("listings")
            .whereNotEqualTo("Role", role)
            .whereEqualTo("Transaction ID", "")
            .get()
            .addOnSuccessListener { documents ->
                val listings = documents.map { doc ->
                    Listing(
                        id = doc.id,
                        phoneNumber = doc.getString("Phone Number") ?: "",
                        role = doc.getString("Role") ?: "",
                        crop = doc.getString("Crop") ?: "",
                        quantity = doc.getString("Quantity") ?: "",
                        location = doc.getString("Location") ?: "",
                        price = doc.getString("Price") ?: ""
                    )
                }
                recyclerView.adapter = ListingsAdapter(listings, role) { id ->
                    if (role == "Merchant") {
                        val intent = Intent(this, TransactionActivity::class.java).apply {
                            putExtra("documentId", id)
                        }
                        startActivity(intent)
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error fetching documents: ", exception)
            }
    }
}
