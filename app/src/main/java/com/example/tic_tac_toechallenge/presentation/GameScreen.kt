package com.example.tic_tac_toechallenge.presentation

import android.content.ContentValues
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.RowScope.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
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
import com.example.tic_tac_toechallenge.presentation.network.RetrofitInstance
import com.example.tic_tac_toechallenge.presentation.sign_in.UserData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Random

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
                    override fun onResponse(call: Call<GameResponseModel?>?, response: Response<GameResponseModel?>) {
//
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
            gameData?.boardState?.let { TicTacToeBoard(it) }
        }
    }
}

@Composable
fun TicTacToeBoard(boardState: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally // Center horizontally
    ) {

        TicTacToeRow(boardState.get(0), boardState.get(1), boardState.get(2), onClickLeft = {}, onClickMiddle = {}, onClickRight = {})
        TicTacToeRow(boardState.get(3), boardState.get(4), boardState.get(5), onClickLeft = {}, onClickMiddle = {}, onClickRight = {})
        TicTacToeRow(boardState.get(6), boardState.get(7), boardState.get(8), onClickLeft = {}, onClickMiddle = {}, onClickRight = {})

    }
}

@Composable
fun TicTacToeRow(left: String, middle:String, right: String, onClickLeft: () -> Unit,onClickMiddle: () -> Unit, onClickRight: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalArrangement = Arrangement.Center, // Center horizontally
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TicTacToeButton(left, onClickLeft)
        Spacer(modifier = Modifier.width(8.dp)) // Add spacing between buttons
        TicTacToeButton(middle, onClickMiddle)
        Spacer(modifier = Modifier.width(8.dp)) // Add spacing between buttons
        TicTacToeButton(right, onClickRight)
    }
}

@Composable
fun TicTacToeButton(value: String, onClick:()->Unit) {
    Button(
        onClick = { /* Handle button click */ },
        modifier = Modifier
            .size(80.dp) // Increase the button size
            .clip(RoundedCornerShape(4.dp)), // Add rounded corners
        colors = ButtonDefaults.outlinedButtonColors(Color(0xFFEEEEEE)),

    ) {

            Log.d("button value", value)


            if(value == "x") {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = null,
                    modifier = Modifier.size(100.dp),
                    tint = Color(0xFF053B50)
                )
            }
            if(value == "o") {
                Image(
                    painterResource(id = R.drawable.circle),
                    modifier = Modifier.size(25.dp),
                    contentDescription = null,
                )
            }



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

