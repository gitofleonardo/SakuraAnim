package cn.huangchengxi.imomoe.activities.detail.fragments

import android.content.Context
import android.os.Bundle
import android.text.SpannableStringBuilder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.huangchengxi.imomoe.R
import cn.huangchengxi.imomoe.activities.detail.EpisodeSource
import com.google.android.material.transition.Hold

class VideoInfoFragment(private val parentActivity: ParentActivity) : Fragment() {
    private var episodeRV:RecyclerView?=null
    private val episodes=ArrayList<EpisodeSource>()
    private val adapter by lazy { EpisodeAdapter(requireContext(),episodes) }
    private val lm by lazy { LinearLayoutManager(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_vedio_info, container, false)
        episodeRV=view.findViewById(R.id.play_source_rv)
        episodeRV!!.adapter=adapter
        episodeRV!!.layoutManager=lm
        adapter.setOnClickListener {
            val item=episodes[it]
            parentActivity.onSwitchVideo(item.url)
        }
        return view
    }
    fun updateEpisode(items:List<EpisodeSource>){
        episodes.clear()
        episodes.addAll(items)
        adapter.notifyDataSetChanged()
    }
    private class EpisodeAdapter(private val context:Context,private val items:List<EpisodeSource>):RecyclerView.Adapter<Holder>(){
        private var onClickListener:((Int)->Unit)?=null
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            val view=LayoutInflater.from(parent.context).inflate(R.layout.view_episode_item,parent,false)
            return Holder(view)
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            val item=items[position]
            holder.name.text=SpannableStringBuilder(item.title)
            holder.container.setOnClickListener {
                onClickListener?.invoke(position)
            }
        }
        fun setOnClickListener(listener:((Int)->Unit)){
            onClickListener=listener
        }
    }
    private class Holder(view:View):RecyclerView.ViewHolder(view){
        val name:TextView=view.findViewById(R.id.episode_name)
        val container:LinearLayout=view.findViewById(R.id.episode_container)
    }
    interface ParentActivity{
        fun onSwitchVideo(url:String)
    }
}