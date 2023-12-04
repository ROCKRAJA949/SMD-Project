package com.example.tic_tac_toechallenge.presentation

import android.content.Intent
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.tic_tac_toechallenge.R
import com.example.tic_tac_toechallenge.presentation.authentication.AuthViewModel
import com.example.tic_tac_toechallenge.presentation.sign_in.SignInState
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    state: SignInState, onSignInClick: () -> Unit, authViewModel: AuthViewModel, signInLauncher: ActivityResultLauncher<Intent>
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = state.signInError){
        state.signInError?.let {error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Image(
            painter = painterResource(id = R.drawable.xando),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick =  onSignInClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.outlinedButtonColors(Color(0xFFEEEEEE))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.googlelogo),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp)) // Add some space between text and image
                Text(text = "Login with Google", color = Color(0xFF053B50))
            }

//            GoogleSignInButton(
//                onClick = {
//                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                        .requestIdToken(stringResource(R.string.DEFAULT_WEB_CLIENT_ID))
//                        .requestEmail()
//                        .build()
//
//                    val signInIntent = GoogleSignIn.getClient(this, gso).signInIntent
//                    signInLauncher.launch(signInIntent)
//                }
//            )
        }
    }
}


@Composable
fun GoogleSignInButton(onClick: () -> Unit) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Button(
//                onClick =  onSignInClick,
        onClick = {
            coroutineScope.launch {
                onClick()
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = ButtonDefaults.outlinedButtonColors(Color(0xFFEEEEEE))
    ) {
        Image(
            painter = painterResource(id = R.drawable.googlelogo),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp)) // Add some space between text and image
        Text(text = "Login with Google", color = Color(0xFF053B50))
    }
}

//@Preview(showBackground = true)
//@Composable
//fun LoginScreenPreview() {
//    TicTacToeChallengeTheme {
//        LoginScreen()
//    }
//}
