package cn.huangchengxi.imomoe.activities.common

import android.os.Handler
import android.os.Message
import java.lang.ref.WeakReference

class CommonHandler(mainThread:MainThread): Handler() {
    private val thread=WeakReference<MainThread>(mainThread)

    override fun handleMessage(msg: Message) {
        thread.get()?.handleMessage(msg)
    }
    interface MainThread{
        fun handleMessage(message: Message)
    }
}