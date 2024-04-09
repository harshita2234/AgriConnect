package com.example.agriconnect

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore


class HomeActivity : AppCompatActivity() {
    //private val db = Firebase.firestore
    private lateinit var greeting:TextView
    private lateinit var logout:Button
    private lateinit var yourlistings:Button
    private lateinit var otherlistings:Button
    private lateinit var chatwithus:Button
    private lateinit var addlisting:Button
    private var role:String?=null
    val db=Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        greeting=findViewById(R.id.greeting)
        logout=findViewById(R.id.logout)
        yourlistings=findViewById(R.id.yourlistings)
        otherlistings=findViewById(R.id.otherlistings)
        chatwithus=findViewById(R.id.chatwithus)
        addlisting=findViewById(R.id.addlisting)
        updategreeting()
        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val sharedPreferences = getSharedPreferences("User", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.clear() // This removes all data
            editor.apply()
            val intent = Intent(this@HomeActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
        yourlistings.setOnClickListener {
            startActivity(Intent(this@HomeActivity, YourListingsActivity::class.java))
        }

        otherlistings.setOnClickListener {
            startActivity(Intent(this@HomeActivity, OtherListingsActivity::class.java))
        }
        addlisting.setOnClickListener{
            startActivity(Intent(this@HomeActivity, AddListingActivity::class.java))
        }
    }

    private fun updategreeting() {
        val prefs = getSharedPreferences("User", MODE_PRIVATE)
        val phoneNumber = prefs.getString("Phone Number", null)
        if (phoneNumber != null) {
            val storedRole = prefs.getString("Role", null)
            if (storedRole == null) {
                db.collection("users")
                    .whereEqualTo("Phone Number", phoneNumber)
                    .limit(1)
                    .get()
                    .addOnSuccessListener { documents ->
                        if (documents.documents.isNotEmpty()) {
                            val user = documents.documents.first()
                            val role = user.getString("Role") ?: ""
                            // Save and update greeting in UI thread
                            runOnUiThread {
                                val prefs = getSharedPreferences("User", MODE_PRIVATE).edit()
                                prefs.putString("Role", role)
                                prefs.apply()
                                greeting.text = getString(R.string.greeting_message, role)
                            }
                        }
                    }
                    .addOnFailureListener {
                    }
            } else {
                greeting.text = getString(R.string.greeting_message, storedRole)
            }
        }
    }
}