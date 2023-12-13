package com.example.tic_tac_toechallenge.presentation

import android.content.ContentValues
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
import com.example.tic_tac_toechallenge.presentation.authentication.JoinGameRequestModel
import com.example.tic_tac_toechallenge.presentation.authentication.MessageResponseModel
import com.example.tic_tac_toechallenge.presentation.authentication.UserResponseModel
import com.example.tic_tac_toechallenge.presentation.network.RetrofitInstance
import com.example.tic_tac_toechallenge.presentation.sign_in.GoogleAuthUIClient
import com.example.tic_tac_toechallenge.presentation.sign_in.UserData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Random

fun winCondition(board: List<String>): String{
    // Check rows
    for (i in 0 until 3) {
        val rowStart = i * 3
        if (board[rowStart] == board[rowStart + 1] && board[rowStart + 1] == board[rowStart + 2]) {
            return board[rowStart]
        }
    }

    // Check columns
    for (i in 0 until 3) {
        if (board[i] == board[i + 3] && board[i + 3] == board[i + 6]) {
            return board[i]
        }
    }

    // Check diagonals
    if (board[0] == board[4] && board[4] == board[8]) {
        return board[0]
    }
    if (board[2] == board[4] && board[4] == board[6]) {
        return board[2]
    }

    // No winner
    return "0";
}

@Composable
fun GameScreen( userData: UserData?, gameId: String) {
    val gamesDb = Firebase.firestore.collection("games")
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
                    override fun onResponsez(call: Call<GameResponseModel?>?, response: Response<GameResponseModel?>) {
//
                        val gameRef = gamesDb.document(id.value).addSnapshotListener { snapshot, e ->
                            if (e != null) {
                                Log.w(ContentValues.TAG, "Listen failed.", e)
                                return@addSnapshotListener
                            }
                            if (snapshot != null && snapshot.exists()) {
                                gameData = snapshot.toObject(GameResponseModel::class.java)
                                Log.d(ContentValues.TAG, "Current data: ${gameData?.boardState}")
                                gameData?.boardState?.let{winCondition(it)}
                            } else {
                                Log.d(ContentValues.TAG, "Current Data: null")
                                null
                            }
                        }
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
        else {
            id.value = gameId
            Log.d("gameId from join screen", id.value)

            gameData = getGameData(id.value)


            if((gameData?.player1Id.toString() == userData?.userId) || (gameData?.player2Id.toString() == userData?.userId)){
                Log.d("Join Error", userData.userId+" is already in session")
            }
            else {
                if(userData?.userId != null) {
                    val joinGameRequestModel = JoinGameRequestModel(id.value, userData.userId)
                    var message:MessageResponseModel = MessageResponseModel(message = "")
                    val call: Call<MessageResponseModel> =
                        RetrofitInstance.apiService.joinGame(joinGameRequestModel)

                    call!!.enqueue(object : Callback<MessageResponseModel?> {
                        override fun onResponse(
                            call: Call<MessageResponseModel?>?,
                            response: Response<MessageResponseModel?>
                        ) {
                        Log.d("API Response ", response.toString())
                            if(response.body() != null){
                                message = response.body()!!
                            }

                            Log.d("message from API", message.message)
                            val gameRef = gamesDb.document(id.value).addSnapshotListener { snapshot, e ->
                                if (e != null) {
                                    Log.w(ContentValues.TAG, "Listen failed.", e)
                                    return@addSnapshotListener
                                }
                                if (snapshot != null && snapshot.exists()) {
                                    gameData = snapshot.toObject(GameResponseModel::class.java)
                                    Log.d(ContentValues.TAG, "Current data: ${gameData?.boardState}")
                                } else {
                                    Log.d(ContentValues.TAG, "Current Data: null")
                                    null
                                }
                            }

                        }

                        override fun onFailure(call: Call<MessageResponseModel?>?, t: Throwable) {
                            // we get error response from API.
                            if (t.message != null) {
                                Log.d("Error found is : ", t.message!!)
                            }
                        }
                    })
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

suspend fun getGameData(id: String): GameResponseModel? {
    return withContext(Dispatchers.IO) {
        try {
            val response = RetrofitInstance.apiService.fetchGameData(id)

            if(response.isSuccessful) {
                response.body()
            }
            else {
                val arr: List<String> = emptyList()

                GameResponseModel("","","","","", arr)
            }
        }catch (e: Exception) {
            val arr: List<String> = emptyList()

            GameResponseModel("","","","","", arr)
        }
    }
}

