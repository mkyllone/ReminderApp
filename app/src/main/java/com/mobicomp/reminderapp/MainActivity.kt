package com.mobicomp.reminderapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.mobicomp.reminderapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)
        val view=binding.root
        setContentView(view)

        binding.signinButton.setOnClickListener{
            Log.d("Signin", "Sign in button clicked")
            startActivity(
                Intent(applicationContext, SigninActivity::class.java)
            )
        }

        binding.signupButton.setOnClickListener{
            Log.d("Signup", "Sign up button clicked")
            startActivity(
                Intent(applicationContext, SignupActivity::class.java)
            )
        }
    }
}