package com.example.tic_tac_toechallenge

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tic_tac_toechallenge.presentation.GameScreen
import com.example.tic_tac_toechallenge.presentation.LoginScreen
import com.example.tic_tac_toechallenge.presentation.MainScreen
import com.example.tic_tac_toechallenge.presentation.Profile
import com.example.tic_tac_toechallenge.presentation.authentication.AuthViewModel
import com.example.tic_tac_toechallenge.presentation.sign_in.GoogleAuthUIClient
import com.example.tic_tac_toechallenge.presentation.sign_in.SignInViewModel
import com.example.tic_tac_toechallenge.ui.theme.TicTacToeChallengeTheme
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    private val googleAuthUiClient by lazy {
        GoogleAuthUIClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

//testing things ples ignore
    private val authViewModel: AuthViewModel by viewModels()
    private val signInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            val account = task.getResult(ApiException::class.java)
            if (account != null) {
                val idToken = account.idToken
                if (idToken != null) {
                    authViewModel.signInWithGoogle(idToken)
                }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacToeChallengeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = (navController), startDestination = "LoginScreen") {
                        composable("LoginScreen") {
                            val viewModel = viewModel<SignInViewModel> ()
                            val state by viewModel.state.collectAsStateWithLifecycle()

                            LaunchedEffect(key1 = Unit ){
                                if(googleAuthUiClient.getSignedInUser() != null){
                                    navController.navigate("mainMenu")
                                }
                            }


                            val launcher = rememberLauncherForActivityResult(
                                contract = ActivityResultContracts.StartIntentSenderForResult(),
                                onResult = { result ->
                                    if(result.resultCode == RESULT_OK) {
                                        lifecycleScope.launch {
                                            val signInResult = googleAuthUiClient.signInWithIntent(
                                                intent = result.data ?: return@launch
                                            )
                                            viewModel.onSignInResult(signInResult)
                                        }
                                    }
                                }
                            )
                            
                            LaunchedEffect(key1 = state.isSignInSuccessful) {
                                if(state.isSignInSuccessful) {
                                    Toast.makeText(
                                        applicationContext,
                                        "Sign in: "+ googleAuthUiClient.getSignedInUser()?.username,
                                        Toast.LENGTH_LONG
                                    ).show()

                                    navController.navigate("mainMenu")
                                    viewModel.resetState()
                                }

                            }
                            
                            LoginScreen(
                                state = state,
                                onSignInClick = {
                                    lifecycleScope.launch {
                                        val signInIntentSender = googleAuthUiClient.signIn()
                                        launcher.launch(
                                            IntentSenderRequest.Builder(
                                                signInIntentSender?: return@launch
                                            ).build()
                                        )
                                    }
                                },
                                authViewModel = authViewModel,
                                signInLauncher = signInLauncher
                            )
                        }
                        composable("mainMenu") {
                            val gameId = "empty"
                            MainScreen(
                                userData = googleAuthUiClient.getSignedInUser(),
                                onSignOut = {
                                    lifecycleScope.launch {
                                        googleAuthUiClient.signOut()
                                        Toast.makeText(
                                            applicationContext,
                                            "Signed Out",
                                            Toast.LENGTH_LONG
                                        ).show()

                                        navController.popBackStack()
                                    }
                                },
                                onProfileClick = {
                                    navController.navigate("profile")
                                },
                                onFriendsClick = {
                                    navController.navigate("friends")
                                },
                                onGameClick = {
                                    navController.navigate("game/${gameId}")
                                },
                                onJoinGameClick = {
                                    //navController.navigate("joinGameMiddle")
                                }
                            )
                        }
                        composable("profile") {
                            Profile(
                                onBack = {

                                        navController.popBackStack()
                                },
                                userData = googleAuthUiClient.getSignedInUser()

                            )
                        }
                        composable("friends") {
                            Friends(
                                onBack = {
                                    navController.popBackStack()
                                }
                            )
                        }
                        composable("game/{gameId}", arguments = listOf(
                            navArgument("gameId"){
                                type = NavType.StringType
                            }
                        )) {
                                val gameId = it.arguments?.getString("gameId")
                            GameScreen(
                                userData = googleAuthUiClient.getSignedInUser(),
                                gameId = gameId
                            )
                        }
                        composable("joinGameMiddle") {
//                            JoinScreen(
//                                onJoinClick = {
//                                    navController.navigate("game/${it}"))
//                                }
//                            )
                        }

                    }

                }
            }
        }
    }
}



//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    TicTacToeChallengeTheme {
//        LoginScreen()
//    }
//}