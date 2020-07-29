package cn.huangchengxi.imomoe.activities.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import cn.huangchengxi.imomoe.R
import com.bumptech.glide.Glide

class RecommendedPagerAdapter(private val items:List<RecommendedPageItem>):PagerAdapter() {
    private val views=ArrayList<View>()

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view=LayoutInflater.from(container.context).inflate(R.layout.view_recommended_pager_item,container,false)
        Glide.with(container.context).load(items[position]).into(view.findViewById(R.id.cover_img))
        views.add(position,view)
        return items[position]
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(views[position])
    }
}