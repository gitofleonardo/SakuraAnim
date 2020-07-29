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

class RecommendedPagerAdapter(private val context: Context,private val items:List<RecommendedPageItem>):
    RecyclerView.Adapter<RecommendedPagerAdapter.PagerHolder>() {
    private var onClickListener:((View,Int)->Unit)?=null

    class PagerHolder(view: View):RecyclerView.ViewHolder(view){
        val image: ImageView by lazy { view.findViewById<ImageView>(R.id.cover_img) }
        val text:TextView by lazy { view.findViewById<TextView>(R.id.page_title) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.view_recommended_pager_item,parent,false)
        return PagerHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: PagerHolder, position: Int) {
        val item=items[position]
        Glide.with(context).load(item.coverUrl).into(holder.image)
        holder.text.text=SpannableStringBuilder(item.viewName)
        holder.image.setOnClickListener {
            onClickListener?.invoke(it,position)
        }
    }
    fun setOnClickListener(listener:((View,Int)->Unit)){
        onClickListener=listener
    }
}