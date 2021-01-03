package com.example.rxjava

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private val greeting = "Hello From RxJava."
    private lateinit var observableGreeting: Observable<String>

    private var textView: TextView?=null

    private var greetingObserver: Observer<String> = object:Observer<String>{
        override fun onSubscribe(d: Disposable?) {
            // observer를 listen하면 호출.
            Log.d("DEBUG","onSubscribe()")
        }

        override fun onNext(t: String?) {
            // 값을 전달할 떄 호출하여 값을 넘겨줌.
            Log.d("DEBUG","onNext()")
            textView?.text = t
        }

        override fun onError(e: Throwable?) {
            Log.d("DEBUG","onError()")
        }

        override fun onComplete() {
            // 가지고 있는 값을 모두 전달하면 호출함.
            Log.d("DEBUG","onComplete()")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.tvGreeting)
        observableGreeting = Observable.just("방출!!")
        observableGreeting.subscribeOn(Schedulers.io())
        observableGreeting.observeOn(AndroidSchedulers.mainThread())
    }
}