package com.example.agriconnect

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date

class AddListingActivity: AppCompatActivity() {
    private lateinit var crop:EditText
    private lateinit var quantity:EditText
    private lateinit var location:EditText
    private lateinit var price:EditText
    private lateinit var add:Button
    private val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_listing)
        val prefs = getSharedPreferences("User", MODE_PRIVATE)
        val phoneNumber = prefs.getString("Phone Number", "0")
        val role = prefs.getString("Role", "")
        crop = findViewById(R.id.cropEditText)
        quantity = findViewById(R.id.quantityEditText)
        location = findViewById(R.id.locationEditText)
        price = findViewById(R.id.priceEditText)
        add=findViewById(R.id.add)
        add.setOnClickListener {
            if (crop.text.toString().isNotEmpty() && quantity.text.toString().isNotEmpty() && location.text.toString().isNotEmpty() && price.text.toString().isNotEmpty()) {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val listing = hashMapOf(
                    "Phone Number" to phoneNumber,
                    "Role" to role,
                    "Crop" to crop.text.toString(),
                    "Quantity" to quantity.text.toString(),
                    "Location" to location.text.toString(),
                    "Price" to price.text.toString(),
                    "Transaction ID" to "",
                    "Date/Time" to dateFormat.format(Date())
                )
                db.collection("listings")
                    .add(listing)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            finish()
                        }
                    }
            } else{
                Toast.makeText(this, "Please enter valid data", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
