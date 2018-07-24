package com.example.rayhan.traveler.places

import android.content.Context

class Place (val name: String,
             private val imageName: String,
             val isFavourite: Boolean = false) {

    fun getImageResourceId(context: Context): Int {
        return context.resources.getIdentifier(this.imageName, "drawable", context.packageName)
    }
}