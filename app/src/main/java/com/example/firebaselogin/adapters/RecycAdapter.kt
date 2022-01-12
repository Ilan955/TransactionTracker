package com.example.firebaselogin.adapters

import android.content.Context
import android.graphics.drawable.Animatable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaselogin.IncomeClass
import com.example.firebaselogin.R

class recycAdapter(var items: ArrayList<IncomeClass>) :
    RecyclerView.Adapter<recycAdapter.transViewHolder>() {

    lateinit var translateAnimation: Animation
    lateinit var ctx: Context

    inner class transViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var dateOfTrans = itemView.findViewById(R.id.DateTV) as TextView
        var nameOfTrans = itemView.findViewById(R.id.DescTV) as TextView
        var amountOfTrans = itemView.findViewById(R.id.amountTV) as TextView
        var imageTrans = itemView.findViewById(R.id.imageTrans) as ImageView
        var mainCard = itemView.findViewById(R.id.mainView) as CardView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): transViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.trans, parent, false)
        ctx = parent.context
        val inflater = LayoutInflater.from(ctx)
        val contactView = inflater.inflate(R.layout.trans, parent, false)

        return transViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: transViewHolder, position: Int) {

        // get the value that is in the position
        var l = items.get(position)
        var dateOfTra = holder.dateOfTrans
        var nameOfTra = holder.nameOfTrans
        var amountOfTra = holder.amountOfTrans
        var imgTran = holder.imageTrans
        // if the value of the income is bigger then one- add Plus icon near
        if (l.incomeAcmount!!.toInt() > 0)
            imgTran.setImageResource(R.drawable.plus_new)
        // else add a minus icon near
        else
            imgTran.setImageResource(R.drawable.new_minux)


        dateOfTra.setText(l.dateOfIncome)
        nameOfTra.setText(l.incomeDesc)
        amountOfTra.setText(l.incomeAcmount)

        // add animation when sliding down the recycle view item
        if(position%2==0)
         translateAnimation = AnimationUtils.loadAnimation(ctx, R.anim.slide_animation)
        else
            translateAnimation = AnimationUtils.loadAnimation(ctx, R.anim.slide_in)

        holder.mainCard.animation = translateAnimation


    }

    override fun getItemCount(): Int {
        return items.size
    }

}