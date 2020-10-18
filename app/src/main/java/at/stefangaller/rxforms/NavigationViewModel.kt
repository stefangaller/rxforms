package at.stefangaller.rxforms

import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.subjects.PublishSubject

open class NavigationViewModel : ViewModel() {
    private val directionInternal: PublishSubject<Int> = PublishSubject.create()
    val directions: Flowable<Int> = directionInternal.distinctUntilChanged().toFlowable(BackpressureStrategy.LATEST)

    protected fun navigateTo(direction: NavDirections) = directionInternal.onNext(direction.actionId)
}