package com.mobicomp.reminderapp

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.mobicomp.reminderapp.databinding.ActivityNewReminderBinding
import java.lang.String.format
import java.util.*

class NewReminderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewReminderBinding
    @RequiresApi(Build.VERSION_CODES.N)


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
        val month = c.get(Calendar.MONTH) + 1
        val day = c.get(Calendar.DAY_OF_MONTH)
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        // Submit button
        binding.btnSubmit3.setOnClickListener{

            if(binding.txtMessage.text.toString().isEmpty() ||
                    binding.txtRemDate.text.toString() == "Date" ||
                    binding.txtRemTime.text.toString() == "Time" ||
                    binding.txtReminderType.text.toString() == "Reminder type"){
                Toast.makeText(context, "Please fill all fields!", Toast.LENGTH_SHORT).show()
            }
            else{
                val reminder = Reminder(binding.txtMessage.text.toString(),
                        imgId,
                        "locationX",
                        "locationY",
                        binding.txtRemDate.text.toString() + " " +
                                binding.txtRemTime.text.toString(),
                        day.toString() + "/" + month.toString() + "/" + year.toString() +
                                " " + format("%02d:%02d", hour, minute),
                        userId,
                        0
                )

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
                binding.txtRemDate.text = "" + dayOfMonth + "/" + month + "/" + year
            }, year, month, day)
            date.show()
        }

        // TimePicker
        binding.btnTime.setOnClickListener{

            val time = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener
            { _, hourOfDay, minute ->
                binding.txtRemTime.text = format("%02d:%02d", hourOfDay, minute)
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


        // Text to speech button
        binding.speak.setOnClickListener{
            val speakIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            speakIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now!")
            startActivityForResult(speakIntent, REQUEST_CODE_STT)
        }



    }

    // Text to speech
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

