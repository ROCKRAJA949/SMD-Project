package com.example.tic_tac_toechallenge.presentation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tic_tac_toechallenge.R
import com.example.tic_tac_toechallenge.presentation.sign_in.UserData
import com.example.tic_tac_toechallenge.ui.theme.TicTacToeChallengeTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterCode() {
    var text by remember { mutableStateOf(value  = "00000") }

    TextField(
        value = text,
        onValueChange = { text = it },
        label = { Text(text = "Enter Game Code!", color = Color(0xFF053B50)) },
        modifier = Modifier.fillMaxWidth()
            .background(Color(0xFFEEEEEE)),
        colors = TextFieldDefaults.textFieldColors(textColor = Color(0xFF053B50))
    )
}

@Composable
fun JoinScreen() {
    //Log.d("userData", userData.toString())
    Box{
        Image(
            painter = painterResource(id = R.drawable.xando),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(modifier = Modifier.fillMaxWidth()){


            Image(
                painter = painterResource(id = R.drawable.backbutton),
                modifier = Modifier
                    .width(47.dp)
                    .height(47.dp)
                    .padding(8.dp)
                    .clickable {},

                contentDescription = "Back Button Image",
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .background(Color(0xFF176B87)),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,

                        ) {



                        Text(
                            text = "JOIN MATCH",
                            fontWeight = FontWeight.Bold,
                            fontSize = 40.sp,
                            color = Color(0xFFEEEEEE),
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(80.dp))

                    //add a text box here
                    EnterCode()

                    Spacer(modifier = Modifier.height(18.dp))
                    //Find a Match
                    Button(
                        onClick =  {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.outlinedButtonColors(Color(0xFFEEEEEE))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.joinmatch),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp)) // Add some space between text and image
                        Text(text = "Join Match", color = Color(0xFF053B50))
                    }
                    //Profile
                    Spacer(modifier = Modifier.height(18.dp))

                }
            }
        }
    }


}

@Preview(showBackground = true)
@Composable
fun JoinMatchPreview() {
    TicTacToeChallengeTheme {
        //JoinScreen()
    }
}

