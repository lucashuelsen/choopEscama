package br.com.choopescama.util

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter


@InverseBindingAdapter(attribute = "app:isLoading")
fun getIsLoading(view: ProgressView): Boolean {
    return view.isLoading
}

@BindingAdapter("app:isLoading")
fun setIsLoading(view: ProgressView, isLoading: Boolean?) {
    view.isLoading = isLoading ?: false
}