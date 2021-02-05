package com.mobicomp.reminderapp

class User{

    // Variables
    var id : Int = 0
    var username : String = ""
    var password : String = ""

    // Constructors
    constructor(username : String, password : String) {
        this.username = username
        this.password = password
    }
    constructor(){
    }
}