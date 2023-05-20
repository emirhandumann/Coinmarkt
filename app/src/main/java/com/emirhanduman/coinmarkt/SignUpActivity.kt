package com.emirhanduman.coinmarkt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.emirhanduman.coinmarkt.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var binding : ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //view binding
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        val haveAccount = binding.textViewHaveAccount

        haveAccount.setOnClickListener {

            //go to sign in activity
            val intent = Intent(this,SignInActivity::class.java)
            startActivity(intent)

            haveAccount.movementMethod = LinkMovementMethod.getInstance();

            //finish this activity
            finish()
        }


        binding.buttonSignUp.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            if (checkFields()) {
                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                     //if account is created successfully
                    if (it.isSuccessful) {
                        auth.signOut()
                        Toast.makeText(this,"Account is created successfully",Toast.LENGTH_SHORT).show()

                        // go to main activity
                        val intent = Intent(this,MainActivity::class.java)
                        startActivity(intent)

                        //finish this activity
                        finish()
                    }
                    //account is not created successfully
                    else {
                        Log.e("Error",it.exception.toString())
                    }
                }
            }
        }




    }
    //check if all fields are filled correctly (it doesn't check these are in database or not)
    private fun checkFields() : Boolean {
        val email = binding.editTextEmail.text.toString()
        val password = binding.editTextPassword.text.toString()
        val passwordConfirm = binding.editTextPasswordConfirm.text.toString()

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
        //is confirm password empty
        if (passwordConfirm == "") {
            binding.textInputLayoutPasswordConfirm.error = "Please confirm your password"
            binding.textInputLayoutPasswordConfirm.errorIconDrawable = null
            return false
        }
        //are password and confirm password equal
        if (password != passwordConfirm) {
            binding.textInputLayoutPasswordConfirm.error = "Passwords do not match"
            return false
        }
        return true
    }

    private fun goToSignIn() {

        val intent = Intent(this,SignInActivity::class.java)
        startActivity(intent)

        finish()
    }
}