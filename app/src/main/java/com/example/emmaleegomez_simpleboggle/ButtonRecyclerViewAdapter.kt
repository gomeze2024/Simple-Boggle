package com.example.emmaleegomez_simpleboggle

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

class ButtonRecyclerViewAdapter(
    context: Context,
    private var mData: List<Char>
) : RecyclerView.Adapter<ButtonRecyclerViewAdapter.ViewHolder>() {
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private val mEnabledStates: BooleanArray = BooleanArray(mData.size) { true }
    private lateinit var mClickListener: ItemClickListener
    private var prevPosition: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = mInflater.inflate(R.layout.board_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.button.text = mData[position].toString()
        holder.button.isEnabled = mEnabledStates[position]

    }

    override fun getItemCount(): Int {
        return mData.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var button: Button = itemView.findViewById(R.id.letter)

        init {
            button.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            if (prevPosition == null || isConnected(adapterPosition, prevPosition!!)) {
                mEnabledStates[adapterPosition] = false
                mClickListener.onItemClick(view, adapterPosition)
                notifyItemChanged(adapterPosition)

                prevPosition = adapterPosition
            } else {
                Toast.makeText(view.context, "You may only select connected letters", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun isConnected(position1: Int, position2: Int): Boolean {
        val gridWidth = 4

        val row1 = position1 / gridWidth
        val col1 = position1 % gridWidth
        val row2 = position2 / gridWidth
        val col2 = position2 % gridWidth

        val dx = abs(col1 - col2)
        val dy = abs(row1 - row2)

        return (dx <= 1 && dy <= 1)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun enableAllButtons() {
        mEnabledStates.fill(true)
        prevPosition = null
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: List<Char>) {
        mData = newData
        notifyDataSetChanged()
    }

    fun getItem(id: Int): Char {
        return mData[id]
    }

    fun setClickListener(itemClickListener: ItemClickListener) {
        mClickListener = itemClickListener
    }

    interface ItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }

}
