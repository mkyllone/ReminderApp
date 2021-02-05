package com.mobicomp.reminderapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.mobicomp.reminderapp.databinding.ActivityChangePwBinding

class ChangePwActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePwBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePwBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)

        val context = this
        val db = DBHandler(context)
        val userId = intent.getIntExtra("userId", 0)
        val username = db.getUsername(userId)
        val password = db.getPassword(userId)

        binding.btnSubmit.setOnClickListener{
            if(binding.txtOldPassword.text.toString().isEmpty() ||
                binding.txtNewPassword.text.toString().isEmpty() ||
                binding.txtConfNewPassword.text.toString().isEmpty()){
                Toast.makeText(context, "Please fill all fields!", Toast.LENGTH_SHORT).show()
            }
            else if(binding.txtOldPassword.text.toString() != password){
                Toast.makeText(context, "Your current password is incorrect. " +
                        "Please try again", Toast.LENGTH_SHORT).show()
            }
            else if(binding.txtNewPassword.text.toString() != binding.txtConfNewPassword.text.toString()){
                Toast.makeText(context, "Passwords do not match. Please try again", Toast.LENGTH_SHORT).show()
            }
            else{
                db.updateUserInformation(userId, username, binding.txtNewPassword.text.toString())
                Toast.makeText(context, "Password changed successfully", Toast.LENGTH_SHORT).show()

                // Pass user index into MenuActivity
                val intent = Intent(this@ChangePwActivity, MenuActivity::class.java)
                intent.putExtra("userId", userId)
                startActivity(intent)
            }
        }
    }
}