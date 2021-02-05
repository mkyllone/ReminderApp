package com.mobicomp.reminderapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.mobicomp.reminderapp.databinding.ActivitySigninBinding
import android.content.Intent

class SigninActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySigninBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)

        val context = this
        val db = DBHandler(context)

        binding.btnLogin.setOnClickListener{

            if(binding.txtUser.text.toString().isEmpty() ||
                    binding.txtPassword.text.toString().isEmpty()){
                Toast.makeText(context, "Please fill all fields!", Toast.LENGTH_SHORT).show()
            }
            else if(!db.userExist(binding.txtUser.text.toString())){
                Toast.makeText(context, "Username does not exist! Please try again.", Toast.LENGTH_SHORT).show()
            }
            else if(db.loginCheck(binding.txtUser.text.toString(), binding.txtPassword.text.toString()) == -1) {
                Toast.makeText(context, "Username and password not match! Please try again.", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()

                // Pass user index into MenuActivity
                val intent = Intent(this@SigninActivity, MenuActivity::class.java)
                intent.putExtra("userId", db.loginCheck(binding.txtUser.text.toString(), binding.txtPassword.text.toString()))
                startActivity(intent)
            }
        }
    }
}