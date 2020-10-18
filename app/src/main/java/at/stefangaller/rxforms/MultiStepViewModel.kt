package at.stefangaller.rxforms

import androidx.lifecycle.toLiveData
import at.stefangaller.rxforms.util.addTo
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit


class MultiStepViewModel : NavigationViewModel() {

    private val disposables = CompositeDisposable()

    private val usernameInternal = BehaviorSubject.createDefault<CharSequence>("")
    private val passwordInternal = BehaviorSubject.createDefault<CharSequence>("")
    private val firstFragmentValid = Observable.combineLatest(usernameInternal, passwordInternal)
    { username, password ->
        username.isNotBlank() && password.isNotBlank()
    }

    private val secondFragmentValid = BehaviorSubject.createDefault(true)

    private val errorInternal = PublishSubject.create<String>()


    val error = errorInternal.toFlowable(BackpressureStrategy.LATEST).toLiveData()

    fun registerFirstFragmentInput(
        clicks: Observable<Unit>,
        username: Observable<CharSequence>,
        password: Observable<CharSequence>
    ) {
        registerFirstFragmentClicks(clicks)
        username.subscribe(usernameInternal)
        password.subscribe(passwordInternal)
    }

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

    private fun registerFirstFragmentClicks(clicks: Observable<Unit>) =
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
}