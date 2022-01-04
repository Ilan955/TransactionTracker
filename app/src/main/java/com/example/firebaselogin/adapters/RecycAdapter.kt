package com.example.firebaselogin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaselogin.IncomeClass
import com.example.firebaselogin.R

class recycAdapter(var items: ArrayList<IncomeClass>) :
    RecyclerView.Adapter<recycAdapter.transViewHolder>() {

    inner class transViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var dateOfTrans = itemView.findViewById(R.id.DateTV) as TextView
        var nameOfTrans = itemView.findViewById(R.id.DescTV) as TextView
        var amountOfTrans = itemView.findViewById(R.id.amountTV) as TextView
        var imageTrans = itemView.findViewById(R.id.imageTrans) as ImageView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): transViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.trans, parent, false)
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.trans, parent, false)

        return transViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: transViewHolder, position: Int) {

        var l = items.get(position)
        var dateOfTra = holder.dateOfTrans
        var nameOfTra = holder.nameOfTrans
        var amountOfTra = holder.amountOfTrans
        var imgTran=holder.imageTrans

        if(l.incomeAcmount!!.toInt()>0)
            imgTran.setImageResource(R.drawable.plus_new)

        else
            imgTran.setImageResource(R.drawable.new_minux)


        dateOfTra.setText(l.dateOfIncome)
        nameOfTra.setText(l.incomeDesc)
        amountOfTra.setText(l.incomeAcmount)

    }

    override fun getItemCount(): Int {
        return items.size
    }

}