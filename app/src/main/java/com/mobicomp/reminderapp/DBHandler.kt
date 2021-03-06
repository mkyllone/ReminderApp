package com.mobicomp.reminderapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.location.Location
import android.widget.Toast

val DATABASE_NAME = "DB"
val TABLE_NAME = "Users"
val TABLE_NAME_REM = "Reminders"
val COL_ID = "id"
val COL_ID_REM = "id"
val COL_USERNAME = "username"
val COL_PASSWORD = "password"
val COL_LATITUDE = "latitude"
val COL_LONGITUDE = "longitude"
val COL_MESSAGE = "message"
val COL_IMAGEID = "img_id"
val COL_LOCATION_X = "location_x"
val COL_LOCATION_Y = "location_y"
val COL_REMINDER_TIME = "reminder_time"
val COL_CREATION_TIME = "creation_time"
val COL_CREATOR_ID = "creator_id"
val COL_REMINDER_SEEN = "reminder_seen"

class DBHandler(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {

        val createTable = "CREATE TABLE " + TABLE_NAME + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_USERNAME + " VARCHAR(256)," + COL_PASSWORD + " VARCHAR(256)," + COL_LATITUDE + " REAL," +
                COL_LONGITUDE + " REAL)";

        val createTableReminders = "CREATE TABLE " + TABLE_NAME_REM + " (" + COL_ID_REM +
            " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_MESSAGE + " VARCHAR(256)," +
                COL_IMAGEID + " INTEGER," +
                COL_LOCATION_X + " REAL," + COL_LOCATION_Y + " REAL," +
                COL_REMINDER_TIME + " VARCHAR(256)," + COL_CREATION_TIME + " VARCHAR(256)," +
                COL_CREATOR_ID + " INTEGER," + COL_REMINDER_SEEN + " INTEGER)";

        db?.execSQL(createTable)
        db?.execSQL(createTableReminders)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    // Insert user data into  database
    fun insertData(user: User){
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(COL_USERNAME, user.username)
        cv.put(COL_PASSWORD, user.password)
        cv.put(COL_LATITUDE, 65.08238)
        cv.put(COL_LONGITUDE, 25.44262)


        val result = db.insert(TABLE_NAME, null, cv)

        if (result == (-1).toLong()) {
            Toast.makeText(context, "Account creation failed", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(context, "Account created successfully", Toast.LENGTH_SHORT).show()
        }
    }

    // Used for reading information from the database to list
    fun readData() : MutableList<User> {
        val list: MutableList<User> = ArrayList()
        val db = this.readableDatabase
        val query = "Select * from " + TABLE_NAME
        val result = db.rawQuery(query, null)

        if (result.moveToFirst()) {
            do {
                val user = User()
                user.username = result.getString(result.getColumnIndex(COL_USERNAME))
                user.password = result.getString(result.getColumnIndex(COL_PASSWORD))
                user.latitude = result.getDouble(result.getColumnIndex(COL_LATITUDE))
                user.longitude = result.getDouble(result.getColumnIndex(COL_LONGITUDE))
                list.add(user)
            } while (result.moveToNext())
        }
        result.close()
        db.close()
        return list
    }

    // Method which returns true if username already exists in database, otherwise false
    fun userExist(nick: String) : Boolean{

        val data = this.readData()
        for (i in 0 until data.size){
            if (data[i].username == nick){
                return true
            }
        }
        return false
    }

    // Method which returns user's id if given login information exists in database, otherwise -1
    fun loginCheck(nick: String, pw: String) : Int{

        val data = this.readData()
        for (i in 0 until data.size){
            if(nick == data[i].username && pw == data[i].password){
                return i
            }
        }
        return -1
    }

    // Method used for getting username by index
    fun getUsername(id: Int) : String{

        val data = this.readData()
        return data[id].username
    }

    // Method used for getting password by index
    fun getPassword(id: Int) : String{

        val data = this.readData()
        return data[id].password
    }

    // Used for changing password or username to the database by index
    fun updateUserInformation(id: Int, username: String, password: String){

        val cv = ContentValues()
        cv.put(COL_USERNAME, username)
        cv.put(COL_PASSWORD, password)
        val whereclause = "$COL_ID=?"
        val whereargs = arrayOf((id + 1).toString())
        this.writableDatabase.update(TABLE_NAME, cv, whereclause, whereargs)
    }

    // Used for inserting virtual latitude and longitude to user database
    fun insertVirtualLocation(id: Int, latitude: Double, longitude: Double){

        val cv = ContentValues()
        cv.put(COL_LATITUDE, latitude)
        cv.put(COL_LONGITUDE, longitude)
        val whereclause = "$COL_ID=?"
        val whereargs = arrayOf((id + 1).toString())
        this.writableDatabase.update(TABLE_NAME, cv, whereclause, whereargs)
    }

    // Insert reminder information into  database
    fun insertDataReminder(reminder: Reminder){
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(COL_MESSAGE, reminder.message)
        cv.put(COL_IMAGEID, reminder.img_id)
        cv.put(COL_LOCATION_X, reminder.location_x)
        cv.put(COL_LOCATION_Y, reminder.location_y)
        cv.put(COL_REMINDER_TIME, reminder.reminder_time)
        cv.put(COL_CREATION_TIME, reminder.creation_time)
        cv.put(COL_CREATOR_ID, reminder.creator_id)
        cv.put(COL_REMINDER_SEEN, reminder.reminder_seen)

        val result = db.insert(TABLE_NAME_REM, null, cv)

        if (result == (-1).toLong()) {
            Toast.makeText(context, "Reminder creation failed", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(context, "Reminder created successfully", Toast.LENGTH_SHORT).show()
        }
    }

    // Used for reading reminder information from the database to list
    private fun readDataReminders() : MutableList<Reminder> {
        val list: MutableList<Reminder> = ArrayList()
        val db = this.readableDatabase
        val query = "Select * from " + TABLE_NAME_REM
        val result = db.rawQuery(query, null)

        if (result.moveToFirst()) {
            do {
                val reminder = Reminder()
                reminder.message = result.getString(result.getColumnIndex(COL_MESSAGE))
                reminder.img_id = result.getInt(result.getColumnIndex(COL_IMAGEID))
                reminder.location_x = result.getDouble(result.getColumnIndex(COL_LOCATION_X))
                reminder.location_y = result.getDouble(result.getColumnIndex(COL_LOCATION_Y))
                reminder.reminder_time = result.getString(result.getColumnIndex(COL_REMINDER_TIME))
                reminder.creation_time = result.getString(result.getColumnIndex(COL_CREATION_TIME))
                reminder.creator_id = result.getInt(result.getColumnIndex(COL_CREATOR_ID))
                reminder.reminder_seen = result.getInt(result.getColumnIndex(COL_REMINDER_SEEN))
                list.add(reminder)
            } while (result.moveToNext())
        }
        result.close()
        db.close()
        return list
    }

    // Used for getting reminder information by user index, returns array of reminders.
    fun getReminderArray(id: Int) : MutableList<Reminder>{

        val list: MutableList<Reminder> = ArrayList()
        val data = this.readDataReminders()

        for (i in 0 until data.size){
            if (data[i].creator_id == id && data[i].reminder_seen == 1 && reminderInRange(id, i)){
                list.add(Reminder(data[i].img_id, data[i].message, data[i].reminder_time))
            }
        }
        return list
    }

    // Used for changing reminder information to database
    fun updateReminderInformation(id: Int, message: String, reminder_time: String, id_img: Int){

        val data = this.readDataReminders()
        val db = this.writableDatabase
        db.delete(TABLE_NAME_REM, null, null)

        val cv = ContentValues()

        for(i in 0 until data.size) {

            if ((i+1) == id){
                cv.put(COL_MESSAGE, message)
                cv.put(COL_IMAGEID, id_img)
                cv.put(COL_REMINDER_TIME, reminder_time)
            }
            else{
                cv.put(COL_MESSAGE, data[i].message)
                cv.put(COL_IMAGEID, data[i].img_id)
                cv.put(COL_REMINDER_TIME, data[i].reminder_time)
            }
            cv.put(COL_LOCATION_X, data[i].location_x)
            cv.put(COL_LOCATION_Y, data[i].location_y)
            cv.put(COL_CREATION_TIME, data[i].creation_time)
            cv.put(COL_CREATOR_ID, data[i].creator_id)
            cv.put(COL_REMINDER_SEEN, data[i].reminder_seen)
            db.insert(TABLE_NAME_REM, null, cv)
        }
        db.close()
    }

    // Used for getting reminder information by its index, returns string of image/message/date info
    fun getReminder(id: Int) : MutableList<String> {

        val list : MutableList<String> = ArrayList()
        val data = this.readDataReminders()

        for (i in 0 until data.size){
            if (i == (id-1)){
                list.add(data[i].img_id.toString())
                list.add(data[i].message)
                list.add(data[i].reminder_time)
                break
            }
        }
        return list
    }

    // Used for getting reminder id by index & position
    fun getReminderId(userId: Int, position: Int) : Int {

        val data = this.readDataReminders()
        var id : Int = 0
        var pos = position

        for (i in 0 until data.size){
            if (userId == (data[i].creator_id)){
                if (data[i].reminder_seen == 0){
                    continue
                }
                if (id == pos) {
                    id = i + 1
                    break
                }
                else{
                    id++
                }
            }
        }
        return id
    }

    // Deleting reminder from database by index
    fun deleteReminder(id: Int){

        val data = this.readDataReminders()
        val db = this.writableDatabase
        db.delete(TABLE_NAME_REM, null, null)

        val cv = ContentValues()

        for(i in 0 until data.size) {
            if((i+1) == id){
                continue
            }
            else {
                cv.put(COL_MESSAGE, data[i].message)
                cv.put(COL_IMAGEID, data[i].img_id)
                cv.put(COL_LOCATION_X, data[i].location_x)
                cv.put(COL_LOCATION_Y, data[i].location_y)
                cv.put(COL_REMINDER_TIME, data[i].reminder_time)
                cv.put(COL_CREATION_TIME, data[i].creation_time)
                cv.put(COL_CREATOR_ID, data[i].creator_id)
                cv.put(COL_REMINDER_SEEN, data[i].reminder_seen)
                db.insert(TABLE_NAME_REM, null, cv)
            }
        }
        db.close()
    }

    // This function is called when notification appears for a specific reminder,
    // reminder_seen is set to 1 in order to make reminder visible in a listview
    fun makeReminderVisible(message: String, date: String, imgId: Int, userId: Int) {

        val data = this.readDataReminders()
        val db = this.writableDatabase
        db.delete(TABLE_NAME_REM, null, null)
        val cv = ContentValues()

        for (i in 0 until data.size) {

            if (data[i].message == message && data[i].reminder_time == date &&
                    data[i].img_id == imgId && data[i].creator_id == userId) {
                cv.put(COL_REMINDER_SEEN, 1)
            }
            else {
                cv.put(COL_REMINDER_SEEN, data[i].reminder_seen)
            }
            cv.put(COL_MESSAGE, data[i].message)
            cv.put(COL_IMAGEID, data[i].img_id)
            cv.put(COL_REMINDER_TIME, data[i].reminder_time)
            cv.put(COL_LOCATION_X, data[i].location_x)
            cv.put(COL_LOCATION_Y, data[i].location_y)
            cv.put(COL_CREATION_TIME, data[i].creation_time)
            cv.put(COL_CREATOR_ID, data[i].creator_id)
            db.insert(TABLE_NAME_REM, null, cv)
        }
    }

    // Calculates the distance between user and reminder,
    // returns true if the reminder is close enough to the user (less than 1km) (or if there is no location requirement, 0.0)
    fun reminderInRange(userId: Int, reminderId: Int) : Boolean {

        val reminderData = this.readDataReminders()
        val userData = this.readData()

        val loc1 = Location("0")
        loc1.latitude = userData[userId].latitude
        loc1.longitude = userData[userId].longitude

        val loc2 = Location("1")
        loc2.latitude = reminderData[reminderId].location_x
        loc2.longitude = reminderData[reminderId].location_y


        val distanceInMeters: Float = loc1.distanceTo(loc2)
        println("ABABABABBABBABABABABBA: $distanceInMeters")
        if (loc2.latitude == 0.0 && loc2.longitude == 0.0){
            return true
        }
        if (distanceInMeters <= 1000){
            return true
        }
        return false
    }

    fun getReminderId2(message: String, date : String,  imgId : Int, userId: Int) : Int {

        var id : Int = 0
        val data = this.readDataReminders()
        for (i in 0 until data.size) {

            if (data[i].message == message && data[i].reminder_time == date &&
                    data[i].img_id == imgId && data[i].creator_id == userId) {
                id = i
            }
        }
        return id
    }




}