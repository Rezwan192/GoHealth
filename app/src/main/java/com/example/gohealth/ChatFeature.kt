package com.example.gohealth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.TextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.remember

class ChatViewModel : ViewModel() {
    var selectedChannel: ChatChannel? by mutableStateOf(null)
}

data class ChatMessage(val id: String, val text: String, val isSender: Boolean)
data class ChatChannel(val id: String, val name: String, val lastMessage: String, val imageUrl: Int)

val customTypography = Typography(
    h4 = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 30.sp
    ),
    body2 = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp
    )
)

class ChatFeature : ComponentActivity() {
    private val chatViewModel = ChatViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "allMessages") {
                    composable("allMessages") {
                        val channels = listOf(
                            ChatChannel("1", "Patient 1", "Hello!", android.R.drawable.ic_menu_report_image),
                            ChatChannel("2", "Patient 2", "Hello!", android.R.drawable.ic_menu_camera)
                        )
                        AllMessagesScreen(channels) { channel ->
                            chatViewModel.selectedChannel = channel
                            navController.navigate("chat/${channel.id}")
                        }
                    }
                    composable("chat/{channelId}") { backStackEntry ->
                        val channelId = backStackEntry.arguments?.getString("channelId")
                        val selectedChannel = chatViewModel.selectedChannel
                        val sampleMessages = listOf(
                            ChatMessage("1", "Hello!", false),
                            ChatMessage("2", "Hi!", true)
                        )
                        selectedChannel?.let {
                            ChatScreen(it, sampleMessages) { messageText ->
                                // Handle sending the message.
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ChatChannelRow(channel: ChatChannel, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.Start
    ) {
        // Show the profile image
        Image(
            painter = painterResource(id = channel.imageUrl),
            contentDescription = "Profile Pic",
            modifier = Modifier
                .size(60.dp, 60.dp)
                .clip(CircleShape)
                .background(Color.Cyan.copy(alpha = 0.4f))
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
        )

        Spacer(modifier = Modifier.width(8.dp)) // Spacer for spacing between image and text

        Column(
            verticalArrangement = Arrangement.Center

        ) {
            Text(text = channel.name, style = customTypography.h4)
            Text(text = channel.lastMessage, style = customTypography.body2)
        }
    }
}

@Composable
fun AllMessagesScreen(channels: List<ChatChannel>, onChannelClick: (ChatChannel) -> Unit) {
    LazyColumn {
        items(channels) { channel ->
            ChatChannelRow(channel) {
                onChannelClick(channel)
            }
        }
    }
}

@Composable
fun ChatDetails(channel: ChatChannel) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = channel.imageUrl),
            contentDescription = "Patient Image",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = channel.name, style = customTypography.h4)
    }
}

@Composable
fun ChatScreen(channel: ChatChannel, messages: List<ChatMessage>, onSendClicked: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ChatDetails(channel) // Display patient details

        // Rest of your ChatScreen composable remains the same, displaying chat messages
        LazyColumn(
            modifier = Modifier.weight(1f),
            reverseLayout = true,  // Newer messages at the bottom
            contentPadding = PaddingValues(bottom = 8.dp)
        ) {
            items(messages) { message ->
                ChatMessageRow(message)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        MessageInput(onSendClicked)
    }
}



@Composable
fun ChatMessageRow(message: ChatMessage) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        if (message.isSender) {
            Text(
                text = message.text,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .background(Color.Blue)
                    .padding(8.dp),
                color = Color.White
            )
        } else {
            Text(
                text = message.text,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .background(Color.Gray)
                    .padding(8.dp),
                color = Color.White
            )
        }
    }
}


@Composable
fun MessageInput(onSendClicked: (String) -> Unit) {
    var messageText by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextField(
            value = messageText,
            onValueChange = { messageText = it },
            modifier = Modifier.weight(1f),
            placeholder = { Text("Enter a message...") }
        )

        Spacer(modifier = Modifier.width(8.dp))

        Button(onClick = {
            onSendClicked(messageText)
            messageText = ""
        }) {
            Text("Send")
        }
    }
}