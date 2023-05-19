package com.emirhanduman.coinmarkt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //There won't be action bar in main activity
        supportActionBar?.hide()

        //2 seconds delay
        Handler(Looper.getMainLooper()).postDelayed({
            //start next activity
            val intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)

            //finish this activity
            finish()
        },2000)

    }
}