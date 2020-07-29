package cn.huangchengxi.imomoe.activities.main

import android.os.Message
import android.util.Log
import cn.huangchengxi.imomoe.activities.common.CommonHandler
import cn.huangchengxi.imomoe.activities.common.imomoeUrl
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.File
import java.lang.ref.WeakReference
import kotlin.concurrent.thread

class MainModel(iPresenter: IPresenter):CommonHandler.MainThread {
    private val FETCH_SUCCESS=0
    private var page:Document?=null

    private val p=WeakReference<IPresenter>(iPresenter)
    private val handler=CommonHandler(this)

    fun getAll(){
        thread(start = true) {
            page=getHtml()
            val msg=handler.obtainMessage()
            msg.what=FETCH_SUCCESS
            handler.sendMessage(msg)
        }
    }
    private fun getPageItems(html: Document,index:Int):List<PageItem>{
        val list=ArrayList<PageItem>()
        val res=html.getElementsByClass("imgs")[index]
        val lis=res.select("li")
        for (li in lis){
            val a=li.child(0)
            val viewUrl=a.attr("href")
            val coverUrl=a.child(0).attr("abs:src")
            val title=li.child(1).select("a").attr("title")
            val more=li.child(2).text()
            val item=PageItem(viewUrl,coverUrl,title,more)
            list.add(item)
        }
        return list
    }
    private fun getRecommended(html:Document):List<RecommendedPageItem>{
        val res=html.getElementsByClass("heros")
        val list=ArrayList<RecommendedPageItem>()
        for (hero in res){
            val lis=hero.select("li")
            for (li in lis){
                val a=li.child(0)
                val viewUrl=li.child(0).attr("href")
                val coverUrl=a.child(0).attr("abs:src")
                val title=a.child(1).text()
                val description=a.child(1).child(0).text()
                val item=RecommendedPageItem(viewUrl,coverUrl,title,description)
                list.add(item)
            }
        }
        return list
    }
    private fun getHtml():Document{
        return Jsoup.connect(imomoeUrl).get()
        //val file=File("/home/huangchengxi/Documents/imomoe.html")
        //return Jsoup.parse(file,"UTF-8")
    }
    interface IPresenter{
        fun onRecommendedLoaded(recommendedItems:List<RecommendedPageItem>)
        fun onRecentlyUpdatedLoaded(recentItems:List<PageItem>)
        fun onJpAnimLoaded(jpItems:List<PageItem>)
        fun onDomesticLoaded(dItems:List<PageItem>)
        fun onUsLoaded(usItems:List<PageItem>)
        fun onAnimMovieLoaded(movieItems:List<PageItem>)
        fun onError(errorMsg:String)
    }

    override fun handleMessage(message: Message) {
        when (message.what){
            FETCH_SUCCESS->{
                val recommendedItems=getRecommended(page!!)
                val recentUpdates=getPageItems(page!!,0)
                val jpAnim=getPageItems(page!!,1)
                val doAnim=getPageItems(page!!,2)
                val usAnim=getPageItems(page!!,3)
                val movieItems=getPageItems(page!!,4)
                p.get()?.onRecommendedLoaded(recommendedItems)
                p.get()?.onRecentlyUpdatedLoaded(recentUpdates)
                p.get()?.onJpAnimLoaded(jpAnim)
                p.get()?.onDomesticLoaded(doAnim)
                p.get()?.onUsLoaded(usAnim)
                p.get()?.onAnimMovieLoaded(movieItems)
            }
        }
    }
}