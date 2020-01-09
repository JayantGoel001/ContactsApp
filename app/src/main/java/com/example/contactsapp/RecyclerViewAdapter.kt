package com.example.contactsapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView

class RecyclerViewAdapter(val context:Context,val list:ArrayList<Contacts>):RecyclerView.Adapter<Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.usercontact,parent,false)
        return Holder(view)
    }

    override fun getItemCount(): Int =list.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.contactsName.text=list[position].name
        holder.phoneNo.text=list[position].phoneNo
        holder.contactID.text=list[position].contactID
        holder.email.text=list[position].contactEmail
        var image:Bitmap
        if(list[position].contactPhoto != "" && list[position].contactPhoto!=null)
        {
            image=BitmapFactory.decodeFile(list[position].contactPhoto)
            if(image!=null)
            {
                holder.imageView.setImageBitmap(image)
            }
            else
            {
                image=BitmapFactory.decodeResource(context.resources,R.drawable.avatar)
                holder.imageView.setImageBitmap(image)
            }
        }

//        holder.itemView.setOnTouchListener { v, event ->
//
//        }
    }
}
class Holder(itemView: View):RecyclerView.ViewHolder(itemView)
{
    val phoneNo=itemView.findViewById<TextView>(R.id.phone_number)
    val contactsName=itemView.findViewById<TextView>(R.id.name)
    val contactID=itemView.findViewById<TextView>(R.id.contactID)
    val imageView=itemView.findViewById<CircleImageView>(R.id.image)
    val email=itemView.findViewById<TextView>(R.id.emailID)
}