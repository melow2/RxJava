package com.example.rxjava

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers


class MainActivity : AppCompatActivity() {

    private val greeting = "Hello From RxJava."
    private lateinit var myObservable: Observable<String>
    private var textView: TextView? = null
    private val TAG = MainActivity::class.simpleName

    private var tempObserver: DisposableObserver<Int> = object : DisposableObserver<Int>() {

        override fun onNext(t: Int?) {
            Log.v(TAG, "onNext() - "+t+" "+Thread.currentThread().name);
        }

        override fun onError(e: Throwable?) {
            TODO("Not yet implemented")
        }

        override fun onComplete() {
            Log.v(TAG, "onComplete() "+Thread.currentThread().name);
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.tvGreeting)
        myObservable = Observable.just(greeting)


        val observable = Observable.create(object : ObservableOnSubscribe<Int> {
            override fun subscribe(emitter: ObservableEmitter<Int>?) {
                // 해당 작업은 subscribeOn(AndroidSchedulers.mainThread()) 로 인하여 main 쓰레드로 동작함
                for (i in arrayOf(1, 2, 3, 4, 5)) {
                    Log.v(TAG, "[MAIN_THREAD] "+Thread.currentThread().name + " : onNext " + i)
                    emitter?.onNext(i)
                    if (i == 5) {
                        emitter?.onComplete()
                    }
                }
            }
        })

        observable
            .subscribeOn(AndroidSchedulers.mainThread()) // emit은 mainThread에서 진행 함.
            .observeOn(Schedulers.io()) // 아래 작업은 observeOn(Schedulers.io()) 로인해 RxCachedThreadScheduler 쓰레드로 동작함
            .doOnNext { i: Int ->
                Log.v(
                    TAG,
                    "[IO_THREAD] "+Thread.currentThread().name + " : onNext " + i
                )
            }
            .observeOn(Schedulers.computation()) // 아래 작업은 observeOn(Schedulers.computation()) 로인해 RxComputationThreadPool 쓰레드로 동작함
            .doOnNext { i: Int ->
                Log.v(
                    TAG,
                    "[COMPUTATION_THREAD] "+Thread.currentThread().name + " : onNext " + i
                )
            }
            .observeOn(Schedulers.newThread()) // 아래 작업은 observeOn(Schedulers.newThread()) 로인해 RxNewThreadScheduler 쓰레드로 동작함
            .subscribe(tempObserver)

    }
}