package com.example.contactsapp

import android.graphics.Bitmap

data class Contacts(val name:String,val phoneNo:String,val contactID:String,val contactEmail:String,val contactPhoto:String?)
{
    constructor():this("","","","", "")
}