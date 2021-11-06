package com.michelle.gallenero.mobileappexercise.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.michelle.gallenero.mobileappexercise.R
import com.squareup.picasso.Picasso

class PhotosViewAdapter(val photoList: MutableList<String> = mutableListOf()) : RecyclerView.Adapter<PhotosViewAdapter.PhotoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.photo_layout,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = photoList.size

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(photoList[position])
    }

    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(photoUrl: String) {
            val imageView = itemView.findViewById(R.id.imageView) as ImageView?
            imageView?.let {
                // Load the images into the image view set
                Picasso.get().load(photoUrl).into(it)
            }
        }
    }
}