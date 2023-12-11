package com.example.tic_tac_toechallenge.presentation


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.tic_tac_toechallenge.R
import com.example.tic_tac_toechallenge.presentation.authentication.UserResponseModel
import com.example.tic_tac_toechallenge.presentation.network.RetrofitInstance
import com.example.tic_tac_toechallenge.presentation.sign_in.UserData
import com.example.tic_tac_toechallenge.ui.theme.TicTacToeChallengeTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

//import com.google.mlkit.vision.text.Text

@Composable
fun Profile(userData: UserData?,onBack:()->Unit) {
    var userDataAPI by remember { mutableStateOf<UserResponseModel?>(null) }
    LaunchedEffect(userData) {
        val id = userData?.userId
        if (id != null) {
            fetchData(id) {result ->
                result.onSuccess {
                    userDataAPI = it
                    Log.d("received User Data",userDataAPI.toString())
                }
                result.onFailure { error ->
                    Log.d("api Get failure", error.toString())
                }

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




        Image(
            painter = painterResource(id = R.drawable.backbutton),
            modifier = Modifier
                .width(47.dp)
                .height(47.dp)
                .padding(8.dp)
                .clickable { onBack() },

            contentDescription = "Back Button Image",
            contentScale = ContentScale.Crop,

        )



        Column (
            modifier = Modifier.fillMaxWidth()
        ){


            Spacer(modifier = Modifier.height(15.dp))


            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(Color(0xFF176B87)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,

                    ) {



                    //Spacer(modifier = Modifier.width(16.dp))

                    // TopBar Heading
                    Text(
                        text = "PROFILE", // Replace with actual user name
                        //style = MaterialTheme.typography.h6,//
                        fontWeight = FontWeight.Bold,
                        fontSize = 40.sp,
                        color = Color(0xFFEEEEEE),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(50.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {

                    AsyncImage(
                        model = userData?.profilePictureUrl,
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop)
                }

                Spacer(modifier = Modifier.height(10.dp))

                userDataAPI?.winCount?.let { PlayerWins(it) }
                userDataAPI?.lossCount?.let { PlayerLosses(it) }
                Spacer(modifier = Modifier.height(8.dp)) // Adjust the spacing as needed
            }
        }


    }
}


@Composable
fun PlayerWins(Wins: Number) {
    Spacer(modifier = Modifier.height(40.dp))
    Row(

        modifier = Modifier
            .height(100.dp)
            .fillMaxWidth()// Adjust the size of the rectangle
            .background(
                color = Color(0xFFEEEEEE), // Change the color as needed
                shape = RoundedCornerShape(6.dp) // Adjust the corner radius as needed
            )
            .shadow(1.dp),
        //.padding(2.dp), // Optional: Add padding to the rectangle
        //horizontalArrangement = Arrangement.SpaceBetween
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly

    ) {


        Image(
            painter = painterResource(id = R.drawable.trophy),
            contentDescription = "Winner Trophy",
            modifier = Modifier.height(60.dp)
        )

        // Player Wins
        Text(

            text = "WINS", // Replace with actual wins count
            //style = MaterialTheme.typography.subtitle1,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF053B50),
            fontSize  = 40.sp,

            modifier = Modifier
                .padding(8.dp)

            //.align(Alignment.Center)


        )
        Box(


        )
        {
            Image(
                painter = painterResource(id = R.drawable.circleyellow),
                contentDescription = "Winner Trophy",
                modifier = Modifier.height(80.dp)
            )
            Text(text = "$Wins",
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                modifier = Modifier.align(Alignment.Center)

            )
        }


    }
}


@Composable
fun PlayerLosses(Loss: Number) {
    Spacer(modifier = Modifier.height(40.dp))
    Row(

        modifier = Modifier
            .height(100.dp)
            .fillMaxWidth()// Adjust the size of the rectangle
            .background(
                color = Color(0xFFEEEEEE), // Change the color as needed
                shape = RoundedCornerShape(6.dp) // Adjust the corner radius as needed
            )
            .shadow(1.dp),
        //.padding(2.dp), // Optional: Add padding to the rectangle
        //horizontalArrangement = Arrangement.SpaceBetween
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly

    ) {


        Image(
            painter = painterResource(id = R.drawable.circlered),
            contentDescription = "Thumbs Down - Losses",
            modifier = Modifier.height(60.dp)
        )

        // Player Wins
        Text(

            text = "LOSSES", // Replace with actual wins count
            //style = MaterialTheme.typography.subtitle1,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF053B50),
            fontSize  = 40.sp,

            modifier = Modifier
                .padding(8.dp)

            //.align(Alignment.Center)


        )
        Box(


        )
        {
            Image(
                painter = painterResource(id = R.drawable.circlered),
                contentDescription = "Circle Red - Losses",
                modifier = Modifier.height(80.dp)
            )
            Text(text = "$Loss",
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                modifier = Modifier.align(Alignment.Center)

            )
        }


    }
}

private fun fetchData(id: String, onComplete: (Result<UserResponseModel>) -> Unit) {
    GlobalScope.launch(Dispatchers.IO) {
        try {
            val response = RetrofitInstance.apiService.fetchData(id)
            onComplete(Result.success(response))
        // Handle the response as needed
        } catch (e: Exception) {
            // Handle the error
            onComplete(Result.failure(e))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    TicTacToeChallengeTheme {
//        Profile()
    }
}
