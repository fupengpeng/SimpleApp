package com.fpp.simpleapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fpp.simpleapp.utils.LogUtil;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        init();

//        initTwo();

//        initThree();

//        initFour();

//        initFive();

        initSix();

        initSeven();


    }

    private void initSeven() {
        // 如果我们想要订阅者只能收到大于5的数据
        // filter 是用于过滤数据的，返回false表示拦截此数据。

        Flowable.fromArray(1, 20, 5, 0, -1, 8)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer.intValue() > 5;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println(integer);
                    }
                });

        //  如果我们只想要2个数据:
        //  take 用于指定订阅者最多收到多少数据。
        Flowable.fromArray(1, 2, 3, 4)
                .take(2)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println(integer);
                    }
                });

        //  如果我们想在订阅者接收到数据前干点事情，比如记录日志:
        //  doOnNext 允许我们在每次输出一个元素之前做一些额外的事情。

        Flowable.just(1, 2, 3)
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("保存:" + integer);
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println(integer);
                    }
                });




    }

    private void initSix() {


        //  假设我的 Flowable 发射的是一个列表，接收者要把列表内容依次输出
        List<Integer> list = new ArrayList<>();
        list.add(10);
        list.add(1);
        list.add(5);

        Flowable.just(list)
                .subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(List<Integer> list) throws Exception {
                        for (Integer integer : list)
                            System.out.println(integer);
                    }
                });


        //  RxJava 2.0 提供了fromIterable方法，可以接收一个 Iterable 容器作为输入，每次发射一个元素。

        List<Integer> lists = new ArrayList<>();
        lists.add(10);
        lists.add(1);
        lists.add(5);

        Flowable.fromIterable(lists)
                .subscribe(num ->
                        System.out.println(num)
                );



        //  虽然去掉了 for 循环，但是代码依然看起来很乱。嵌套了两层，它会破坏某些我们现在还没有讲到的RxJava的特性。
        List<Integer> listss = new ArrayList<>();
        listss.add(10);
        listss.add(1);
        listss.add(5);

        Flowable.just(listss)
                .subscribe(nums -> {
                    Observable.fromIterable(nums)
                            .subscribe(num -> System.out.println(num));
                });


        /*

        它就是 flatMap()。
        Flowable.flatMap 可以把一个 Flowable 转换成另一个 Flowable :

        和 map 不同之处在于 flatMap 返回的是一个 Flowable 对象。
        这正是我们想要的，我们可以把从List发射出来的一个一个的元素发射出去。

         */
        List<Integer> listsss = new ArrayList<>();
        listsss.add(10);
        listsss.add(1);
        listsss.add(5);

        Flowable.just(listsss)
                .flatMap(new Function<List<Integer>, Publisher<Integer>>() {
                    @Override
                    public Publisher<Integer> apply(List<Integer> integers) throws Exception {
                        return Flowable.fromIterable(integers);
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println(integer);
                    }
                });



    }

    private void initFive() {


        Flowable.just("map1")
                .map(new Function<String, Integer>() {
                    @Override
                    public Integer apply(String s) throws Exception {
                        LogUtil.e(" apply == " + s.hashCode());
                        return s.hashCode();
                    }
                })
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        LogUtil.e("apply  =-= " + integer.toString());
                        return integer.toString();
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        LogUtil.e("accept = " + s);
                        System.out.println(s);
                    }
                });


    }

    private void initFour() {

        Flowable.just("map")
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        return s + " -====ittianyu";
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.println(s);
                    }
                });



    }

    private void initThree() {
        Flowable.just("hello RxJava 2 -ittianyu")
                .subscribe(s -> System.out.println(s));


        Flowable.just("hello RxJava 2")
                .subscribe(s -> System.out.println(s + " -----ittianyu"));


    }

    private void initTwo() {

        Flowable<String> flowable = Flowable.just("hello RxJava 2");

        Consumer consumer = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println(s);
            }
        };

        flowable.subscribe(consumer);




        Flowable.just("hello ").subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                LogUtil.e(s);
            }
        });




    }

    private void init() {


        /*
        Publisher可以发出一系列的事件，而Subscriber负责和处理这些事件。
        平常用得最多的Publisher是Flowable

         */

        // create a flowable
        Flowable<String> flowable = Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> e) throws Exception {
                LogUtil.e("subscribe");
                // 发送事件
                e.onNext("hello RxJava 2");
                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER);

        // create
        Subscriber subscriber = new Subscriber<String>() {
            @Override
            public void onSubscribe(Subscription s) {
                LogUtil.e("onSubscribe   " + s);
                System.out.println("onSubscribe");
                // 请求资源
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(String s) {
                LogUtil.e("s = " + s);
                // 接收事件
                System.out.println(s);
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {
                LogUtil.e("onComplete");
                // 接收结束事件
                System.out.println("onComplete");
            }
        };


        flowable.subscribe(subscriber);

    }
}
