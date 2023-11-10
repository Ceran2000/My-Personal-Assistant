package com.example.mypersonalassistant.auth

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.mypersonalassistant.R
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val firebaseAuth: FirebaseAuth,
    private val oneTapClient: SignInClient
) {

    private val scope = MainScope()

    private val authChangedFlow = MutableSharedFlow<Unit>()
    val currentUser: Flow<UserData?> = authChangedFlow
        .onStart { emit(Unit) }
        .map {
            firebaseAuth.currentUser?.let { UserData(it.uid, it.displayName, it.email, it.photoUrl.toString()) }
        }



    val isUserSignedIn: Flow<Boolean> = callbackFlow {
        val authStateListener = AuthStateListener { auth ->
            val isSignedIn = auth.currentUser != null
            trySend(isSignedIn)
        }
        firebaseAuth.addAuthStateListener(authStateListener)
        awaitClose {
            firebaseAuth.removeAuthStateListener(authStateListener)
        }
    }

    suspend fun signIn() = try {
        val result = oneTapClient.beginSignIn(buildSignInRequest()).await()
        result?.pendingIntent?.intentSender
    } catch (e: Exception) {
        e.printStackTrace()
        if (e is CancellationException) throw e
        null
    }

    private fun buildSignInRequest(): BeginSignInRequest = BeginSignInRequest.Builder()
        .setGoogleIdTokenRequestOptions(
            GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(context.getString(R.string.web_client_id))
                .build()
    )
        .setAutoSelectEnabled(true)
        .build()

    suspend fun signInWithIntent(intent: Intent): SignInResult {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        return try {
            val user = firebaseAuth.signInWithCredential(googleCredentials).await().user
            val userData = user?.let {
                UserData(userId = it.uid, userName = it.displayName, email = it.email, imageUrl = it.photoUrl?.toString())
            }
            Log.d("AuthManager", "### $userData")
            scope.launch {
                authChangedFlow.emit(Unit)
            }
            SignInResult(data = userData, errorMessage = null)
        } catch (e: Exception) {
            e.printStackTrace()
            SignInResult(data = null, errorMessage = e.message)
        }
    }

    suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
            firebaseAuth.signOut()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

data class SignInResult(val data: UserData?, val errorMessage: String?)

data class UserData(val userId: String, val userName: String?, val email: String?, val imageUrl: String?)