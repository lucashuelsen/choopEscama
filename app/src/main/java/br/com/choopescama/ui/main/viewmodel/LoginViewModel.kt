package br.com.choopescama.ui.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.choopescama.util.QueueMutableLiveDataLoader
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthActionCodeException
import java.util.Locale

class LoginViewModel(val listener: OnCompleteListener<AuthResult>?) : ViewModel() {
    private var auth: FirebaseAuth? = null
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val exception = MutableLiveData<String>()
    val dataLoader = QueueMutableLiveDataLoader()


    init {
        auth = FirebaseAuth.getInstance()
    }

    fun onClickRegister() {
        dataLoader.setIsLoading(true)

        if (listener != null) {
            auth?.createUserWithEmailAndPassword(
                email.value ?: " ",
                password.value ?: " "
            )?.addOnCompleteListener(listener)
        }
    }

    fun onClickcancel(){
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