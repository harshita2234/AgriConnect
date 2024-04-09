package com.example.agriconnect

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.firestore
import java.util.concurrent.TimeUnit

class OtpActivity : AppCompatActivity() {
    private lateinit var otpEditText: EditText
    private lateinit var verifyOtpButton: Button
    private var verificationId: String? = null
    private var phonenumber: String? = null
    private val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        otpEditText = findViewById(R.id.otpEditText)
        verifyOtpButton = findViewById(R.id.verifyOtpButton)
        verificationId = intent.getStringExtra("verificationId")
        phonenumber = intent.getStringExtra("phonenumber")
        verifyOtpButton.setOnClickListener {
            val otp = otpEditText.text.toString().trim()
            if (otp.isNotEmpty() && verificationId != null) {
                verifyVerificationCode(otp)
            } else {
                Toast.makeText(this, "Please enter valid OTP", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun verifyVerificationCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(this@OtpActivity) { task ->
                if (task.isSuccessful) {
                    db.collection("users").whereEqualTo("Phone Number", phonenumber)
                        .get()
                        .addOnSuccessListener { documents ->
                            if (!documents.isEmpty){
                                val intent = Intent(this@OtpActivity, HomeActivity::class.java)
                                intent.putExtra("phonenumber", phonenumber)
                                startActivity(intent)
                            } else {
                                val intent = Intent(this@OtpActivity, SignUpActivity::class.java)
                                intent.putExtra("phonenumber", phonenumber)
                                startActivity(intent)
                            }
                            finish()
                        }
                        .addOnFailureListener { _ ->
                        }

                } else {
                    Toast.makeText(this, "Verification Failed", Toast.LENGTH_SHORT).show()
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(this, "Verification Failed because code is INVALID", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
    override fun onStart() {
        super.onStart()
        val sharedPreferences = getSharedPreferences("User", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("Phone Number", phonenumber)
        editor.apply()
    }
}
