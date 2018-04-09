package com.wzf.wucarryme.common.utils

import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.android.FragmentEvent
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.trello.rxlifecycle2.components.support.RxFragment
import io.reactivex.FlowableTransformer
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object RxUtil {

    private fun <T> schedulerTransformer(scheduler: Scheduler): ObservableTransformer<T, T> {
        return ObservableTransformer { observable ->
            observable
                    .subscribeOn(scheduler)
                    .observeOn(AndroidSchedulers.mainThread(), true)
        }
    }

    fun <T> io(): ObservableTransformer<T, T> {
        return schedulerTransformer(Schedulers.io())
    }

    private fun <T> schedulerTransformerF(scheduler: Scheduler): FlowableTransformer<T, T> {
        return FlowableTransformer { flowable ->
            flowable
                    .subscribeOn(scheduler)
                    .observeOn(AndroidSchedulers.mainThread(), true)
        }
    }

    fun <T> ioF(): FlowableTransformer<T, T> {
        return schedulerTransformerF(Schedulers.io())
    }

    fun <T> activityLifecycle(activity: RxAppCompatActivity): ObservableTransformer<T, T> {
        return ObservableTransformer { observable ->
            observable.compose(activity.bindUntilEvent<T>(ActivityEvent.DESTROY))
        }
    }

    fun <T> fragmentLifecycle(fragment: RxFragment): ObservableTransformer<T, T> {
        return ObservableTransformer { observable ->
            observable.compose(fragment.bindUntilEvent<T>(FragmentEvent.DESTROY))
        }
    }

    fun <T> activityLifecycleF(activity: RxAppCompatActivity): FlowableTransformer<T, T> {
        return FlowableTransformer { flowable ->
            flowable.compose(activity.bindUntilEvent<T>(ActivityEvent.DESTROY))
        }
    }

    fun <T> fragmentLifecycleF(fragment: RxFragment): FlowableTransformer<T, T> {
        return FlowableTransformer { flowable ->
            flowable.compose(fragment.bindUntilEvent<T>(FragmentEvent.DESTROY))
        }
    }

}
