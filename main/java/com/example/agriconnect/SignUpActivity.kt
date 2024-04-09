package com.example.agriconnect

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.util.Objects

class SignUpActivity : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var roleRadioGroup: RadioGroup
    private lateinit var accountEditText: EditText
    private lateinit var ifscEditText: EditText
    private lateinit var saveButton: Button
    private var role=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        val phonenumber = intent.getStringExtra("phonenumber")
        nameEditText = findViewById(R.id.nameEditText)
        roleRadioGroup = findViewById(R.id.roleRadioGroup)
        accountEditText = findViewById(R.id.accountEditText)
        ifscEditText = findViewById(R.id.ifscEditText)
        saveButton = findViewById(R.id.save)
        val db = Firebase.firestore
        saveButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val selectedRadioButtonId = roleRadioGroup.checkedRadioButtonId
            val account = accountEditText.text.toString()
            val ifsc = ifscEditText.text.toString()

            if (name.isNotEmpty() && selectedRadioButtonId != -1 && account.isNotEmpty() && ifsc.isNotEmpty()) {
                val radioButton: RadioButton = findViewById(selectedRadioButtonId)
                role = radioButton.text.toString()

                val user = hashMapOf(
                    "Phone Number" to phonenumber,
                    "Name" to name,
                    "Role" to role,
                    "Account Number" to account,
                    "IFSC Code" to ifsc
                )

                db.collection("users")
                    .add(user)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this@SignUpActivity, "Account Created!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@SignUpActivity, HomeActivity::class.java)
                            intent.putExtra("phonenumber", phonenumber)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@SignUpActivity, "Failed to create account", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this@SignUpActivity, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
