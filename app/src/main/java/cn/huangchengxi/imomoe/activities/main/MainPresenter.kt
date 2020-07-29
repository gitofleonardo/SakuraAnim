package cn.huangchengxi.imomoe.activities.main

import java.lang.ref.WeakReference

class MainPresenter(private val mainView:MainModel.IPresenter):MainModel.IPresenter {
    private val model=MainModel(this)
    private val view=WeakReference<MainModel.IPresenter>(mainView)

    fun refreshHomePage(){
        model.getAll()
    }

    override fun onRecommendedLoaded(recommendedItems: List<RecommendedPageItem>) {
        view.get()?.onRecommendedLoaded(recommendedItems)
    }

    override fun onRecentlyUpdatedLoaded(recentItems: List<PageItem>) {
        view.get()?.onRecentlyUpdatedLoaded(recentItems)
    }

    override fun onJpAnimLoaded(jpItems: List<PageItem>) {
        view.get()?.onJpAnimLoaded(jpItems)
    }

    override fun onDomesticLoaded(dItems: List<PageItem>) {
        view.get()?.onDomesticLoaded(dItems)
    }

    override fun onUsLoaded(usItems: List<PageItem>) {
        view.get()?.onUsLoaded(usItems)
    }

    override fun onAnimMovieLoaded(movieItems: List<PageItem>) {
        view.get()?.onAnimMovieLoaded(movieItems)
    }

    override fun onError(errorMsg: String) {
        view.get()?.onError(errorMsg)
    }
}