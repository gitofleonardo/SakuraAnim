package cn.huangchengxi.imomoe.activities.main.views

import android.content.Context
import android.text.SpannableStringBuilder
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import cn.huangchengxi.imomoe.R

class HeaderView(context: Context,attrs:AttributeSet?,defStyle:Int):FrameLayout(context,attrs,defStyle) {
    private val showMore by lazy { findViewById<LinearLayout>(R.id.show_more) }
    private val titleText by lazy { findViewById<TextView>(R.id.header_title) }
    private var onShowMoreListener:(()->Unit)?=null

    constructor(context: Context,attrs: AttributeSet?):this(context,attrs,0)
    constructor(context: Context):this(context,null)

    init {
        View.inflate(context,R.layout.view_main_anim_bar,this)
        val arr=context.obtainStyledAttributes(attrs,R.styleable.HeaderView)
        val title=arr.getString(R.styleable.HeaderView_headerTitle)
        titleText.text=SpannableStringBuilder(title)
        arr.recycle()
    }
    fun setOnShowMoreListener(listener:()->Unit){
        onShowMoreListener=listener
        showMore.setOnClickListener {
            onShowMoreListener?.invoke()
        }
    }
}