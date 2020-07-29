package cn.huangchengxi.imomoe.activities.main

import android.content.Context
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.huangchengxi.imomoe.R
import com.bumptech.glide.Glide

class PageListAdapter(private val context: Context, private val items:List<PageItem>):RecyclerView.Adapter<PageListAdapter.Holder>() {
    private var onClickListener:((Int)->Unit)?=null

    class Holder(view:View):RecyclerView.ViewHolder(view){
        val coverView:ImageView by lazy { view.findViewById<ImageView>(R.id.home_cover_img) }
        val title:TextView by lazy { view.findViewById<TextView>(R.id.anim_title) }
        val description:TextView by lazy { view.findViewById<TextView>(R.id.description) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.view_main_item,parent,false)
        return Holder(
            view
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item=items[position]
        Glide.with(context).load(item.coverUrl).into(holder.coverView)
        holder.title.text=SpannableStringBuilder(item.name)
        holder.description.text=SpannableStringBuilder(item.description)
        holder.coverView.setOnClickListener {
            onClickListener?.invoke(position)
        }
    }
    fun setOnClickListener(listener:(Int)->Unit){
        this.onClickListener=listener
    }
}