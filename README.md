
# RxJava & RxAndroid & RxKotlin
* 사용자는 클릭 이벤트, 네트워크 호출, 데이터 스토리지, 변수 변경 및 오류까지도 및 모든 항목에서 데이터 스트림을 생성할 수 있다.
* 모듈은 자체 스레드에서 실행되며, 동시에 여러 코드 블록을 실행한다.
* 그렇다면 반응형 프로그래밍이란 무엇일까?
* Consumer Code Block은 데이터가 들어올 때 데이터에 반응한다.
* ReactiveX란 무엇일까? ReactiveX란 다양한 프로그래밍 언어에 대한 Reactive Programming의 개념을 구현하는 프로젝트이다. 
* ReactiveX란 reactive extenstions를 나타내는 짧은 단어이다.
* ReactivceX는 Observer Pattern, Iterator Pattern, Function Programming의 최상의 아이디어를 조합한 것이다.
* RxAndroid는 android specific support을 제공하는 RxJava의 맨 위에있는 계층일 뿐이다.
* 예를 들어, Main Thread 또는 주어진 다른 Thread에 대해 Scheduler를 제공한다.
* RxAndroid는 RxJava를 대체하지는 않는다. 앱에 두 가지를 모두 사용해야한다.
* 이것은 코딩을 더욱 쉽게 만들기 위해 전문가 개발자들에 의해 만들어진 일련의 클래스, 인터페이스, 그리고 방법론들이다.
* 코딩 문제를 신속하게 해결할 수 있는 방법, 훨씬 더 나은 Thread처리 방법, 더 깨끗하고 유지 관리 가능하며, 코드를 이해하기 쉬운 방법으로 제공한다. 
* RxJava와 RxAndroid는 Dagger, Retrofit, Picasso와 같은 또 다른 2개의 라이브러리일 뿐이다.
* RxJava를 이해한다면 RxKotlin에 적용하기는 쉽다. 왜냐하면 Rx의 개념은 변경되지 않기 때문이다.
#
## RxJava & RxAndroid Quick OverView
* Observables은 우리가 데이터를 얻을 수 있는 곳이다.
* Observables를 Observe하기 위해 Observer를 갖고 있다. (관찰할 수 있는 것을 관찰하기 위해 관찰자가 있다.)
* Observable -> Instances of Observable Class, They emit data (관찰 가능한 클래스의 인스턴스에서 데이터를 내보낸다.)
* Observer -> Instances of Observer Interface, They consume data emited by the observable( observable에서 emit한 데이터를 cosume 한다. )
* 때로는 데이터 스트림을 Observables로 직접 가져올 수 있는데 예를 들어 Retrofit을 사용하는 경우 Observable로 작성할 수 있다.
* 또는 기존의 코드를 관찰할 수 있도록 작성해야하는 경우도 있다. 
* 하나의 Observable에는 여러개의 Observer가 있을 수 있다.
* 데이터에 대해 등록된 Observable가 Observer가 하나라도 있을 경우 데이터를 emit한다. 없을경우 emit하지 않는다.
* Observer는 3가지의 주요 메소드를 갖고 있다. onNext(), onCompleted(), onError()
* Observable이 데이터를 emit할 때 onNext()를 가장먼저 호출 한다.
* Observable에서 에러가 발생하면 Observer의 onError() 메소드를 호출한다. 
* RxJava는 모든 Block을 제공한다,
* Observable의 Emit이 모두 완료된 이후에 Observer의 onCompleted() 메소드를 호출한다.
* Observables와 Observer 사이에는 멀티 스레딩을 잘 처리하기 위한 Scheduler가 있다.
* Scheduler는 기본적으로 특정 코드가 실행되는 스레드를 백그라운드 스레드에서 결정하거나 Main Thread에서 결정한다.  
* 사용할 수 있는 Scheduler가 많아도, io와 main이 대부분이다.
* 마지막으로 Operator가 있다. Operator는 observer에 의해서 stream을 받기도 전해 변환할 수 있으며, 여러 Operator들을 체인으로 연결할 수 있다.
* 원하는 특정 방식으로 Observables에 속한 data stream를 변환할 수 있다. 
* Map, FlatMap, SwitchMap 등이 있다.
* 우리는 Normal Observable 이나 Flowable(유동적인) 사이에 같은 Observer가 있다는 것을 알아차릴 것이다. 
* Disposable 과 CompositeDisposable 이 있다. 이는 memory leaks을 피할 수 있게 한다. 
#
## Concurrency and Multi-threading with Schedulers.
* RxJava의 가장 큰 장점 중 하나는 다양한 스레드에서 작업 및 처리 결과를 쉽게 Schedule 할 수 있다는 것이다.
* 이를 통해 시스템 성능을 최적화하고, 버그를 방지할 수 있다.
* RxJava의 Scheduler란 Scheduler의 도움으로 우리는 MultiThreading을 처리한다.
* Scheduler는 하나 이상의 thread를 관리하는 thread pool로 인식될 수 있다. 
* Scheduler는 작업을 실행해야 할 때마다, 풀에서 스레드를 가져와 해당 스레드에서 task를 실행한다.
* RxJava에는 다양한 유형의 Scheduler가 있다.
* Schedulers.io()는 무제한의 스레드 풀을 가질 수 있다. CPU사용량이 많지 않은 작업에 사용된다.
* 예를 들어 네트워크 통신 및 파일 시스템과의 상호 작용을 수행하는 데이터베이스 상호 작용과 같다.
* AndroidSchedulers.mainThread() 는 UI Thread이다.
* MainThread()에서 사용자 상호 작용이 발생하며, 이 스케쥴러는 RxJava와 함꼐 제공되지 않지만, RxAndroid에서 RxJava로 제공된다.
* 나머지 다른 스케쥴러가 있는데, Schedulers.newThread() 이 스케쥴러는 예약된 각 작업 단위마다 새 스레드를 만든다.
* Schedulers.single() 이 스케쥴러에는, 지정된 순서에 따라 태스크를 차례로 실행하는 단일 스레드가 있다.
* Schedulers.trampoline() 이 스케쥴러는 첫번쨰 in,out의 기본 사항에 따라 작업을 실행한다. recurring tasks를 구현할때 사용한다.
* Schedulers.from(Executor executor)는 지정된 실행자가 지원하는 사용자 지정 스케쥴러를 만들고 반환한다.(This creates and returns a custom scheduler backed by the specified executor)
* RxJava는 두 가지 방법을 사용하여 원하는 스레드에서 작업을 예약할 수 있는 간단한 방법을 제공한다. 
* Schedulers.io()가 실행되는 시점부터 데이터 데이터 스트림이 io스레드에서 실행된다. 즉 관찰자는 io스레드를 통해 데이터를 수신한다.
* AndroidSchedulers.mainThread() 에서 다시 데이터 스트림이 main 스레드에서 실행된다. (View와 같은 UI작업에 사용할 수 있다. )

