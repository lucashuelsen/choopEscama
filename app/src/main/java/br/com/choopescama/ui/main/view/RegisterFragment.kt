package br.com.choopescama.ui.main.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import br.com.choopescama.R
import br.com.choopescama.databinding.ActivityRegisterBinding
import br.com.choopescama.ui.main.viewmodel.RegisterViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult


class RegisterFragment : AppCompatActivity() {
    private var mBinding : ActivityRegisterBinding? = null
    private var mViewModel: RegisterViewModel? = null
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
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        mBinding?.lifecycleOwner = this
        mBinding?.viewModel = mViewModel
    }

    private fun setupViewModel(){
        mViewModel = ViewModelProvider(
            this,
            RegisterViewModel.LoginViewModelFactory(listener)
            ).get(RegisterViewModel::class.java)
    }
}