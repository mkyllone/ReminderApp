package com.mobicomp.reminderapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.mobicomp.reminderapp.databinding.ActivityChangeUnBinding

class ChangeUnActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangeUnBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeUnBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)

        val context = this
        val db = DBHandler(context)
        val userId = intent.getIntExtra("userId", 0)
        val password = db.getPassword(userId)

        binding.btnSubmit2.setOnClickListener{

            when {
                binding.txtNewUsername.text.toString().isEmpty() -> {
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
                db.userExist(binding.txtNewUsername.text.toString()) -> {
                    Toast.makeText(context, "Username is not available! Please try again.", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    db.updateUserInformation(userId, binding.txtNewUsername.text.toString(), password)
                    Toast.makeText(context, "Username changed successfully!", Toast.LENGTH_SHORT).show()

                    // Pass user index into MenuActivity
                    val intent = Intent(this@ChangeUnActivity, MenuActivity::class.java)
                    intent.putExtra("userId", userId)
                    startActivity(intent)
                }
            }
        }
    }
}