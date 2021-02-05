package com.mobicomp.reminderapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

val DATABASE_NAME = "DB"
val TABLE_NAME = "Users"
val COL_ID = "id"
val COL_USERNAME = "username"
val COL_PASSWORD = "password"

class DBHandler(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {

        val createTable = "CREATE TABLE " + TABLE_NAME + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_USERNAME + " VARCHAR(256)," + COL_PASSWORD + " INTEGER)";

        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    // Insert user data into  database
    fun insertData(user : User){
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(COL_USERNAME, user.username)
        cv.put(COL_PASSWORD, user.password)
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
                list.add(user)
            } while (result.moveToNext())
        }
        result.close()
        db.close()
        return list
    }

    // Method which returns true if username already exists in database, otherwise false
    fun userExist(nick : String) : Boolean{

        val data = this.readData()
        for (i in 0 until data.size){
            if (data[i].username == nick){
                return true
            }
        }
        return false
    }

    // Method which returns user's id if given login information exists in database, otherwise -1
    fun loginCheck(nick : String, pw : String) : Int{

        val data = this.readData()
        for (i in 0 until data.size){
            if(nick == data[i].username && pw == data[i].password){
                return i
            }
        }
        return -1
    }

    // Method used for getting username by index
    fun getUsername(id : Int) : String{

        val data = this.readData()
        return data[id].username
    }

    // Method used for getting password by index
    fun getPassword(id : Int) : String{

        val data = this.readData()
        return data[id].password
    }

    // Used for changing password or username to the database by index
    fun updateUserInformation(id : Int, username : String, password: String){

        val cv = ContentValues()
        cv.put(COL_USERNAME, username)
        cv.put(COL_PASSWORD, password)
        val whereclause = "$COL_ID=?"
        val whereargs = arrayOf((id+1).toString())
        this.writableDatabase.update(TABLE_NAME, cv, whereclause, whereargs)
    }
}