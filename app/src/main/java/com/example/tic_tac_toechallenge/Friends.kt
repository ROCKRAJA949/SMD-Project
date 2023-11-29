package com.example.tic_tac_toechallenge

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tic_tac_toechallenge.ui.theme.TicTacToeChallengeTheme

@Composable
fun Friends() {


    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        // verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
        //.padding(16.dp)
    ) {
        item {
            // Top Bar
            TopBarFriends()


            FriendRow("John Smith")
            FriendRow("Land Cruiser")
            FriendRow("Peter McKinnon")




        }
    }
}

@Composable
fun TopBarFriends() {
    Column(
    )
    {

        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            modifier = Modifier
                .size(24.dp)
                .background(Color.White)
        )

        Row(

            Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(
                    MaterialTheme.colorScheme.primary
                    //painter = painterResource(id = R.drawable.bg)

                )
            ,
            verticalAlignment = Alignment.CenterVertically,

            ) {

            Spacer(modifier = Modifier.width(100.dp))




            //Spacer(modifier = Modifier.width(16.dp))

            // TopBar Heading
            Text(
                text = "FRIENDS", // Replace with actual user name
                //style = MaterialTheme.typography.h6,//
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun FriendRow(name: String) {
    Spacer(modifier = Modifier.height(30.dp))
    Row(

        modifier = Modifier
            //.size(300.dp, 100.dp) // Adjust the size of the rectangle
            .fillMaxWidth()
            .background(
                color = Color.LightGray, // Change the color as needed
                shape = RoundedCornerShape(6.dp) // Adjust the corner radius as needed
            )
            .shadow(1.dp)
            .padding(20.dp), // Optional: Add padding to the rectangle
        horizontalArrangement = Arrangement.SpaceBetween


    ) {


        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground), // Replace with actual user image
            contentDescription = null,
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onPrimary)
                .padding(8.dp)
        )
        //Spacer(modifier = Modifier.width(200.dp))
        // Player Wins
        Text(

            text = name, // Replace with actual wins count
            //style = MaterialTheme.typography.subtitle1,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            modifier = Modifier
                //.padding(8.dp)
                .align(Alignment.CenterVertically)


        )

        Button(
            onClick = {}

        ){
            Text(text = "INVITE")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FriendScreenPreview() {
    TicTacToeChallengeTheme {
        Friends()
    }
}

