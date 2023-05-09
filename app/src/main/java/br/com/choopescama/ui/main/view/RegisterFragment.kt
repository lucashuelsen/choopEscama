package br.com.choopescama.ui.main.view

import android.content.Intent
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

    private fun setupViewModel(){
        mViewModel = ViewModelProvider(
            this,
            RegisterViewModel.ViewModelFactory(listener)
            ).get(RegisterViewModel::class.java)
    }

    private fun setupBinding(){
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        mBinding?.lifecycleOwner = this
        mBinding?.viewModel = mViewModel
    }

    private fun setupSuccessListener() {
        listener = OnCompleteListener<AuthResult> {
            mViewModel?.dataLoader?.setIsLoading(false)
            mViewModel?.exception?.value = it.exception?.message

            if(it.isSuccessful){
                callLogin()
            }
        }
    }

    private fun callLogin(){
        val intent = Intent(this, LoginFragment::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }
}