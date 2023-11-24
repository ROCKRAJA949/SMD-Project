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
        verticalArrangement = Arrangement.spacedBy(4.dp),
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
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (j in 0 until 3) {
            TicTacToeButton()
        }
    }
}

@Composable
fun TicTacToeButton() {
    Button(
        onClick = { /* Handle button click */ },
        modifier = Modifier
            .size(50.dp)
            .clip(RoundedCornerShape(0.dp))
    ) {
        // You can customize the content of the button here
        // For example, you can use icons like Icons.Default.Clear and Icons.Default.Close for X and O
        Icon(imageVector = Icons.Default.Clear, contentDescription = null)
    }
}

@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    TicTacToeChallengeTheme {
        GameScreen()
    }
}
