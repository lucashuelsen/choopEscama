package br.com.choopescama.ui.main.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.choopescama.databinding.ActivityLoginBinding
import androidx.databinding.DataBindingUtil
import br.com.choopescama.R
import br.com.choopescama.ui.main.viewmodel.LoginViewModel


class LoginFragment : AppCompatActivity() {
    private var mBinding : ActivityLoginBinding? = null
    private var mViewModel: LoginViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        setupViewModel()
    }

    private fun setupBinding(){
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        mBinding?.lifecycleOwner = this
    }

    private fun setupViewModel(){
        mViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
    }
}