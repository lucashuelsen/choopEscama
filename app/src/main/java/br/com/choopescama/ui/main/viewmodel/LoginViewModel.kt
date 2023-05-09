package br.com.choopescama.ui.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel(val listener: OnCompleteListener<AuthResult>?) : ViewModel() {
    private var auth: FirebaseAuth? = null
    var email = MutableLiveData<String>()
    var password = MutableLiveData<String>()

    init {
        auth = FirebaseAuth.getInstance()
    }

    fun onClickButton() {
        if (listener != null) {
            auth?.createUserWithEmailAndPassword(
                email.value ?: "",
                password.value ?: ""
            )?.addOnCompleteListener(listener)
        }
    }

    class LoginViewModelFactory(val listener: OnCompleteListener<AuthResult>?) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            //Local variables
            val viewModel = when (modelClass) {
                LoginViewModel::class.java -> {
                    LoginViewModel(listener) as T
                }
                else -> super.create(modelClass)
            }

            return viewModel
        }
    }
}