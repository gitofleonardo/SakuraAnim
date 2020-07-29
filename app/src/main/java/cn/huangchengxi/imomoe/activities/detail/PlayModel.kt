package cn.huangchengxi.imomoe.activities.detail

import android.os.Message
import android.util.Log
import cn.huangchengxi.imomoe.activities.common.CommonHandler
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import kotlin.concurrent.thread

class PlayModel(private val playerPresenter:Player):CommonHandler.MainThread {
    private val FETCH_SUCCESS=0
    private val PARSE_SUCCESS=1
    private val PARSE_PLAYER_ADDRESS=2
    private val LOG_MAXLENGTH = 2000

    private var document:Document?=null
    private var playDocument:Document?=null

    private val handler=CommonHandler(this)

    fun parseStringHtml(html:String){
        e("html",html)
        playDocument=Jsoup.parse(html)
        val iframe=playDocument!!.getElementById("play2").attr("abs:src")
        e("iframe",playDocument.toString())
        thread(start = true) {
            val playerDoc=getHtml(iframe)
            val player=playerDoc.select("video").attr("src")
            val msg=handler.obtainMessage()
            msg.what=PARSE_PLAYER_ADDRESS
            msg.obj=player
            handler.sendMessage(msg)
        }
    }
    fun getBasicInformation(url:String){
        thread(start = true){
            document=getHtml(url)
            val msg=handler.obtainMessage()
            msg.what=FETCH_SUCCESS
            handler.sendMessage(msg)
        }
    }
    private fun getHtml(url: String):Document{
        return Jsoup.connect(url).maxBodySize(0).timeout(6000).get()
    }

    private fun getInformation():AnimInformation{
        val fire= document!!.getElementsByClass("fire")[0]
        val coverUrl=fire.child(0).child(1).attr("abs:src")
        val name=fire.child(0).child(1).attr("alt")
        val urls=document!!.getElementsByClass("movurl")
        val playSources=ArrayList<PlaySource>()
        for (url in urls){
            val episodeSources=ArrayList<EpisodeSource>()
            val lis=url.child(0).select("li")
            for (li in lis){
                val a=li.child(0)
                val nameTitle=a.attr("title")
                val address=a.attr("abs:href")
                val item=EpisodeSource(nameTitle,address)
                episodeSources.add(item)
            }
            val playerSource=PlaySource("播放地址",episodeSources)
            playSources.add(playerSource)
        }
        return AnimInformation(name,coverUrl,playSources)
    }
    interface Player{
        fun onSuccessParsing(playUrl:String)
        fun onSuccessLoadingInformation(information: AnimInformation)
        fun onErrorParsing(errorMsg:String)
        fun onNetworkError()
    }

    override fun handleMessage(message: Message) {
        when (message.what){
            FETCH_SUCCESS->{
                val info=getInformation()
                playerPresenter.onSuccessLoadingInformation(info)
            }
            PARSE_PLAYER_ADDRESS->{
                val url=message.obj as String
                playerPresenter.onSuccessParsing(url)
            }
        }
    }
    fun e(TAG: String, msg: String) {
        val strLength = msg.length
        var start = 0
        var end: Int = LOG_MAXLENGTH
        for (i in 0..99) {
            if (strLength > end) {
                Log.e(TAG + i, msg.substring(start, end))
                start = end
                end += LOG_MAXLENGTH
            } else {
                Log.e(TAG, msg.substring(start, strLength))
                break
            }
        }
    }
}