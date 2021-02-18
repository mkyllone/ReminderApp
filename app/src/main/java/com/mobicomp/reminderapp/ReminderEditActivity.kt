package com.mobicomp.reminderapp

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.mobicomp.reminderapp.databinding.ActivityReminderEditBinding
import kotlin.String
import java.util.*

class ReminderEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReminderEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReminderEditBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val context = this
        val db = DBHandler(context)
        val userId = intent.getIntExtra("userId", 0)
        val remId = intent.getIntExtra("remId", 0)
        var imgId : Int = 4

        // Display reminder information
        val reminderList = db.getReminder(remId)
        binding.imgType.setImageResource(reminderList[0].toInt())
        binding.txtMessage4.text = reminderList[1]
        binding.txtDate4.text = reminderList[2]

        // Calendar
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH) + 1
        val day = c.get(Calendar.DAY_OF_MONTH)
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        // Submit button
        binding.btnSubmit4.setOnClickListener{

            if(binding.txtEditMessage.text.toString().isEmpty() ||
                    binding.txtEditDate.text.toString() == "Date" ||
                    binding.txtEditTime.text.toString() == "Time" ||
                    binding.txtEditReminderType.text.toString() == "Reminder type"){
                Toast.makeText(context, "Please fill all fields!", Toast.LENGTH_SHORT).show()
            }
            else{
                db.updateReminderInformation(remId, binding.txtEditMessage.text.toString(),
                        binding.txtEditDate.text.toString() + " " +
                                binding.txtEditTime.text.toString(), imgId
                        )

                val intent = Intent(this@ReminderEditActivity, MenuActivity::class.java)
                intent.putExtra("userId", userId)
                startActivity(intent)
            }

        }

        // Delete button
        binding.btnDelete.setOnClickListener{

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Delete")
            builder.setMessage("Are you sure you want to delete this reminder?")
            val dialogClickListener = DialogInterface.OnClickListener{ _, which ->
                when(which){
                    DialogInterface.BUTTON_POSITIVE -> {
                        db.deleteReminder(remId)
                        Toast.makeText(this@ReminderEditActivity,
                                "Reminder removed successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@ReminderEditActivity, MenuActivity::class.java)
                        intent.putExtra("userId", userId)
                        startActivity(intent)
                    }
                    DialogInterface.BUTTON_NEGATIVE ->{
                    }
                    DialogInterface.BUTTON_NEUTRAL ->{
                    }
                }
            }

            builder.setPositiveButton("YES",dialogClickListener)
            builder.setNegativeButton("NO",dialogClickListener)
            builder.setNeutralButton("CANCEL",dialogClickListener)

            val dialog = builder.create()
            dialog.show()
        }

        // DatePicker
        binding.btnDate2.setOnClickListener{

            val date = DatePickerDialog(this, DatePickerDialog.OnDateSetListener
            { _, year, month, dayOfMonth ->
                binding.txtEditDate.text = "" + dayOfMonth + "/" + month + "/" + year
            }, year, month, day)
            date.show()
        }

        // TimePicker
        binding.btnTime2.setOnClickListener{

            val time = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener
            { _, hourOfDay, minute ->
                binding.txtEditTime.text = String.format("%02d:%02d", hourOfDay, minute)
            },hour, minute, true)
            time.show()
        }

        // Choosing the type of reminder (for icon)
        binding.btnReminderType.setOnClickListener{

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Choose a reminder type")

            val types = arrayOf("Meeting", "Call", "Email", "Food", "Other" )
            builder.setItems(types){_, which ->
                when (which){
                    0 -> {
                        binding.txtEditReminderType.text = types[0]
                        imgId = R.drawable.ic_meeting_room_24px
                    }
                    1 -> {
                        binding.txtEditReminderType.text = types[1]
                        imgId = R.drawable.ic_phone_24px
                    }
                    2 -> {
                        binding.txtEditReminderType.text = types[2]
                        imgId = R.drawable.ic_email_24px
                    }
                    3 -> {
                        binding.txtEditReminderType.text = types[3]
                        imgId = R.drawable.ic_fastfood_24px
                    }
                    4 -> {
                        binding.txtEditReminderType.text = types[4]
                        imgId = R.drawable.ic_grade_24px
                    }
                }
            }
            val dialog = builder.create()
            dialog.show()
        }
    }
}