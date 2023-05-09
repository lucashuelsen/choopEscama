package br.com.choopescama.util

interface IDataLoader<T> {
    fun onPreLoad()

    /**
     * Method to handle the data to be loaded
     */
    suspend fun load(): T

    fun onFinishLoad(result: T)
}