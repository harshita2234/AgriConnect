package com.example.agriconnect

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class TransactionActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)

        val documentId = intent.getStringExtra("documentId") ?: ""
        // Assume these are correctly linked to your layout components
        val cropTextView: TextView = findViewById(R.id.cropTextView)
        val quantityTextView: TextView = findViewById(R.id.quantityTextView)
        val locationTextView: TextView = findViewById(R.id.locationTextView)
        val priceTextView: TextView = findViewById(R.id.priceTextView)
        val accountNumberTextView: TextView = findViewById(R.id.accountNumberTextView)
        val ifscCodeTextView: TextView = findViewById(R.id.ifscCodeTextView)
        val transactionStatusTextView: TextView = findViewById(R.id.transactionStatusTextView)
        val transactionIdEditText: EditText = findViewById(R.id.transactionIdEditText)
        val submitButton: Button = findViewById(R.id.submitButton)

        // Retrieve and display listing information
        db.collection("listings").document(documentId).get()
            .addOnSuccessListener { document ->
                cropTextView.text = "Crop: ${document.getString("Crop")}"
                quantityTextView.text = "Quantity: ${document.getString("Quantity")}"
                locationTextView.text = "Location: ${document.getString("Location")}"
                priceTextView.text = "Price: ${document.getString("Price")}"

                // Retrieve and display user (farmer's) payment information
                val phoneNumber = document.getString("Phone Number") ?: ""
                db.collection("users").whereEqualTo("Phone Number", phoneNumber).get()
                    .addOnSuccessListener { userDocuments ->
                        for (userDoc in userDocuments) {
                            accountNumberTextView.text = "Account Number: ${userDoc.getString("Account Number")}"
                            ifscCodeTextView.text = "IFSC Code: ${userDoc.getString("IFSC Code")}"
                        }
                    }

                // Set transaction status text
                val transactionId = document.getString("Transaction ID")
                if (transactionId.isNullOrEmpty()) {
                    transactionStatusTextView.text = "Status: Not Accepted Yet"
                    transactionStatusTextView.setTextColor(resources.getColor(R.color.grey))
                } else {
                    transactionStatusTextView.text = "Status: Accepted"
                    transactionStatusTextView.setTextColor(resources.getColor(R.color.green))
                }
            }

        // Handle submit button click
        submitButton.setOnClickListener {
            val transactionId = transactionIdEditText.text.toString().trim()
            if (transactionId.isEmpty()) {
                Toast.makeText(this, "Please fill Transaction ID", Toast.LENGTH_SHORT).show()
            } else {
                // Fetch the merchant's phone number from SharedPreferences
                val prefs = getSharedPreferences("User", MODE_PRIVATE)
                val merchantPhoneNumber = prefs.getString("Phone Number", "") ?: ""

                // Update the listing with the transaction ID and merchant's phone number
                db.collection("listings").document(documentId)
                    .update(mapOf(
                        "Transaction ID" to transactionId,
                        "Merchant Phone Number" to merchantPhoneNumber
                    ))
                    .addOnSuccessListener {
                        Toast.makeText(this, "Transaction completed successfully.", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error updating transaction: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}
