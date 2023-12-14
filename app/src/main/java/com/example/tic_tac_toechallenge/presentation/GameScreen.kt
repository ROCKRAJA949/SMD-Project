package com.example.tic_tac_toechallenge.presentation

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.example.tic_tac_toechallenge.presentation.authentication.UpdateGameRequestModel
import com.example.tic_tac_toechallenge.presentation.authentication.UserResponseModel
import com.example.tic_tac_toechallenge.presentation.authentication.WinnerRequestModel
import com.example.tic_tac_toechallenge.presentation.network.RetrofitInstance
import com.example.tic_tac_toechallenge.presentation.sign_in.UserData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Random
import java.util.logging.LogManager

fun winCondition(board: List<String>): String{

    // Check rows
    for (i in 0 until 3) {
        val rowStart = i * 3
        Log.d("checking row", rowStart.toString())
        if (board[rowStart] == board[rowStart + 1] && board[rowStart + 1] == board[rowStart + 2] && (board[rowStart] == "x" || board[rowStart] == "o")) {
            return board[rowStart]
        }
    }

    // Check columns
    for (i in 0 until 3) {
        Log.d("checking column", i.toString())
        if (board[i] == board[i + 3] && board[i + 3] == board[i + 6] &&  (board[i] == "x" || board[i] == "o")) {
            return board[i]
        }
    }

    // Check diagonals
    if (board[0] == board[4] && board[4] == board[8] && (board[0] == "x" || board[0] == "o")) {
        return board[0]
    }
    if (board[2] == board[4] && board[4] == board[6] && (board[2] == "x" || board[2] == "o")) {
        return board[2]
    }

    // No winner
    return "0";
}

fun drawCheck(board: List<String>): String {
    var drawCount = 0
    for(i in 0 until 9){
        if(board[i] == "")
            break
        else
            drawCount++
    }

    if(drawCount == 9){
        Log.d("draw check", "true")
        return "d"
    }

    drawCount = 0
    return ""
}
//@Composable
//fun playerJoinToast(context: Context, player2Id: String) {
//    var joiner by remember { mutableStateOf<String>("123") }
//    fetchData(player2Id){result ->
//        result.onSuccess {
//            joiner = it.username!!
//            Log.d("received User Name for toast", joiner)
//            Toast.makeText(context, joiner + " has joined!", Toast.LENGTH_LONG).show()
//        }
//        result.onFailure { error ->
//            Log.d("toast failure", error.toString())
//        }
//
//    }
//
//}

@Composable
fun GameScreen( userData: UserData?, gameId: String, onBackClick: ()-> Unit) {
    val gamesDb = Firebase.firestore.collection("games")
    var id = remember {
        mutableStateOf("")
    }
    var gameData by remember { mutableStateOf<GameResponseModel?>(null) }
    var toastCount by remember { mutableStateOf<Int>(0)}
    LaunchedEffect(gameId){
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

                                Log.d(ContentValues.TAG, "Current data: ${gameData?.turn}")
                                gameData?.boardState?.let{winCondition(it)}
                                gameData?.boardState?.let { drawCheck(it) }
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
            gameData?.let { TicTacToeBoard(it, userData, id.value) }

//            if(gameData?.player2Id?.length === 28){
//                Log.d("lenght of player 2 id ", gameData?.player2Id?.length.toString())
//                toastCount++;
//                playerJoinToast(LocalContext.current, gameData?.player2Id!!)
//            }

            gameData?.let{ UserTurn(it.turn, it, onBackClick) }

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





@Composable
fun UserTurn(userid: String, gameData: GameResponseModel, onBackClick: () -> Unit){
    var userName by remember { mutableStateOf<String?>("") }
    fetchData(userid){result ->
        result.onSuccess {
            userName = it.username
            Log.d("received User Name for turn",it.username.toString())
        }
        result.onFailure { error ->
            ""
            Log.d("turn user name failure", error.toString())
        }

    }

    Row(

        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color(0xFF176B87)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,

        ) {



        Spacer(modifier = Modifier.width(20.dp))
        if(gameData.winnerId == "") {
            if(gameData?.boardState?.let{drawCheck(it)} == "d") {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Draw!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        color = Color(0xFFEEEEEE),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.width(30.dp))

                    Button(
                        onClick = onBackClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.outlinedButtonColors(Color(0xFFEEEEEE))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.back),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp)) // Add some space between text and image
                        Text(text = "Go Back", color = Color(0xFF053B50))
                    }
                }
            }else{
                Text(
                    text =  userName.toString() + "'s Turn!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    color = Color(0xFFEEEEEE),
                    textAlign = TextAlign.Center
                )
            }
        }
        else {
            var winnerName  by remember { mutableStateOf<String?>("") }
            fetchData(gameData.winnerId){result ->
                result.onSuccess {
                    winnerName =it.username
                    Log.d("received User Name for winner",it.username.toString())
                }
                result.onFailure { error ->
                    ""
                    Log.d("winner user name failure", error.toString())
                }

            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if(winnerName != ""){
                    Text(
                        text =  winnerName.toString() + " Wins!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        color = Color(0xFFEEEEEE),
                        textAlign = TextAlign.Center
                    )
                }
                else{
                    Text(
                        text =  "Loading...",
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        color = Color(0xFFEEEEEE),
                        textAlign = TextAlign.Center
                    )
                }


                Spacer(modifier = Modifier.width(30.dp))

                Button(
                    onClick =  onBackClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.outlinedButtonColors(Color(0xFFEEEEEE))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.back),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // Add some space between text and image
                    Text(text = "Go Back", color = Color(0xFF053B50))
                }
            }

        }
    }

}

