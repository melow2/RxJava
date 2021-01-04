package com.example.rxjava

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*


class MainActivity : AppCompatActivity() {

    private val greeting = "Hello From RxJava."
    private lateinit var observableGreeting: Observable<String>

    private var textView: TextView?=null

    private var greetingObserver: Observer<String> = object:Observer<String>{
        override fun onSubscribe(d: Disposable?) {
            // observer를 listen하면 호출.
            Log.d("DEBUG", "onSubscribe()")
        }

        override fun onNext(t: String?) {
            // 값을 전달할 떄 호출하여 값을 넘겨줌.
            Log.d("DEBUG", "onNext()")
            textView?.text = t
        }

        override fun onError(e: Throwable?) {
            Log.d("DEBUG", "onError()")
        }

        override fun onComplete() {
            // 가지고 있는 값을 모두 전달하면 호출함.
            Log.d("DEBUG", "onComplete()")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.tvGreeting)
        val tmpStr: String = Arrays.stream(arrayOfNulls<String>(10000000)).map { x -> "*" }.collect(Collectors.joining())
        val foo: Flowable<*> = Flowable.range(0, 1000000000)
                .map { x: Int ->
                    println("[very fast sender] i'm fast. very fast.")
                    println(String.format("sending id: %s %d%50.50s", Thread.currentThread().name, x, tmpStr))
                    x.toString() + tmpStr
                }

        foo.observeOn(Schedulers.computation()).subscribe { x: Any? ->
            Thread.sleep(1000)
            println("[very busy receiver] i'm busy. very busy.")
            println(String.format("receiving id: %s %50.50s", Thread.currentThread().name, x))
        }

        while (true) {
            Thread.sleep(1000)
        }
    }
}