/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.ogaclejapan.rx.binding;

import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action1;

public class RxObject<T> implements Rx<T> {

    private T target;

    protected RxObject(T target) {
        this.target = target;
    }

    public static <T> RxObject<T> of(T target) {
        return new RxObject<T>(target);
    }

    @Override
    public final T get() {
        return target;
    }

    @Override
    public <E> Subscription bind(final Observable<E> observable,
            final Action<? super T, ? super E> action) {
        return observable
                .observeOn(observeOn())
                .subscribe(onBind(action), ERROR_ACTION_EMPTY, COMPLETE_ACTION_EMPTY);
    }

    @Override
    public final <E> Subscription bind(final RxObservable<E> observable,
            final Action<? super T, ? super E> action) {
        return bind(observable.asObservable(), action);
    }

    protected boolean isBindable() {
        return true;
    }

    protected Scheduler observeOn() {
        return MAIN_THREAD_SCHEDULER;
    }

    protected <E> Action1<E> onBind(final Action<? super T, E> action) {
        return new Action1<E>() {
            @Override
            public void call(final E e) {
                if (isBindable()) {
                    action.call(get(), e);
                }
            }
        };
    }

}
