package cn.huangchengxi.imomoe.activities.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import cn.huangchengxi.imomoe.R

class MainActivity : AppCompatActivity(),MainModel.IPresenter {
    private val presenter=MainPresenter(this)
    private val recomItems=ArrayList<RecommendedPageItem>()

    private val recommendedPager by lazy { findViewById<ViewPager>(R.id.home_recommended_pager) }
    private val recommendedPagerAdapter=RecommendedPagerAdapter(recomItems)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }
    private fun init(){
        recommendedPager.adapter=recommendedPagerAdapter
        presenter.refreshHomePage()
    }
    override fun onRecommendedLoaded(recommendedItems: List<RecommendedPageItem>) {
        recomItems.clear()
        recomItems.addAll(recommendedItems)
        recommendedPagerAdapter.notifyDataSetChanged()
        Toast.makeText(this,"Load success",Toast.LENGTH_SHORT).show()
        Log.e("success",recommendedItems.toString())
    }

    override fun onRecentlyUpdatedLoaded(recentItems: List<PageItem>) {
    }

    override fun onJpAnimLoaded(jpItems: List<PageItem>) {
    }

    override fun onDomesticLoaded(dItems: List<PageItem>) {
    }

    override fun onUsLoaded(usItems: List<PageItem>) {
    }

    override fun onAnimMovieLoaded(movieItems: List<PageItem>) {
    }

    override fun onError(errorMsg: String) {
    }
}