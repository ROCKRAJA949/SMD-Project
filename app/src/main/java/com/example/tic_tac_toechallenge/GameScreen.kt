package com.example.tic_tac_toechallenge

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tic_tac_toechallenge.ui.theme.TicTacToeChallengeTheme

@Composable
fun GameScreen() {
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

@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    TicTacToeChallengeTheme {
        GameScreen()
    }
}
