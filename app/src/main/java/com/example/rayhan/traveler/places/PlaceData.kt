package com.example.rayhan.traveler.places

object PlaceData {

    var placeNameArray = arrayOf(
            "Bora Bora", "Canada", "Dubai", "Hong Kong",
            "Iceland", "India", "Kenya", "London", "Switzerland", "Sydney")

    fun getPlaces(): ArrayList<Place> {
        val list = ArrayList<Place>()

        for (i in placeNameArray.indices) {
            var isFav = false
            if (i == 2 || i == 5) {
                isFav = true
            }

            val place = Place(
                    placeNameArray[i],
                    placeNameArray[i].replace("\\s+".toRegex(), "").toLowerCase(),
                    isFav)
            list.add(place)
        }

        return list
    }
}