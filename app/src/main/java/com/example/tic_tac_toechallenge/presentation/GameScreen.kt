package com.example.tic_tac_toechallenge.presentation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tic_tac_toechallenge.R
import com.example.tic_tac_toechallenge.presentation.authentication.GameRequestModel
import com.example.tic_tac_toechallenge.presentation.authentication.GameResponseModel
import com.example.tic_tac_toechallenge.presentation.authentication.UserResponseModel
import com.example.tic_tac_toechallenge.presentation.network.RetrofitInstance
import com.example.tic_tac_toechallenge.presentation.sign_in.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Random

@Composable
fun GameScreen( userData: UserData?, gameId: String?) {
    var id = remember {
        mutableStateOf("")
    }
    var gameData by remember { mutableStateOf<GameResponseModel?>(null) }
    LaunchedEffect(gameId){
        Log.d("Game Start","in Launched Effect")
        if(gameId == "empty"){
            val r = Random(System.currentTimeMillis())
            id.value =  ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000)).toString()
            if(userData?.userId != null){
                val gameRequestModel =  GameRequestModel (id.value, userData.userId)

                val call: Call<GameResponseModel> = RetrofitInstance.apiService.createGame(gameRequestModel)

                call!!.enqueue(object : Callback<GameResponseModel?> {
                    override fun onResponse(call: Call<GameResponseModel?>?, response: Response<GameResponseModel?>) {
//                        Log.d("API Response ", response.body().toString())
                        gameData = response.body()
                        Log.d("Game Data from API", gameData.toString())

                    }

                    override fun onFailure(call: Call<GameResponseModel?>?, t: Throwable) {
                        // we get error response from API.
                        if(t.message != null) {
                            Log.d("Error found is : ", t.message!!)
                        }
                    }
                })

            }
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
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally // Center horizontally
        ) {
            Text(
                text = id.value, // Replace with actual user name
                //style = MaterialTheme.typography.h6,//
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                color = Color(0xFFEEEEEE),
                textAlign = TextAlign.Center
            )
            TicTacToeBoard()
        }
    }
}

@Composable
fun TicTacToeBoard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally // Center horizontally
    ) {
        for (i in 0 until 3) {
            TicTacToeRow()
        }
    }
}

@Composable
fun TicTacToeRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalArrangement = Arrangement.Center, // Center horizontally
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TicTacToeButton()
        Spacer(modifier = Modifier.width(8.dp)) // Add spacing between buttons
        TicTacToeButton()
        Spacer(modifier = Modifier.width(8.dp)) // Add spacing between buttons
        TicTacToeButton()
    }
}

@Composable
fun TicTacToeButton() {
    Button(
        onClick = { /* Handle button click */ },
        modifier = Modifier
            .size(80.dp) // Increase the button size
            .clip(RoundedCornerShape(4.dp)), // Add rounded corners
        colors = ButtonDefaults.outlinedButtonColors(Color(0xFFEEEEEE))
    ) {
        // You can customize the content of the button here
        // For example, you can use icons like Icons.Default.Clear and Icons.Default.Close for X and O
        /*
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            modifier = Modifier.size(40.dp),
            tint = Color.Red

            //Icons for x and o
            /*
                For 'O':
                Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = Color(0xFF053B50)
                )

                For 'X':
                Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = Color(0xFF053B50)
                )
            */

        )
        */
    }
}

//@Preview(showBackground = true)
//@Composable
//fun GameScreenPreview() {
//    TicTacToeChallengeTheme {
//        GameScreen()
//    }
//}
