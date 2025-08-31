package com.proj.tubialert

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FAQAdapter(private val faqList: List<FAQItem>) :
    RecyclerView.Adapter<FAQAdapter.FAQViewHolder>() {

    inner class FAQViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val question: TextView = itemView.findViewById(R.id.faqQuestion)
        val answer: TextView = itemView.findViewById(R.id.faqAnswer)
        val expandCollapseIcon: ImageView = itemView.findViewById(R.id.expandCollapseIcon)
        val divider: View = itemView.findViewById(R.id.divider)
        val questionLayout: LinearLayout = itemView.findViewById(R.id.faqQuestionLayout)

        init {
            questionLayout.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = faqList[position]
                    item.isExpanded = !item.isExpanded
                    notifyItemChanged(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FAQViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_faq, parent, false)
        return FAQViewHolder(view)
    }

    override fun onBindViewHolder(holder: FAQViewHolder, position: Int) {
        val currentItem = faqList[position]

        holder.question.text = currentItem.question
        holder.answer.text = currentItem.answer

        if (currentItem.isExpanded) {
            holder.answer.visibility = View.VISIBLE
            holder.divider.visibility = View.VISIBLE
            holder.expandCollapseIcon.setImageResource(R.drawable.ic_expand_less)
        } else {
            holder.answer.visibility = View.GONE
            holder.divider.visibility = View.GONE
            holder.expandCollapseIcon.setImageResource(R.drawable.ic_expand_more)
        }
    }

    override fun getItemCount(): Int {
        return faqList.size
    }
}