package at.stefangaller.rxforms

import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import at.stefangaller.rxforms.util.addTo
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit


class MultiStepViewModel : NavigationViewModel() {

    private val disposables = CompositeDisposable()

    private val firstFragmentValid = BehaviorSubject.createDefault(true)

    private val secondFragmentValid = BehaviorSubject.createDefault(true)

    private val errorInternal = PublishSubject.create<String>()

    fun registerFirstFragmentClicks(clicks: Observable<Unit>) =
        clicks.debounce(200, TimeUnit.MILLISECONDS)
            .withLatestFrom(firstFragmentValid) { _, valid ->
                if (valid) {
                    navigateTo(FirstStepFragmentDirections.toSecondFragment())
                } else {
                    errorInternal.onNext("Invalid Form 1")
                }
            }
            .subscribe()
            .addTo(disposables)

    fun registerSecondFragmentClicks(clicks: Observable<Unit>) =
        clicks.debounce(200, TimeUnit.MILLISECONDS)
            .withLatestFrom(secondFragmentValid) { _, valid ->
                if (valid) {
                    navigateTo(SecondStepFragmentDirections.toSummary())
                } else {
                    errorInternal.onNext("Invalid Form 2")
                }
            }
            .subscribe()
            .addTo(disposables)

    fun registerSummaryFragmentButtonClicks(clicks: Observable<Unit>) =
        clicks.debounce(200, TimeUnit.MILLISECONDS).subscribe {
            navigateTo(SummaryFragmentDirections.restart())
        }.addTo(disposables)


    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}