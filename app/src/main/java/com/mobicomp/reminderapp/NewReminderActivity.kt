package com.mobicomp.reminderapp

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.mobicomp.reminderapp.databinding.ActivityNewReminderBinding
import java.lang.String.format
import java.util.*
import java.util.concurrent.TimeUnit

class NewReminderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewReminderBinding
    @RequiresApi(Build.VERSION_CODES.N)

    var locationX : Double = 0.0
    var locationY : Double = 0.0

    @SuppressLint("DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = ActivityNewReminderBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        var imgId : Int = 4
        val context = this
        val db = DBHandler(context)
        val userId = intent.getIntExtra("userId", 0)

        // Calendar
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        // Time&datepicker values
        var remYear = 0
        var remMonth = 0
        var remDay = 0
        var remHour = 0
        var remMinute = 0

        // Variable for notifications on/off
        var showNotification : Boolean = true

        // Submit button
        binding.btnSubmit3.setOnClickListener{

            if(binding.txtMessage.text.toString().isEmpty() ||
                    binding.txtRemDate.text.toString() == "Date" ||
                    binding.txtRemTime.text.toString() == "Time" ||
                    binding.txtReminderType.text.toString() == "Reminder type" ||
                    binding.txtNotificationVisibility.text.toString() == "Reminder notifications"
                    ){
                Toast.makeText(context, "Please fill all fields!", Toast.LENGTH_SHORT).show()
            }
            else{

                // Making a new reminder
                val reminder = Reminder(binding.txtMessage.text.toString(),
                        imgId,
                        locationX,
                        locationY,
                        binding.txtRemDate.text.toString() + " " +
                                binding.txtRemTime.text.toString(),
                        day.toString() + "/" + month.toString() + "/" + year.toString() +
                                " " + format("%02d:%02d", hour, minute),
                        userId,
                        0
                )

                // Convert notification time (hours) to milliseconds
                var delay : Int = 0
                if (binding.txtNumber.text.toString().isNotEmpty()) {
                    delay = binding.txtNumber.text.toString().toInt() * 3600000

                }

                // Timing requirement
                var timingRequirement : Int = 0
                if (binding.txtNumber.text.toString().isEmpty()) {
                    timingRequirement = 0
                }
                else{
                    timingRequirement = 1
                }

                // Data passed to WorkManager
                val data: Data = workDataOf(
                        "message" to binding.txtMessage.text.toString(),
                        "date" to binding.txtRemDate.text.toString() + " " +
                                binding.txtRemTime.text.toString(),
                        "imgId" to imgId,
                        "userId" to userId,
                        "showNotification" to showNotification,
                        "timingRequirement" to timingRequirement
                )
                val dataDue: Data = workDataOf(
                        "message" to binding.txtMessage.text.toString(),
                        "date" to "Reminder due is now!",
                        "imgId" to imgId,
                        "showNotification" to showNotification,
                        "timingRequirement" to 1
                )

                val currentDate = Calendar.getInstance()
                val dueDate = Calendar.getInstance()
                dueDate.set(Calendar.YEAR, remYear)
                dueDate.set(Calendar.MONTH, remMonth)
                dueDate.set(Calendar.DAY_OF_MONTH, remDay)
                dueDate.set(Calendar.HOUR_OF_DAY, remHour)
                dueDate.set(Calendar.MINUTE, remMinute)

                // Calculating the delays for WorkManager
                val timeDelay = dueDate.timeInMillis - currentDate.timeInMillis - delay
                val timeDelayDue = dueDate.timeInMillis - currentDate.timeInMillis

                // Creating a new WorkManager including a given reminder time, before due
                val reminderWorkerRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
                        .setInitialDelay(timeDelay, TimeUnit.MILLISECONDS)
                        .setInputData(data)
                        .build()
                WorkManager.getInstance(context).enqueue(reminderWorkerRequest)



                // Creating a new WorkManager when the reminder is due
                val reminderWorkerRequestDue = OneTimeWorkRequestBuilder<ReminderWorker>()
                        .setInitialDelay(timeDelayDue, TimeUnit.MILLISECONDS)
                        .setInputData(dataDue)
                        .build()
                WorkManager.getInstance(context).enqueue(reminderWorkerRequestDue)

                db.insertDataReminder(reminder)
                val intent = Intent(this@NewReminderActivity, MenuActivity::class.java)
                intent.putExtra("userId", userId)
                startActivity(intent)
            }

        }

        // DatePicker
        binding.btnDate.setOnClickListener{

            val date = DatePickerDialog(this, DatePickerDialog.OnDateSetListener
            { _, year, month, dayOfMonth ->
                binding.txtRemDate.text = "" + dayOfMonth + "/" + (month+1) + "/" + year
                remYear = year
                remMonth = month
                remDay = dayOfMonth
            }, year, month, day)
            date.show()
        }

        // TimePicker
        binding.btnTime.setOnClickListener{

            val time = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener
            { _, hourOfDay, minute ->
                binding.txtRemTime.text = format("%02d:%02d", hourOfDay, minute)
                remHour = hourOfDay
                remMinute = minute
            },hour, minute, true)
            time.show()
        }

        // Choosing the type of reminder (for icon)
        binding.btnType.setOnClickListener{

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Choose a reminder type")

            val types = arrayOf("Meeting", "Call", "Email", "Food", "Other" )
            builder.setItems(types){_, which ->
                when (which){
                    0 -> {
                        binding.txtReminderType.text = types[0]
                        imgId = R.drawable.ic_meeting_room_24px
                    }
                    1 -> {
                        binding.txtReminderType.text = types[1]
                        imgId = R.drawable.ic_phone_24px
                    }
                    2 -> {
                        binding.txtReminderType.text = types[2]
                        imgId = R.drawable.ic_email_24px
                    }
                    3 -> {
                        binding.txtReminderType.text = types[3]
                        imgId = R.drawable.ic_fastfood_24px
                    }
                    4 -> {
                        binding.txtReminderType.text = types[4]
                        imgId = R.drawable.ic_grade_24px
                    }
                }
            }
            val dialog = builder.create()
            dialog.show()
        }


        // Speech-to-text button
        binding.speak.setOnClickListener{
            val speakIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            speakIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now!")
            startActivityForResult(speakIntent, REQUEST_CODE_STT)
        }

        // Reminder notifications on/off button
        binding.btnNotification.setOnClickListener{

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Do you want to make reminder with a notification?")

            val types = arrayOf("Yes", "No")
            builder.setItems(types) { _, which ->
                when (which) {
                    0 -> {
                        binding.txtNotificationVisibility.text = "Notifications on"
                        showNotification = true
                    }
                    1 -> {
                        binding.txtNotificationVisibility.text = "Notifications off"
                        showNotification = false
                    }
                }
            }
            val dialog = builder.create()
            dialog.show()
        }

        // Button for selecting location for reminder
        binding.btnLocation2.setOnClickListener{

            val requestCode = 0
            val intent = Intent(this, MapsActivityReminder::class.java)
            startActivityForResult(intent, requestCode)

        }

    }

    // Speech-to-text
    companion object {
        private const val REQUEST_CODE_STT = 1
    }

    private val textToSpeechEngine: TextToSpeech by lazy {
        TextToSpeech(this,
                TextToSpeech.OnInitListener { status ->
                    if (status == TextToSpeech.SUCCESS) {
                        textToSpeechEngine.language = Locale.UK
                    }
                })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_STT -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    if (!result.isNullOrEmpty()) {
                        val recognizedText = result[0]
                        binding.txtMessage.setText(recognizedText)
                    }
                }
            }
        }
        when(requestCode) {
            0 -> {
                if (data != null) {
                    locationX = data.getStringExtra("latitude").toString().toDouble()
                    locationY = data.getStringExtra("longitude").toString().toDouble()
                    binding.txtLocation.text = format("Lat: %06f\nLong: %06f", locationX, locationY)
                }; }
        }
    }

    override fun onPause() {
        textToSpeechEngine.stop()
        super.onPause()
    }

    override fun onDestroy() {
        textToSpeechEngine.shutdown()
        super.onDestroy()
    }





}