@Composable
fun TicTacToeBoard(gameData: GameResponseModel, userData: UserData?, gameId: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally // Center horizontally
    ) {

        TicTacToeRow(gameData.boardState.get(0), gameData.boardState.get(1), gameData.boardState.get(2), onClickLeft = { onClickTopLeft(gameData, userData, gameId) }, onClickMiddle = { onClickTop(gameData, userData, gameId) }, onClickRight = { onClickTopRight(gameData, userData, gameId) })
        TicTacToeRow(gameData.boardState.get(3), gameData.boardState.get(4), gameData.boardState.get(5), onClickLeft = { onClickMiddleLeft(gameData, userData, gameId) }, onClickMiddle = { onClickMiddle(gameData, userData, gameId) }, onClickRight = { onClickMiddleRight(gameData, userData, gameId) })
        TicTacToeRow(gameData.boardState.get(6), gameData.boardState.get(7), gameData.boardState.get(8), onClickLeft = { onClickBottomLeft(gameData, userData, gameId) }, onClickMiddle = { onClickBottom(gameData, userData, gameId) }, onClickRight = { onClickBottomRight(gameData, userData, gameId) })

    }
}

fun onClickTopLeft(gameData: GameResponseModel, userData: UserData?, gameId: String) {
    val updatedBoardState: MutableList<String> = gameData.boardState.toMutableList()
    if(gameData.turn == userData?.userId) {
        if(gameData.turn == gameData.player1Id) {
            updatedBoardState[0] = "x"
        }
        else {
            updatedBoardState[0] = "o"
        }

        updateGameBoardAndWinDeclaration(gameId, boardState = updatedBoardState, userData, gameData)
    }

}

fun onClickTop(gameData: GameResponseModel, userData: UserData?, gameId: String) {
    val updatedBoardState: MutableList<String> = gameData.boardState.toMutableList()
    if(gameData.turn == userData?.userId) {
        if(gameData.turn == gameData.player1Id) {
            updatedBoardState[1] = "x"
        }
        else {
            updatedBoardState[1] = "o"
        }

        updateGameBoardAndWinDeclaration(gameId, boardState = updatedBoardState, userData, gameData)
    }

}

fun onClickTopRight(gameData: GameResponseModel, userData: UserData?, gameId: String) {
    val updatedBoardState: MutableList<String> = gameData.boardState.toMutableList()
    if(gameData.turn == userData?.userId) {
        if(gameData.turn == gameData.player1Id) {
            updatedBoardState[2] = "x"
        }
        else {
            updatedBoardState[2] = "o"
        }

        updateGameBoardAndWinDeclaration(gameId, boardState = updatedBoardState, userData, gameData)
    }

}

fun onClickMiddleLeft(gameData: GameResponseModel, userData: UserData?, gameId: String) {
    val updatedBoardState: MutableList<String> = gameData.boardState.toMutableList()
    if(gameData.turn == userData?.userId) {
        if(gameData.turn == gameData.player1Id) {
            updatedBoardState[3] = "x"
        }
        else {
            updatedBoardState[3] = "o"
        }

        updateGameBoardAndWinDeclaration(gameId, boardState = updatedBoardState, userData, gameData)
    }

}

fun onClickMiddle(gameData: GameResponseModel, userData: UserData?, gameId: String) {
    val updatedBoardState: MutableList<String> = gameData.boardState.toMutableList()
    if(gameData.turn == userData?.userId) {
        if(gameData.turn == gameData.player1Id) {
            updatedBoardState[4] = "x"
        }
        else {
            updatedBoardState[4] = "o"
        }

        updateGameBoardAndWinDeclaration(gameId, boardState = updatedBoardState, userData, gameData)
    }

}

