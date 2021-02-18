package com.mobicomp.reminderapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_reminder_array_adapter.view.*

class ReminderArrayAdapter(context: Context, reminders: List<Reminder>)
    : ArrayAdapter<Reminder>(context, 0, reminders) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val rootView = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.activity_reminder_array_adapter, parent, false)
        val currentReminder = getItem(position)

        if (currentReminder != null) {
            rootView.reminderImage.setImageResource(currentReminder.img_id)
            rootView.reminderMsg.text = currentReminder.message
            rootView.reminderTime.text = currentReminder.reminder_time
        }
        return rootView
    }
}










