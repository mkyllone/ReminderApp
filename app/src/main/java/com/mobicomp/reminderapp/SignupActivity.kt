package com.mobicomp.reminderapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.mobicomp.reminderapp.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)

        val context = this
        val db = DBHandler(context)

        binding.SignupBtn.setOnClickListener{

            if(binding.userName.text.toString().isEmpty() ||
                    binding.txtPassword.text.toString().isEmpty() ||
                    binding.passwordConfirm.text.toString().isEmpty()){
                        Toast.makeText(context, "Please fill all fields!", Toast.LENGTH_SHORT).show()
            }
            else if (binding.passwordConfirm.text.toString() != binding.txtPassword.text.toString()){
                Toast.makeText(context, "Passwords do not match! Please try again.", Toast.LENGTH_SHORT).show()
            }
            else if (db.userExist(binding.userName.text.toString())){
                Toast.makeText(context, "Username is not available! Please try again.", Toast.LENGTH_SHORT).show()
            }
            else{
                val user = User(binding.userName.text.toString(), binding.txtPassword.text.toString())

                db.insertData(user)
                startActivity(
                        Intent(applicationContext, SigninActivity::class.java)
                )
            }
        }
    }
}