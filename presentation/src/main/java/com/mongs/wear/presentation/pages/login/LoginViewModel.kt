package com.mongs.wear.presentation.pages.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.mongs.wear.core.exception.presentation.NotExistsEmailException
import com.mongs.wear.core.exception.presentation.NotExistsGoogleAccountIdException
import com.mongs.wear.core.exception.presentation.NotExistsNameException
import com.mongs.wear.core.exception.usecase.LoginUseCaseException
import com.mongs.wear.core.exception.usecase.NeedJoinUseCaseException
import com.mongs.wear.core.exception.usecase.NeedUpdateAppUseCaseException
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
                val googleAccountId = account.id
                val email = account.email

                if (googleAccountId.isNullOrEmpty()) throw NotExistsGoogleAccountIdException()

                if (email.isNullOrEmpty()) throw NotExistsEmailException()

                loginUseCase(
                    LoginUseCase.Param(
                        googleAccountId = googleAccountId,
                        email = email,
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

                    val googleAccountId = account.id
                    val email = account.email

                    if (googleAccountId.isNullOrEmpty()) throw NotExistsGoogleAccountIdException()

                    if (email.isNullOrEmpty()) throw NotExistsEmailException()

                    loginUseCase(
                        LoginUseCase.Param(
                            googleAccountId = googleAccountId,
                            email = email,
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

                val googleAccountId = account.id
                val email = account.email
                val name = account.displayName

                if (googleAccountId.isNullOrEmpty()) throw NotExistsGoogleAccountIdException()

                if (email.isNullOrEmpty()) throw NotExistsEmailException()

                if (name.isNullOrEmpty()) throw NotExistsNameException()

                joinUseCase(
                    JoinUseCase.Param(
                        googleAccountId = googleAccountId,
                        email = email,
                        name = name,
                    )
                )

                loginUseCase(
                    LoginUseCase.Param(
                        googleAccountId = googleAccountId,
                        email = email,
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
                cont.resumeWithException(task.exception ?: Exception("FCM 토큰 조회 실패"))
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

    override suspend fun exceptionHandler(exception: Throwable) {
        when (exception) {
            is NeedJoinUseCaseException -> {
                joinAndLogin()
            }

            is NeedUpdateAppUseCaseException -> {
                uiState.loadingBar = false
                uiState.needAppUpdate = true
            }

            is LoginUseCaseException -> {
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