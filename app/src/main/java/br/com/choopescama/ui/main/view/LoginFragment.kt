package br.com.choopescama.ui.main.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.choopescama.databinding.ActivityLoginBinding
import androidx.databinding.DataBindingUtil
import br.com.choopescama.R
import br.com.choopescama.ui.main.viewmodel.LoginViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult


class LoginFragment : AppCompatActivity() {
    private var mBinding : ActivityLoginBinding? = null
    private var mViewModel: LoginViewModel? = null
    var listener : OnCompleteListener<AuthResult>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupSuccessListener()
        setupViewModel()
        setupBinding()
    }

    private fun setupSuccessListener() {
        listener = OnCompleteListener<AuthResult> {
            mViewModel?.dataLoader?.setIsLoading(false)
            mViewModel?.exception?.value = it.exception?.message

            if(it.isSuccessful){
                Toast.makeText(this, "Conta criada com sucesso!",Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupBinding(){
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        mBinding?.lifecycleOwner = this
        mBinding?.viewModel = mViewModel
    }

    private fun setupViewModel(){
        mViewModel = ViewModelProvider(
            this,
            LoginViewModel.LoginViewModelFactory(listener)
            ).get(LoginViewModel::class.java)
    }
}