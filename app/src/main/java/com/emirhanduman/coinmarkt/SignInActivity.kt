package com.emirhanduman.coinmarkt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.TextView
import android.widget.Toast
import com.emirhanduman.coinmarkt.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var binding : ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //view binding
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val createAccount = binding.textViewCreateAccount

        createAccount.setOnClickListener {
            //go to sign up activity
            val intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)

            //finish this activity
            finish()
        }


        auth = Firebase.auth

        binding.buttonSignIn.setOnClickListener {

            //if fields are in firebase database
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            if (checkFields()) {

                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                    //if sign in is successful
                    if (it.isSuccessful) {
                        Toast.makeText(this,"Successfully sign in",Toast.LENGTH_SHORT).show()

                        //go to market activity
                        val intent = Intent(this,MarketActivity::class.java)
                        startActivity(intent)

                        //finish this activity
                        finish()
                    }
                    //if sign in is not successful
                    else {
                       Log.e("error: ",it.exception.toString())
                    }
                }
            }
        }

    }
    private fun checkFields() : Boolean {
        val email = binding.editTextEmail.text.toString()
        val password = binding.editTextPassword.text.toString()

        //is email empty
        if (email == "") {
            binding.textInputLayoutEmail.error = "Please enter your email"
            return false
        }
        //does email have a valid type
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.textInputLayoutEmail.error = "Please enter a valid email"
            return false
        }
        //is password empty
        if (password == "") {
            binding.textInputLayoutPassword.error = "Please enter your password"
            binding.textInputLayoutPassword.errorIconDrawable = null
            return false
        }
        //password length must be at least 8
        if (password.length < 8) {
            binding.textInputLayoutPassword.error = "Password must be at least 8 characters"
            binding.textInputLayoutPassword.errorIconDrawable = null
            return false
        }
        return true
    }
}