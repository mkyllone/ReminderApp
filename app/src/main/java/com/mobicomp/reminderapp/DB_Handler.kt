package com.mobicomp.reminderapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

val DATABASE_NAME = "DB"
val TABLE_NAME = "Users"
val COL_USERNAME = "username"
val COL_PASSWORD = "password"

class DB_Handler(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {

        val createTable = "CREATE TABLE TABLE_NAME ($COL_USERNAME$COL_PASSWORD )"

        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun insertData(user : User_information){
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(COL_USERNAME, user.username)
        cv.put(COL_PASSWORD, user.password)
        var result = db.insert(TABLE_NAME, null, cv)

        if (result == (-1).toLong()) {
            Toast.makeText(context, "Account creation failed", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(context, "Account created successfully", Toast.LENGTH_SHORT).show()
        }
    }

   /* fun readData() : MutableList<User_information> {
        var list: MutableList<User_information> = ArrayList()
        val db = this.readableDatabase
        val query = "Select * from " + TABLE_NAME
        val result = db.rawQuery(query, null)

        if (result.moveToFirst()) {
            do {
                var user = User_information()
                user.username = result.getString(result.getColumnIndex(COL_NAME))
                user.password = result.getString(result.getColumnIndex(COL_PASSWORD))
                list.add(user)
            } while (result.moveToNext())
        }
        result.close()
        db.close()
        return list
    }*/
}