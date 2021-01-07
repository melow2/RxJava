package com.example.rxjava

import androidx.annotation.CheckResult
import com.jakewharton.rxrelay3.Relay
import io.reactivex.rxjava3.annotations.SchedulerSupport
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.kotlin.ofType


@Suppress("nothing_to_inline")
inline fun <T> Relay<T>.asObservable(): Observable<T> = this