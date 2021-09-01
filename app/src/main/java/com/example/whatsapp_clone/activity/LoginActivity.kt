package com.example.whatsapp_clone.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.whatsapp_clone.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            var proceed = true
            if (binding.etEmail.text.isEmpty()) {
                binding.etEmail.error = "Email is required"
                proceed = false
            }

            if (binding.etPassword.text.isEmpty()) {
                binding.etPassword.error = "Password is required"
                proceed = false
            }

            if(proceed) {
                signIn()
            }
         }

        binding.tvSignUp.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signIn() {
        firebaseAuth.signInWithEmailAndPassword(binding.etEmail.text.toString(), binding.etPassword.text.toString())
            .addOnCompleteListener {
                if (!it.isSuccessful) { return@addOnCompleteListener
                    Toast.makeText(this, "login Error : ${it.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
                    val intent = Intent (this, LoginActivity::class.java)
                    startActivity(intent)
                } else
                    Toast.makeText(this, "Succesfully Login", Toast.LENGTH_SHORT).show()
                    val intent = Intent (this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
            }
            .addOnFailureListener {
                Log.d("Main", "Failed Login: ${it.message}")
                Toast.makeText(this, "Email/Password incorrect", Toast.LENGTH_SHORT).show()
            }
    }
}