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


        binding.SignupBtn.setOnClickListener{

            if(binding.userName.text.toString().isEmpty() ||
                    binding.passWord.text.toString().isEmpty() ||
                    binding.passwordConfirm.text.toString().isEmpty()){
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }

            else if (binding.passwordConfirm.text.toString() != binding.passWord.text.toString()){
                Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
            }

            else{
                var user = User_information(binding.userName.text.toString(), binding.passWord.text.toString())
                var db = DB_Handler(context)
                db.insertData(user)
                startActivity(
                        Intent(applicationContext, MenuActivity::class.java)
                )
            }
        }
    }
}