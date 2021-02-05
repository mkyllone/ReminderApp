package com.mobicomp.reminderapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.mobicomp.reminderapp.databinding.ActivityChangeBinding

class ChangeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val userId = intent.getIntExtra("userId", 0)

        binding.btnChangeName.setOnClickListener{
            Log.d("Change name", "Change name button clicked")

            // Pass user index into ChangeUnActivity
            val intent = Intent(this@ChangeActivity, ChangeUnActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }

        binding.btnChangePassword.setOnClickListener{
            Log.d("Change password", "Change password button clicked")

            // Pass user index into ChangePwActivity
            val intent = Intent(this@ChangeActivity, ChangePwActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }
    }
}