#
## Disposables.
* 사용자가 Retrofit을 사용하고 있고, 네트워크 호출이 완료되기 전에 취소한다면? 
* Activity 또는 Fragment가 삭제된다. 그러나 Observer Subscription은 남아있을 것이다.
* Observer가 User Interface를 업데이트하려고 할 때, 이미 view가 파괴더있기때문에 memory leak을 일으켜 앱이 중지되거나 충돌한다. 
* 이러한 상황을 방지하기 위해 Observer가 우리는 Observer가 더이상 Observerable을 Observer하지 않는 경우 Disposable을 사용하여 dispose(삭제)할 수 있다. 

#
## DisposableObservers.
* DisposableObserver 클래스는 Observer와 Disposable interface를 모두 구현한다.
* 2개이상의 Observer가 있는 activity나 fragment에서 훨씬 효율적이다.

#
## CompositeDisposable (복합삭제)
* 만약에 2개이상의 Observer가 있는 경우, 아래와 같이 모두 dispose 해야할 것이다.
```
@Override 
protected void onDestroy(){
    super.onDestroy()
    ob1.dispose()
    ob2.dispose()
}
```
* 이것은 많은 Observer를 가지고 있을 경우 좋은 방법이 아니다.
* 따라서 1개보다 많은 Observer를 가지고 있을 경우 CompositeDisposable을 사용한다.
* CompositeDisposable은 pool의 subsription 목록을 유지할 수 있으며, 한번에 모두 삭제할 수 있다. (compositeDisposable.dispose(), clear())
* clear()와 dispose()의 차이점은 dispose()를 사용하게되면 comositeDisposable을 다시 사용할 수 없지만, clear()는 다시 사용할 수 있다. 
#
## Summary
* RxJava란 ? 
``` 
RxJava is the JVM implementation of Reactive Extensions.
```
* "Reactive Extensions"는 무엇을 의미합니까?
```
Reactive Extensions is a library for composing asynchronous(different parts of the program run at the same time) 
and event-based(executes the code based on the events generated by other parts of the application) 
programs by using observable data streams
```
* Observer와 Observerable의 차이점은?
```
Observable emits data,Observer gets data.
```
* Observables의 타입 종류는 ?
```
Flowable, Observable, Single, and Completable 
```
* RxJava에서 Operator를 사용하는 이유는 무엇입니까?
```
To modify data.
```
* RxJava의 Schedulers는 무엇입니까?
```
Schedulers are where(thread) the work should be done.
```
* RxJav의 Subscriber은 무엇을 의미합니까?
```
Where the response will be sent after work has been completed.
```





