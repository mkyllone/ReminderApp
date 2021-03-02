package com.mobicomp.reminderapp

class User{

    // Variables
    var id : Int = 0
    var username : String = ""
    var password : String = ""
    var latitude : Double = 0.0
    var longitude : Double = 0.0


    // Constructors
    constructor(username : String, password : String) {
        this.username = username
        this.password = password
    }
    constructor(){
    }
    constructor(latitude : Double, longitude : Double) {
        this.latitude = latitude
        this.longitude = latitude
    }

}