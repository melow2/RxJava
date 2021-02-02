package com.example.rxjava

import androidx.annotation.CheckResult
import com.jakewharton.rxrelay3.Relay
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Observable


@Suppress("nothing_to_inline")
inline fun <T> Relay<T>.asObservable(): Observable<T> = this

/**
 * 1) <T:Any, R:Any> 타입이 어느것이든 올 수 있음.
 * 2) toFlowable은 BackPressure에서 publish/subscribe 모델에서 consumer가 처리하는 속도가 따라가지 못한다면 busy watiing또는 out of memory가 걸릴 수 있는데,
 * 이 때 흐름제어를 하기 위한것이 BackpressureStrategy이다. // https://tourspace.tistory.com/283
 * 3) DROP은 버퍼링시 Observable을 버리는 것이며,
 * 4) MISSING 무조건 push하는 것이다.
 *
 * @author 권혁신
 * @version 1.0.0
 * @since 2021-02-02 오전 10:07
 **/

@CheckResult
inline fun <T : Any, R : Any> Observable<T>.exhaustMap(crossinline transform: (T) -> Observable<R>): Observable<R> {
    return toFlowable(BackpressureStrategy.DROP)
        .flatMap({ transform(it).toFlowable(BackpressureStrategy.MISSING) }, 1)
        .toObservable()
}