package com.mobicomp.reminderapp


class Reminder{

    // Variables
    var id : Int = 0
    var img_id : Int = 0
    var message : String = ""
    var location_x : Double = 0.0
    var location_y : Double = 0.0
    var reminder_time : String = ""
    var creation_time : String = ""
    var creator_id : Int = 0
    var reminder_seen : Int = 0

    // Constructors
    constructor(message : String, img_id : Int, location_x : Double, location_y : Double,
                reminder_time : String, creation_time : String, creator_id : Int,
                reminder_seen : Int) {
        this.message = message
        this.location_x = location_x
        this.location_y = location_y
        this.reminder_time = reminder_time
        this.creation_time = creation_time
        this.creator_id = creator_id
        this.reminder_seen = reminder_seen
        this.img_id = img_id
    }
    constructor(){
    }

    constructor(img_id : Int, message : String, reminder_time : String){

        this.img_id = img_id
        this.message = message
        this.reminder_time = reminder_time
    }

}