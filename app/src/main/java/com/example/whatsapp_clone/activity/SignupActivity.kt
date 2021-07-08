
package com.example.whatsapp_clone.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.whatsapp_clone.databinding.ActivitySignupBinding
import com.example.whatsapp_clone.util.DATA_USERS
import com.example.whatsapp_clone.util.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDB = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            var proceed = true
            if (binding.etName.text.isEmpty()) {
                binding.etName.error = "Name is required"
                proceed = false
            }
            if (binding.etPhone.text.isEmpty()) {
                binding.etPhone.error = "Phone is required"
                proceed = false
            }
            if (binding.etEmailSignUp.text.isEmpty()) {
                binding.etEmailSignUp.error = "Email is required"
                proceed = false
            }
            if (binding.etPasswordSignUp.text.isEmpty()) {
                binding.etPasswordSignUp.error = "Password is required"
                proceed = false
            }
            if (proceed) {
                signUp()
            }
        }
    }

    private fun signUp() {
        firebaseAuth.createUserWithEmailAndPassword(binding.etEmailSignUp.text.toString(), binding.etPasswordSignUp.text.toString())
            .addOnCompleteListener {
                if (!it.isSuccessful) { return@addOnCompleteListener
                    startActivity(Intent(this, SignupActivity::class.java))
                } else if (firebaseAuth.uid != null) {
                    val name = binding.etName.text.toString()
                    val phone = binding.etPhone.text.toString()
                    val email = binding.etEmailSignUp.text.toString()
                    val user = User(name, phone, email, "", "Hello, I'm new", "", "")
                    firebaseDB.collection(DATA_USERS).document(firebaseAuth.uid!!).set(user)

                    startActivity(Intent(this, MainActivity::class.java))
                }
            }
            .addOnFailureListener {
                Log.d("Main", "Failed Login: ${it.message}")
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
    }


}