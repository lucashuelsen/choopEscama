package br.com.choopescama.util

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import br.com.choopescama.BR
import java.lang.ref.WeakReference
import java.util.*
class QueueDataLoaderDependency : BaseObservable() {

    // ---------------------------------------------------------------------------------------------
    // region Instance attributes
    // ---------------------------------------------------------------------------------------------

    /**
     * Indicates if the loader is being executed or not
     */
    private val _isLoading = ObservableField(false)
    private val mDataLoaders = ArrayList<WeakReference<QueueMutableLiveDataLoader>>()

    val dataLoaders: List<WeakReference<QueueMutableLiveDataLoader>>
        get() = mDataLoaders

    //endregion

    // ---------------------------------------------------------------------------------------------
    // region Public methods
    // ---------------------------------------------------------------------------------------------

    fun addDependency(dataLoader: QueueMutableLiveDataLoader) {
        mDataLoaders.add(WeakReference(dataLoader))
        refreshLoadingState()
        dataLoader.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable, propertyId: Int) {
                if (propertyId == BR.isLoading) {
                    refreshLoadingState()
                }
            }
        })
    }

    /**
     * Adds all dependencies from the given [QueueDataLoaderDependency].
     * @param loaderDependency The [QueueDataLoaderDependency] to retrieve the dependencies.
     */
    fun addDependencies(loaderDependency: QueueDataLoaderDependency) {
        //Local variables
        val dataLoaders: List<WeakReference<QueueMutableLiveDataLoader>>?

        //Prepare
        dataLoaders = loaderDependency.dataLoaders

        if (dataLoaders != null) {
            for (dataLoader in dataLoaders) {
                val queueMutableLiveDataLoader = dataLoader.get()
                if (queueMutableLiveDataLoader != null) {
                    this.addDependency(queueMutableLiveDataLoader)
                }
            }
        }
    }

    @Bindable
    fun getIsLoading(): ObservableField<Boolean> = _isLoading

    @Synchronized
    fun setIsLoading(isLoading: Boolean): QueueDataLoaderDependency {
        this.getIsLoading().set(isLoading)
        notifyPropertyChanged(BR.isLoading)
        return this
    }

    private fun refreshLoadingState() {
        //Local variables
        val invalidLoaders = ArrayList<WeakReference<QueueMutableLiveDataLoader>>()
        var isLoading: Boolean? = false

        for (dataLoader in mDataLoaders) {

            if (dataLoader.get() != null) {
                if (dataLoader.get()?.getIsLoading()?.get() != null) {
                    isLoading = dataLoader.get()?.getIsLoading()?.get()

                    if (isLoading != null && isLoading) break
                }
            } else {
                invalidLoaders.add(dataLoader)
            }
        }

        //Clean invalid references
        mDataLoaders.removeAll(invalidLoaders)

        if (isLoading == null) {
            isLoading = false
        }

        setIsLoading(isLoading)
    }

    //endregion
}