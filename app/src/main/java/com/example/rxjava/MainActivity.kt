package com.example.rxjava

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.annotation.CheckResult
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.rxrelay3.PublishRelay
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.annotations.SchedulerSupport
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.ofType
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers


class MainActivity : AppCompatActivity() {

    private val greeting = "Hello From RxJava."
    private lateinit var myObservable: Observable<String>
    private var textView: TextView? = null
    private var testBtn:Button?=null
    private val TAG = MainActivity::class.simpleName

    private val testPublishRelay = PublishRelay.create<Unit>()
    private val _testPublishRelay = testPublishRelay.asObservable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.tvGreeting)
        testBtn = findViewById(R.id.test_btn)
        myObservable = Observable.just(greeting)
        testBtn?.setOnClickListener {
            testPublishRelay.accept(Unit)
        }
        Test01()
        Test02()
        Test03()
        Test04()
        Test05()
    }


    private fun Test01() {

        val tempObserver: DisposableObserver<Int> = object : DisposableObserver<Int>() {

            override fun onNext(t: Int?) {
                Log.v(TAG, "onNext() - " + t + " " + Thread.currentThread().name);
            }

            override fun onError(e: Throwable?) {
                TODO("Not yet implemented")
            }

            override fun onComplete() {
                Log.v(TAG, "onComplete() " + Thread.currentThread().name);
            }
        }

        val observable = Observable.create(object : ObservableOnSubscribe<Int> {
            override fun subscribe(emitter: ObservableEmitter<Int>?) {
                // 해당 작업은 subscribeOn(AndroidSchedulers.mainThread()) 로 인하여 main 쓰레드로 동작함
                for (i in arrayOf(1, 2, 3, 4, 5)) {
                    Log.v(TAG, "[MAIN_THREAD] " + Thread.currentThread().name + " : onNext " + i)
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
                    "[IO_THREAD] " + Thread.currentThread().name + " : onNext " + i
                )
            }
            .observeOn(Schedulers.computation()) // 아래 작업은 observeOn(Schedulers.computation()) 로인해 RxComputationThreadPool 쓰레드로 동작함
            .doOnNext { i: Int ->
                Log.v(
                    TAG,
                    "[COMPUTATION_THREAD] " + Thread.currentThread().name + " : onNext " + i
                )
            }
            .observeOn(Schedulers.newThread()) // 아래 작업은 observeOn(Schedulers.newThread()) 로인해 RxNewThreadScheduler 쓰레드로 동작함
            .subscribe(tempObserver)
    }


    private fun Test02() {

        val intentFilter = ObservableTransformer<Int, Int> { it ->
            it.subscribeOn(Schedulers.computation()).map { it * it }
                .observeOn(Schedulers.io()) // subscribe는 io쓰레드에서.계산은 computation에서
        }

        Observable.range(1, 10)
            .compose(intentFilter)
            .map {
                "square: $it"
            }
            .blockingSubscribe { println(it) }
    }


    private fun Test03() {
        var count: Int = 0
        val observable = Observable.range(0, 4)
            .timestamp()
            .map {
                println("연산")
                String.format("[%d] %d", it.value(), it.time())
            }
            .doOnNext {
                count++
            }
            .publish()

        observable.subscribe {
            println("subscriber1: $it")
        }

        observable.subscribe {
            println("subscriber2: $it")
        }

        // 총 4번방출하는데 야들이 받을뿐..

        observable.connect()
        println("연산횟수: $count")
    }

    private fun Test04() {
        val compositeDisposable = CompositeDisposable()
        val intentS = PublishRelay.create<HomeViewIntent>()

        val intentFilter = ObservableTransformer<HomeViewIntent, HomeViewIntent> {
            it.publish { shared ->
                Observable.mergeArray(
                    shared.ofType<HomeViewIntent.Initial>().take(1), // 최초 한개의 값만 가져옴. 3이라면 3개
                    shared.filter { it !is HomeViewIntent.Initial }
                )
            }
        }

        val mergedIntents = Observable.mergeArray(
            Observable.just(HomeViewIntent.Initial),
            Observable.just(HomeViewIntent.Refresh),
            Observable.just(HomeViewIntent.LoadNextPageUpdatedComic),
            Observable.just(HomeViewIntent.RetryNewest),
            Observable.just(HomeViewIntent.RetryMostViewed),
            Observable.just(HomeViewIntent.RetryUpdate)
        ) // 6개의 Observable이 있고 이것들이 모두 방출되어야 함.

        mergedIntents.subscribe(intentS::accept)

        val mixedSource = Observable.just<HomeViewIntent>(
            HomeViewIntent.Initial,
            HomeViewIntent.LoadNextPageUpdatedComic
        )
            .doOnSubscribe { s: Disposable? -> Log.d("MIXED_SOURCE", "***Subscribed") }

        mixedSource.publish { f: Observable<HomeViewIntent> ->
            Observable.mergeArray(
                f.ofType(HomeViewIntent.LoadNextPageUpdatedComic::class.java)
                    .compose { g: Observable<HomeViewIntent.LoadNextPageUpdatedComic> -> g.map { v: HomeViewIntent.LoadNextPageUpdatedComic -> v::class.simpleName } },
                f.ofType(HomeViewIntent.Refresh::class.java)
                    .compose { g: Observable<HomeViewIntent.Refresh> -> g.map { v: HomeViewIntent.Refresh -> v::class.simpleName } },
                f.ofType(HomeViewIntent.Initial::class.java)
                    .compose { g: Observable<HomeViewIntent.Initial> ->
                        g.map { v: HomeViewIntent.Initial ->
                            Log.d("MIXED_SOURCE", "Initial111111111111")
                            v::class.simpleName
                        }
                    },
                f.ofType(HomeViewIntent.RetryNewest::class.java)
                    .compose { g: Observable<HomeViewIntent.RetryNewest> -> g.map { v: HomeViewIntent.RetryNewest -> v::class.simpleName } },
                f.ofType(HomeViewIntent.Initial::class.java)
                    .compose { g: Observable<HomeViewIntent.Initial> ->
                        g.map { v: HomeViewIntent.Initial ->
                            Log.d("MIXED_SOURCE", "Initial222222222222")
                            v::class.simpleName
                        }
                    }
            )
        }.subscribe {
            Log.d("MIXED_SOURCE", it.toString())
        }
    }


    private fun Test05() {
        val observableLists = Observable.just<HomeViewIntent>(
            HomeViewIntent.Initial,
            HomeViewIntent.LoadNextPageUpdatedComic,
            HomeViewIntent.Refresh,
            HomeViewIntent.RetryMostViewed,
            HomeViewIntent.RetryNewest
        ).doOnSubscribe { s: Disposable? -> Log.d("TEST5", "*** TEST5 Subscribed ***") }

        val publishRelay = PublishRelay.create<HomeViewIntent>()
        observableLists.subscribe(publishRelay::accept)

        val intentFilter = ObservableTransformer<HomeViewIntent, HomeViewIntent> {
            it.publish {
                Observable.mergeArray(
                    it.ofType<HomeViewIntent.Initial>().take(1),
                    it.filter { it !is HomeViewIntent.Initial }
                )
            }
        }

        publishRelay.compose(intentFilter).doOnNext { Log.d("TEST5", "intent=$it") }

    }

    companion object {
        @CheckResult
        @SchedulerSupport(SchedulerSupport.NONE)
        inline fun <reified U : Any, T : Any> Observable<T>.notOfType() = filter { it !is U }!!

    }

}
