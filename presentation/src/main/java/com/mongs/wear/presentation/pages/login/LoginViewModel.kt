package com.mongs.wear.presentation.pages.login

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.mongs.wear.domain.auth.exception.LoginException
import com.mongs.wear.domain.auth.exception.NeedJoinException
import com.mongs.wear.domain.auth.exception.NeedUpdateAppException
import com.mongs.wear.domain.auth.usecase.JoinUseCase
import com.mongs.wear.domain.auth.usecase.LoginUseCase
import com.mongs.wear.presentation.global.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val joinUseCase: JoinUseCase,
    private val loginUseCase: LoginUseCase,
    private val firebaseMessaging: FirebaseMessaging,
): BaseViewModel() {

    /**
     * 구글 로그인 확인
     */
    init {
        viewModelScopeWithHandler.launch(Dispatchers.Main) {

            uiState.loadingBar = true

            GoogleSignIn.getLastSignedInAccount(context)?.let { account ->
                loginUseCase(
                    LoginUseCase.Param(
                        googleAccountId = account.id,
                        email = account.email,
                        fcmToken = firebaseMessaging.getTokenSuspend(),
                    )
                )
                uiState.navMainPagerView = true
            } ?: run {
                uiState.loadingBar = false
                uiState.signInButton = true
            }
        }
    }

    /**
     * 구글 로그인 완료
     */
    fun login(googleSignInResult: ActivityResult) {
        viewModelScopeWithHandler.launch(Dispatchers.IO) {
            if (googleSignInResult.resultCode == Activity.RESULT_OK) {
                GoogleSignIn.getSignedInAccountFromIntent(googleSignInResult.data).result?.let { account ->
                    loginUseCase(
                        LoginUseCase.Param(
                            googleAccountId = account.id,
                            email = account.email,
                            fcmToken = firebaseMessaging.getTokenSuspend(),
                        )
                    )
                    uiState.navMainPagerView = true
                }
            } else {
                uiState.loadingBar = false
                uiState.signInButton = true
            }
        }
    }

    /**
     * 회원 가입 & 로그인
     */
    private fun joinAndLogin() {
        viewModelScopeWithHandler.launch(Dispatchers.IO) {
            GoogleSignIn.getLastSignedInAccount(context)?.let { account ->

                joinUseCase(
                    JoinUseCase.Param(
                        googleAccountId = account.id,
                        email = account.email,
                        name = account.displayName
                    )
                )

                loginUseCase(
                    LoginUseCase.Param(
                        googleAccountId = account.id,
                        email = account.email,
                        fcmToken = firebaseMessaging.getTokenSuspend(),
                    )
                )

                uiState.navMainPagerView = true
            } ?: run {
                uiState.loadingBar = false
                uiState.signInButton = true
            }
        }
    }

    /**
     * 로그인 버튼
     */
    fun loginButtonClick(googleLoginLauncher: ActivityResultLauncher<Intent>) {
        viewModelScopeWithHandler.launch(Dispatchers.IO) {

            uiState.loadingBar = true
            uiState.signInButton = false

            val googleSignIn = GoogleSignIn.getLastSignedInAccount(context)
            val googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestId()
                .build()

            val googleSignInClient = GoogleSignIn.getClient(context, googleSignInOption)

            if (googleSignIn != null) {
                googleSignInClient.signOut()
            }

            googleLoginLauncher.launch(googleSignInClient.signInIntent)
        }
    }

    /**
     * FCM 토큰 조회
     */
    private suspend fun FirebaseMessaging.getTokenSuspend(): String = suspendCancellableCoroutine { cont ->
        getToken().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                cont.resume(task.result ?: "")
            } else {
                cont.resumeWithException(task.exception ?: Exception("Unknown error occurred"))
            }
        }
    }

    /**
     * UI 상태
     */
    val uiState: UiState = UiState()

    class UiState : BaseUiState() {
        var signInButton by mutableStateOf(false)
        var needAppUpdate by mutableStateOf(false)
        var navMainPagerView by mutableStateOf(false)
    }

    override fun exceptionHandler(exception: Throwable) {

        when (exception) {

            is NeedJoinException -> {
                joinAndLogin()
            }

            is NeedUpdateAppException -> {
                uiState.loadingBar = false
                uiState.needAppUpdate = true
            }

            is LoginException -> {
                uiState.loadingBar = false
                uiState.signInButton = true
            }

            else -> {
                uiState.loadingBar = false
                uiState.signInButton = true
            }
        }
    }
}