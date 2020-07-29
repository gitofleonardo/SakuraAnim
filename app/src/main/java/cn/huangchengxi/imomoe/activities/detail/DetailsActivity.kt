package cn.huangchengxi.imomoe.activities.detail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.webkit.*
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import cn.huangchengxi.imomoe.R
import cn.huangchengxi.imomoe.activities.detail.fragments.VideoInfoFragment
import java.lang.Exception

class DetailsActivity : AppCompatActivity(),PlayModel.Player,VideoInfoFragment.ParentActivity{
    private val videoInfoFragment=VideoInfoFragment(this)
    private val presenter=PlayPresenter(this)
    private var absUrl:String?=null
    private val mediaPlayer=MediaPlayer()
    private val playerSurface by lazy { findViewById<SurfaceView>(R.id.player_sv) }
    private val back by lazy { findViewById<FrameLayout>(R.id.back) }
    private val videoTitle by lazy { findViewById<TextView>(R.id.video_title) }
    private val webView by lazy { WebView(this) }
    private val webClient=object : WebViewClient(){
        override fun onPageFinished(view: WebView?, url: String?) {
            webView.stopLoading()
            webView.pauseTimers()
            webView.loadUrl("javascript:HTMLOUT.processHTML(document.getElementsByTagName('html'));")        }

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            return true
        }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldInterceptRequest(
            view: WebView?,
            request: WebResourceRequest?
        ): WebResourceResponse? {
            val header=request?.requestHeaders.toString()
            //Log.e("headers",header)
            return super.shouldInterceptRequest(view, request)
        }
    }

    private var pausePosition=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        getAbsUrlFromIntent()
        init()
    }
    @SuppressLint("SetJavaScriptEnabled", "AddJavascriptInterface")
    private fun init(){
        back.setOnClickListener {
            onBackPressed()
        }
        if (absUrl==null){
            Toast.makeText(this,"Url错误",Toast.LENGTH_SHORT).show()
        }else{
            presenter.getInformation(absUrl!!)
        }
        //pushFragment(videoInfoFragment)
        supportFragmentManager.beginTransaction()
            .add(R.id.detail_fragment,videoInfoFragment)
            .commitAllowingStateLoss()

        webView.settings.javaScriptEnabled=true
        webView.addJavascriptInterface(JSInterface(),"HTMLOUT")
        webView.webViewClient=webClient
    }
    private fun pushFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .add(R.id.detail_fragment,fragment)
            .addToBackStack("details")
            .commitAllowingStateLoss()
    }
    private fun getAbsUrlFromIntent(){
        absUrl=intent.getStringExtra("url")
    }
    companion object{
        fun startDetailsActivity(context: Context,url:String){
            val intent=Intent(context,DetailsActivity::class.java)
            intent.putExtra("url",url)
            context.startActivity(intent)
        }
    }

    override fun onSuccessParsing(playUrl: String) {
        Log.e("playUrl",playUrl)
        Toast.makeText(this,playUrl,Toast.LENGTH_SHORT).show()
        try {
            mediaPlayer.setDataSource(playUrl)
            val holder=playerSurface.holder
            holder.addCallback(object : SurfaceHolder.Callback{
                override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {

                }
                override fun surfaceDestroyed(p0: SurfaceHolder) {
                    if (mediaPlayer.isPlaying){
                        mediaPlayer.stop()
                    }
                }
                override fun surfaceCreated(p0: SurfaceHolder) {
                    mediaPlayer.prepareAsync()
                    mediaPlayer.setDisplay(p0)
                    mediaPlayer.setOnPreparedListener {
                        it.start()
                    }
                }
            })
        }catch (e:Exception){
            Toast.makeText(this,"Video Error",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSuccessLoadingInformation(information: AnimInformation) {
        videoTitle.text=SpannableStringBuilder(information.name)
        val episodes=ArrayList<EpisodeSource>()
        for (s in information.playSources){
            episodes.addAll(s.source)
        }
        videoInfoFragment.updateEpisode(episodes)
    }

    override fun onErrorParsing(errorMsg: String) {

    }

    override fun onNetworkError() {

    }

    override fun onResume() {
        super.onResume()
        if (mediaPlayer.isPlaying){
            mediaPlayer.seekTo(pausePosition)
        }
    }

    override fun onPause() {
        super.onPause()
        pausePosition=mediaPlayer.currentPosition
    }

    override fun onSwitchVideo(url: String) {
        //presenter.getPlayAddress(url)
        webView.loadUrl(url)
    }
    private inner class JSInterface{
        @JavascriptInterface
        fun processHTML(html:String){
            Log.e("html",html)
            presenter.getPlayAddress(html)
        }
    }
}