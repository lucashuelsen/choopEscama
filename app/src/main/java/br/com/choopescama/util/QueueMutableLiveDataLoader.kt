package br.com.choopescama.util

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import br.com.choopescama.BR
import kotlinx.coroutines.*
import java.util.concurrent.CopyOnWriteArrayList

class QueueMutableLiveDataLoader : BaseObservable() {

    // ---------------------------------------------------------------------------------------------
    // region Instance attributes
    // ---------------------------------------------------------------------------------------------

    /**
     * Indicates if the loader is being executed or not
     */
    private val isLoading = ObservableField(false)
    private val mDataLoaders: MutableList<IDataLoader<*>> = CopyOnWriteArrayList()
    private val asyncJobs: MutableList<Job> = CopyOnWriteArrayList()
    private val loaderLock = Any()
    private val coroutineRunLock = Any()
    private val parentJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    //endregion

    // ---------------------------------------------------------------------------------------------
    // region Public methods
    // ---------------------------------------------------------------------------------------------

    fun load(dataLoader: IDataLoader<out Any?>) {
        load(null, dataLoader, Dispatchers.Default)
    }

    fun <T> load(
        mutableLiveData: MutableLiveData<in T>?,
        dataLoader: IDataLoader<T>?
    ) {
        load(mutableLiveData, dataLoader, Dispatchers.Default)
    }

    fun <T> load(
        mutableLiveData: MutableLiveData<in T>?,
        dataLoader: IDataLoader<T>?,
        dispatcher: CoroutineDispatcher
    ) {
        if(dataLoader != null) {
            addDataLoaderToQueue(dataLoader)
            executeItem(dispatcher, dataLoader, mutableLiveData)
        }
    }

    fun <T> load(mutableLiveData: MutableLiveData<T>?, iLoader: () -> T) {
        load(mutableLiveData, object: IDataLoader<T> {
            override fun onPreLoad() {}
            override suspend fun load(): T {
                return iLoader.invoke()
            }
            override fun onFinishLoad(result: T) {}
        })
    }

    private fun <T> executeItem(
        dispatcher: CoroutineDispatcher,
        dataLoader: IDataLoader<T>,
        mutableLiveData: MutableLiveData<in T>?
    ) {
        synchronized(coroutineRunLock) {
            val lastOrNull = asyncJobs.lastOrNull()
            val launch: Job = this.coroutineScope.launch(dispatcher) {
                //Calls onPreLoad before anything else
                withContext(Dispatchers.Main) {
                    dataLoader.onPreLoad()
                }

                //Wait for the last job to finish to execute this job
                if (lastOrNull != this) {
                    lastOrNull?.join()
                }

                withContext(Dispatchers.Default) {
                    //Load on background
                    val loadValue = dataLoader.load()

                    withContext(Dispatchers.Main) {
                        // only the main thread can use the "set value" of the LiveData
                        mutableLiveData?.value = loadValue
                        dataLoader.onFinishLoad(loadValue)
                    }
                }
            }

            asyncJobs.add(launch)
            launch.invokeOnCompletion {
                asyncJobs.remove(launch)
                removeDataLoaderFromQueue(dataLoader)
            }
        }
    }

    private fun <T> removeDataLoaderFromQueue(dataLoader: IDataLoader<T>) {
        synchronized(loaderLock) {
            mDataLoaders.remove(dataLoader)
            updateIsLoadingStatus()
        }
    }

    private fun <T> addDataLoaderToQueue(dataLoader: IDataLoader<T>) {
        synchronized(loaderLock) {
            mDataLoaders.add(dataLoader)
            updateIsLoadingStatus()
        }
    }

    private fun updateIsLoadingStatus() {
        synchronized(loaderLock) {
            setIsLoading(mDataLoaders.size > 0)
        }
    }

    fun getDataLoaders(): MutableList<IDataLoader<*>> {
        return mDataLoaders
    }

    @Bindable
    fun getIsLoading(): ObservableField<Boolean> {
        return isLoading
    }

    @Synchronized
    fun setIsLoading(isLoading: Boolean): QueueMutableLiveDataLoader? {
        this.isLoading.set(isLoading)
        notifyPropertyChanged(BR.isLoading)
        return this
    }

    //endregion
}