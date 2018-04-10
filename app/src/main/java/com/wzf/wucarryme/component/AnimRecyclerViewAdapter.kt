package com.wzf.wucarryme.component

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.wzf.wucarryme.R

/**
 * Thanks for drakeet/Meizhi
 *
 *
 * https://github.com/drakeet/Meizhi/blob/master/app%2Fsrc%2Fmain%2Fjava%2Fme%2Fdrakeet%2Fmeizhi%2Fui%2Fadapter%2FAnimRecyclerViewAdapter.java
 */
abstract class AnimRecyclerViewAdapter<T : RecyclerView.ViewHolder> : RecyclerView.Adapter<T>() {

    private var mLastPosition = -1

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): T {
//        return null
//    }

    override fun onBindViewHolder(holder: T, position: Int) {}

    override fun getItemCount(): Int {
        return 0
    }

    fun showItemAnim(view: View, position: Int) {
        val context = view.context
        if (position > mLastPosition) {
            view.alpha = 0f
            view.postDelayed({
                val animation = AnimationUtils.loadAnimation(context,
                    R.anim.slide_in_right)
                animation.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) {
                        view.alpha = 1f
                    }

                    override fun onAnimationEnd(animation: Animation) {}

                    override fun onAnimationRepeat(animation: Animation) {}
                })
                view.startAnimation(animation)
            }, (DELAY * position).toLong())
            mLastPosition = position
        }
    }

    companion object {

        private const val DELAY = 138
    }
}