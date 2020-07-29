package cn.huangchengxi.imomoe.activities.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import cn.huangchengxi.imomoe.R
import cn.huangchengxi.imomoe.activities.detail.DetailsActivity

class MainActivity : AppCompatActivity(),MainModel.IPresenter {
    private val presenter=MainPresenter(this)
    private val recomItems=ArrayList<RecommendedPageItem>()
    private val recentItems=ArrayList<PageItem>()
    private val jpItems=ArrayList<PageItem>()
    private val doItems=ArrayList<PageItem>()
    private val usItems=ArrayList<PageItem>()
    private val movieItems=ArrayList<PageItem>()

    private val homeSrl by lazy { findViewById<SwipeRefreshLayout>(R.id.home_srl) }

    private val recommendedPager by lazy { findViewById<ViewPager2>(R.id.home_recommended_pager) }
    private val recommendedAdapter=RecommendedPagerAdapter(this,recomItems)

    private val recentUpdateRV by lazy { findViewById<RecyclerView>(R.id.recent_recycler) }
    private val recentAdapter=PageListAdapter(this,recentItems)
    private val recentLayoutManager=GridLayoutManager(this,2)
    private val jpRV by lazy { findViewById<RecyclerView>(R.id.jp_recycler) }
    private val jpAdapter=PageListAdapter(this,jpItems)
    private val jpLM=GridLayoutManager(this,2)
    private val doRV by lazy { findViewById<RecyclerView>(R.id.do_recycler) }
    private val doAdapter=PageListAdapter(this,doItems)
    private val doLM=GridLayoutManager(this,2)
    private val usRV by lazy { findViewById<RecyclerView>(R.id.us_recycler) }
    private val usAdapter=PageListAdapter(this,usItems)
    private val usLM=GridLayoutManager(this,2)
    private val movieRV by lazy { findViewById<RecyclerView>(R.id.movie_recycler) }
    private val movieAdapter=PageListAdapter(this,movieItems)
    private val movieLM=GridLayoutManager(this,2)

    private val searchBar by lazy { findViewById<FrameLayout>(R.id.search_edit_text) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }
    private fun init(){
        homeSrl.setOnRefreshListener {
            presenter.refreshHomePage()
        }
        recommendedPager.adapter=recommendedAdapter
        recommendedAdapter.setOnClickListener { _, i ->
            val item=recomItems[i]
            DetailsActivity.startDetailsActivity(this,item.viewUrl)
        }
        recentUpdateRV.layoutManager=recentLayoutManager
        recentUpdateRV.adapter=recentAdapter
        jpRV.layoutManager=jpLM
        jpRV.adapter=jpAdapter
        doRV.layoutManager=doLM
        doRV.adapter=doAdapter
        usRV.layoutManager=usLM
        usRV.adapter=usAdapter
        movieRV.layoutManager=movieLM
        movieRV.adapter=movieAdapter

        presenter.refreshHomePage()

        searchBar.setOnClickListener {

        }
    }
    override fun onRecommendedLoaded(recommendedItems: List<RecommendedPageItem>) {
        recomItems.clear()
        recomItems.addAll(recommendedItems)
        recommendedAdapter.notifyDataSetChanged()

        homeSrl.isRefreshing=false
    }

    override fun onRecentlyUpdatedLoaded(recentItems: List<PageItem>) {
        this.recentItems.clear()
        this.recentItems.addAll(recentItems)
        recentAdapter.notifyDataSetChanged()
    }

    override fun onJpAnimLoaded(jpItems: List<PageItem>) {
        this.jpItems.clear()
        this.jpItems.addAll(jpItems)
        jpAdapter.notifyDataSetChanged()
    }

    override fun onDomesticLoaded(dItems: List<PageItem>) {
        this.doItems.clear()
        this.doItems.addAll(dItems)
        doAdapter.notifyDataSetChanged()
    }

    override fun onUsLoaded(usItems: List<PageItem>) {
        this.usItems.clear()
        this.usItems.addAll(usItems)
        usAdapter.notifyDataSetChanged()
    }

    override fun onAnimMovieLoaded(movieItems: List<PageItem>) {
        this.movieItems.clear()
        this.movieItems.addAll(movieItems)
        movieAdapter.notifyDataSetChanged()
    }

    override fun onError(errorMsg: String) {
        Toast.makeText(this,errorMsg,Toast.LENGTH_SHORT).show()
        homeSrl.isRefreshing=false
    }
}