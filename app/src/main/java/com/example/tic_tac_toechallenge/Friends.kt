package com.example.tic_tac_toechallenge

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.tic_tac_toechallenge.ui.theme.TicTacToeChallengeTheme

@Composable
fun Friends() {
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
}

@Composable
fun TopBarFriends() {
    Column(
    )
    {

        Image(
            painter = painterResource(id = R.drawable.backbutton),
            modifier = Modifier
                .width(47.dp)
                .height(47.dp)
                .padding(8.dp)
                .clickable { println("Back Button Clicked") },

            contentDescription = "Back Button Image",
            contentScale = ContentScale.Crop
        )

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
                text = "FRIENDS", // Replace with actual user name
                //style = MaterialTheme.typography.h6,//
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                color = Color(0xFFEEEEEE),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun FriendRow(name: String) {
    Spacer(modifier = Modifier.height(12.dp))
    Row(

        modifier = Modifier
            //.size(300.dp, 100.dp) // Adjust the size of the rectangle
            .fillMaxWidth()
            .background(
                color = Color(0xFFEEEEEE), // Change the color as needed
                shape = RoundedCornerShape(6.dp) // Adjust the corner radius as needed
            )
            .shadow(1.dp)
            .padding(20.dp), // Optional: Add padding to the rectangle
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically


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
            color = Color(0xFF053B50),
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            modifier = Modifier
                //.padding(8.dp)
                .align(Alignment.CenterVertically)
        )

        Button(
            onClick =  {},
            colors = ButtonDefaults.outlinedButtonColors(Color(0xFF053B50))
        ) {
            Text(text = "Invite", color = Color(0xFFEEEEEE))
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

