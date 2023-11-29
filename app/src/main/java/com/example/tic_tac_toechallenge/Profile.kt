package com.example.tic_tac_toechallenge

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tic_tac_toechallenge.ui.theme.TicTacToeChallengeTheme


@Composable
fun Profile()
{
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        // verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
        //.padding(16.dp)
    ) {
        item {
            // Top Bar
            TopBar()
            Spacer(modifier = Modifier.height(30.dp))

            PlayerWinsRectangle()
            Spacer(modifier = Modifier.height(30.dp))
            PlayerLossesRectangle()


        }
    }
}

@Composable
fun TopBar() {
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
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)

                .background(
                    MaterialTheme.colorScheme.primary
                    //painter = painterResource(id = R.drawable.bg)

                ),

            verticalAlignment = Alignment.CenterVertically

        ) {

            Spacer(modifier = Modifier.width(16.dp))


            // User Image
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground), // Replace with actual user image
                contentDescription = null,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onPrimary)
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // User Name
            Text(
                text = "User Name", // Replace with actual user name
                //style = MaterialTheme.typography.h6,//
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun PlayerWinsRectangle() {

    Box(

        modifier = Modifier
            .size(300.dp, 100.dp) // Adjust the size of the rectangle
            .background(
                color = Color.LightGray, // Change the color as needed
                shape = RoundedCornerShape(6.dp) // Adjust the corner radius as needed
            )
            .shadow(1.dp)
            .padding(16.dp) // Optional: Add padding to the rectangle


    ) {

        // Player Wins
        Text(

            text = "Player Wins: 10", // Replace with actual wins count
            //style = MaterialTheme.typography.subtitle1,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.Center)


        )
    }
}

@Composable
fun PlayerLossesRectangle() {

    Box(

        modifier = Modifier
            .size(300.dp, 100.dp) // Adjust the size of the rectangle
            .background(
                color = Color.LightGray, // Change the color as needed
                shape = RoundedCornerShape(6.dp) // Adjust the corner radius as needed
            )
            .shadow(1.dp)
            .padding(16.dp) // Optional: Add padding to the rectangle


    ) {

        // Player Wins
        Text(

            text = "Player Losses: 5", // Replace with actual wins count
            //style = MaterialTheme.typography.subtitle1,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.Center)


        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    TicTacToeChallengeTheme {
        Profile()
    }
}