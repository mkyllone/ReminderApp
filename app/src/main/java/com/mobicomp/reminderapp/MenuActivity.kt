package com.mobicomp.reminderapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import com.mobicomp.reminderapp.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val context = this
        val db = DBHandler(context)
        val userId = intent.getIntExtra("userId", 0)
        val data = db.readData()

        // Display username on the screen
        binding.txtUsername.text = ""
        binding.txtUsername.append(data[userId].username)

        // Creating listview
        val listView = findViewById<ListView>(R.id.listReminders)
        val names = arrayOf("Reminder1 example", "Reminder2 example", "Reminder3 example", "Reminder4 example")
        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_1, names)
        listView.adapter = arrayAdapter

        binding.btnLogout.setOnClickListener{
            Log.d("Logout", "Log out button clicked")
            startActivity(
                    Intent(applicationContext, MainActivity::class.java)
            )
        }

        binding.btnEditProfile.setOnClickListener{
            Log.d("Edit profile", "Edit profile button clicked")

            // Pass user index into ChangeActivity
            val intent = Intent(this@MenuActivity, ChangeActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }

        binding.btnNewRem.setOnClickListener{
            Log.d("New reminder", "New reminder button clicked")
            startActivity(
                    Intent(applicationContext, NewReminderActivity::class.java)
            )
        }
    }

}







