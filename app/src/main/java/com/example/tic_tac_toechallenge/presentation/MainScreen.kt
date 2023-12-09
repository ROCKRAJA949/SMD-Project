package com.example.tic_tac_toechallenge.presentation


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tic_tac_toechallenge.R
import com.example.tic_tac_toechallenge.presentation.sign_in.UserData
import com.example.tic_tac_toechallenge.ui.theme.TicTacToeChallengeTheme

@Composable
fun MainScreen(userData: UserData?, onSignOut:() -> Unit, onProfileClick:() -> Unit, onFriendsClick:() -> Unit, onGameClick: () -> Unit) {
    Log.d("userData", userData.toString())
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
            Image(
                painter = painterResource(id = R.drawable.gamelogo),
                contentDescription = null,
                modifier = Modifier.size(250.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(30.dp))
            //Find a Match
            Button(
                onClick =  onGameClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.outlinedButtonColors(Color(0xFFEEEEEE))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.findmatch),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp)) // Add some space between text and image
                Text(text = "Find a match", color = Color(0xFF053B50))
            }
            //Profile
            Spacer(modifier = Modifier.height(18.dp))
            Button(
                onClick =  onProfileClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.outlinedButtonColors(Color(0xFFEEEEEE))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp)) // Add some space between text and image
                Text(text = "Profile", color = Color(0xFF053B50))
            }
            //Friends List
            Spacer(modifier = Modifier.height(18.dp))
            Button(
                onClick =  onFriendsClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.outlinedButtonColors(Color(0xFFEEEEEE))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.friends),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp)) // Add some space between text and image
                Text(text = "Friends List", color = Color(0xFF053B50))
            }
            //Exit
            Spacer(modifier = Modifier.height(18.dp))
            Button(
                onClick =  onSignOut,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.outlinedButtonColors(Color(0xFFEEEEEE))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logout),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp)) // Add some space between text and image
                Text(text = "Exit", color = Color(0xFF053B50))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    TicTacToeChallengeTheme {
//        MainScreen()
    }
}