fun onClickMiddleRight(gameData: GameResponseModel, userData: UserData?, gameId: String) {
    val updatedBoardState: MutableList<String> = gameData.boardState.toMutableList()
    if(gameData.turn == userData?.userId) {
        if(gameData.turn == gameData.player1Id) {
            updatedBoardState[5] = "x"
        }
        else {
            updatedBoardState[5] = "o"
        }

        updateGameBoardAndWinDeclaration(gameId, boardState = updatedBoardState, userData, gameData)
    }

}

fun onClickBottomLeft(gameData: GameResponseModel, userData: UserData?, gameId: String) {
    val updatedBoardState: MutableList<String> = gameData.boardState.toMutableList()
    if(gameData.turn == userData?.userId) {
        if(gameData.turn == gameData.player1Id) {
            updatedBoardState[6] = "x"
        }
        else {
            updatedBoardState[6] = "o"
        }

        updateGameBoardAndWinDeclaration(gameId, boardState = updatedBoardState, userData, gameData)
    }

}

fun onClickBottom(gameData: GameResponseModel, userData: UserData?, gameId: String) {
    val updatedBoardState: MutableList<String> = gameData.boardState.toMutableList()
    if(gameData.turn == userData?.userId) {
        if(gameData.turn == gameData.player1Id) {
            updatedBoardState[7] = "x"
        }
        else {
            updatedBoardState[7] = "o"
        }

        updateGameBoardAndWinDeclaration(gameId, boardState = updatedBoardState, userData, gameData)
    }

}

fun onClickBottomRight(gameData: GameResponseModel, userData: UserData?, gameId: String) {
    val updatedBoardState: MutableList<String> = gameData.boardState.toMutableList()
    if(gameData.turn == userData?.userId) {
        if(gameData.turn == gameData.player1Id) {
            updatedBoardState[8] = "x"
        }
        else {
            updatedBoardState[8] = "o"
        }

       updateGameBoardAndWinDeclaration(gameId, boardState = updatedBoardState, userData, gameData)
    }

}


fun updateGameBoardAndWinDeclaration(gameId:String, boardState:List<String>, userData: UserData?, gameData: GameResponseModel) {


    val updateGameRequestModel =
        userData?.userId?.let { UpdateGameRequestModel(gameId, boardState, it) }
    val call: Call<MessageResponseModel>? = updateGameRequestModel?.let {
        RetrofitInstance.apiService.updateGame(
            it
        )
    }

    call!!.enqueue(object : Callback<MessageResponseModel?> {
        override fun onResponse(call: Call<MessageResponseModel?>?, response: Response<MessageResponseModel?>) {

            Log.d("Game update message", response.message())
        }

        override fun onFailure(call: Call<MessageResponseModel?>?, t: Throwable) {
            if(t.message != null) {
                Log.d("Error found is : ", t.message!!)
            }
        }
    })

    val winner:String = winCondition(boardState)


    if(winner == "x"){
        val winnerId = gameData.player1Id
        val winnerRequestModel = WinnerRequestModel(id = gameId, winnerId = winnerId)

        val call: Call<MessageResponseModel> = RetrofitInstance.apiService.declareWinner(winnerRequestModel)

        call!!.enqueue(object : Callback<MessageResponseModel?> {
            override fun onResponse(call: Call<MessageResponseModel?>?, response: Response<MessageResponseModel?>) {
                Log.d("Message from api", response.message())

            }

            override fun onFailure(call: Call<MessageResponseModel?>?, t: Throwable) {
                // we get error response from API.
                if(t.message != null) {
                    Log.d("Error found is : ", t.message!!)
                }
            }
        })
    }
    else if (winner == "o") {
        val winnerId = gameData.player2Id
        val winnerRequestModel = winnerId?.let { WinnerRequestModel(id = gameId, it) }

        val call: Call<MessageResponseModel>? = winnerRequestModel?.let {
            RetrofitInstance.apiService.declareWinner(
                it
            )
        }

        call!!.enqueue(object : Callback<MessageResponseModel?> {
            override fun onResponse(call: Call<MessageResponseModel?>?, response: Response<MessageResponseModel?>) {
                Log.d("Message from api", response.message())

            }

            override fun onFailure(call: Call<MessageResponseModel?>?, t: Throwable) {
                // we get error response from API.
                if(t.message != null) {
                    Log.d("Error found is : ", t.message!!)
                }
            }
        })
    }
    else {
        //do nothing
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
        onClick = onClick,
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



