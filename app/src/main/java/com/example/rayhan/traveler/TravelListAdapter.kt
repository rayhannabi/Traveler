package com.example.rayhan.traveler

import android.content.Context
import android.graphics.BitmapFactory
import android.support.v4.content.ContextCompat
import android.support.v7.graphics.Palette
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.example.rayhan.traveler.places.PlaceData
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_places.view.*

class TravelListAdapter(private var context: Context): RecyclerView.Adapter<TravelListAdapter
.ViewHolder>() {

    private lateinit var onItemClickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.row_places, parent, false)
        return ViewHolder(item)
    }

    override fun getItemCount(): Int = PlaceData.getPlaces().size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = PlaceData.getPlaces()[position]
        holder.itemView.placeName.text = place.name
        Picasso.get().load(place.getImageResourceId(context)).into(holder.itemView.placeImage)

        val image = BitmapFactory.decodeResource(context.resources, place.getImageResourceId(context))
        Palette.from(image).generate { palette ->
            val backgroundColor = palette.getMutedColor(ContextCompat.getColor(context,
                    android.R
                    .color.black))
            holder.itemView.placeNameHolder.setBackgroundColor(backgroundColor)
            holder.itemView.placeNameHolder.alpha = 0.95f
        }
    }

    fun setOnClickListener(itemClickListener: OnItemClickListener) {
        this.onItemClickListener = itemClickListener
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView.placeHolder.setOnClickListener(this)
        }

        override fun onClick(view: View) = onItemClickListener.onItemClick(itemView, adapterPosition)
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }
}