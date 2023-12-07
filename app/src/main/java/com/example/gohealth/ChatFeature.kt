package com.example.gohealth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.TextField
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource



import androidx.lifecycle.ViewModel
import androidx.compose.runtime.remember
import java.util.UUID
import androidx.navigation.NavHostController

class ChatViewModel : ViewModel() {
    var selectedChannel: ChatChannel? by mutableStateOf(null)
    var messages: List<ChatMessage> by mutableStateOf(emptyList())
    var chatChannels by mutableStateOf(emptyList<ChatChannel>()) // Define chatChannels here
    fun sendMessage(messageText: String) {
        val channel = selectedChannel ?: return
        val newMessage = ChatMessage(UUID.randomUUID().toString(), messageText, isSender = true)
        messages = messages + newMessage
    }
}

class ChatChannelRepository {
    fun getChatChannels(): List<ChatChannel> {
        // Replace this with fetch chat channels
        return listOf(
            ChatChannel("1", "Evie Becerra", "Hello!", android.R.drawable.ic_menu_report_image),
            ChatChannel("2", "Orsa Lambertini", "How goes it?", android.R.drawable.ic_menu_camera),
            ChatChannel("3", "Spongebob Squarepants", "F is for friends who do stuff together!", android.R.drawable.ic_menu_report_image),
            ChatChannel("4", "Kristyn Rash", "What should I do about this rash?", android.R.drawable.ic_menu_camera),
            ChatChannel("5", "Mallorie Anersen", "Cool- I will see you then!", android.R.drawable.ic_menu_report_image),
            ChatChannel("6", "Fernanda Goslin", "Wonderful- I'm free Saturday or Sunday morning", android.R.drawable.ic_menu_camera),
        )
    }
}


data class ChatMessage(val id: String, val text: String, val isSender: Boolean)
data class ChatChannel(val id: String, val name: String, val lastMessage: String, val imageUrl: Int)


@Composable
fun ChatFeature(navController: NavHostController, modifier: Modifier = Modifier) {
    val chatViewModel = remember { ChatViewModel() }
    val navController = rememberNavController()
    chatViewModel.chatChannels = ChatChannelRepository().getChatChannels()
    NavHost(navController, startDestination = "allMessages") {
        composable("allMessages") {
            val channels = chatViewModel.chatChannels
            AllMessagesScreen(channels) { channel ->
                chatViewModel.selectedChannel = channel
                navController.navigate("chat/${channel.id}")
            }
        }
        composable("chat/{channelId}") { backStackEntry ->
            val channelId = backStackEntry.arguments?.getString("channelId")
            val selectedChannel = chatViewModel.chatChannels.find { it.id == channelId }
            selectedChannel?.let {
                ChatScreen(it, chatViewModel) { messageText ->
                    // Handle sending the message.
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
        Image(
            painter = painterResource(id = channel.imageUrl),
            contentDescription = "Profile Pic",
            modifier = Modifier
                .size(60.dp, 60.dp)
                .clip(CircleShape)
                .background(Color.Cyan.copy(alpha = 0.4f))
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            verticalArrangement = Arrangement.Center

        ) {
            Text(text = channel.name, style = MaterialTheme.typography.bodyLarge)
            Text(text = channel.lastMessage, style = MaterialTheme.typography.bodyMedium)
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
        Text(text = channel.name, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun ChatScreen(
    channel: ChatChannel,
    chatViewModel: ChatViewModel,
    onSendClicked: (String) -> Unit
) {
    val messages = remember { mutableStateListOf<ChatMessage>() }


    DisposableEffect(chatViewModel.selectedChannel) {
        if (chatViewModel.selectedChannel != null) {
            // Replace this with logic to fetch messages for the given channel ID
            val conversationMessages = getConversationMessagesForChannelId(channel.id)
            messages.addAll(conversationMessages)
        }
        // Cleanup effect when the ChatScreen is disposed
        onDispose { }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ChatDetails(channel) // Display patient details

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(bottom = 8.dp)
        ) {
            items(messages) { message ->
                ChatMessageRow(message)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        MessageInput { messageText ->
            chatViewModel.sendMessage(messageText) // Call sendMessage to add a new message
            onSendClicked(messageText)
        }
    }
}

// Replace this with logic to fetch messages for the given channel ID
private fun getConversationMessagesForChannelId(channelId: String): List<ChatMessage> {
    return when (channelId) {
        "1" -> listOf(
            ChatMessage("1", "Hello!", isSender = true),
        )
        "2" -> listOf(
            ChatMessage("2", "Hey, how are you doing?", isSender = true),
            ChatMessage("3", "Hi", isSender = false),
            ChatMessage("4", "How goes it?", isSender = false)
        )
        "3" -> listOf(
            ChatMessage("5", "Do you like krabby patties?", isSender = true),
            ChatMessage("6", "Yes! Ofcourse! Who doesn't?", isSender = false),
            ChatMessage("7", "Are we friends now?", isSender = true),
            ChatMessage("8", "F is for friends who do stuff together!", isSender = false)
        )
        "4" -> listOf(
            ChatMessage("9", "Hey", isSender = false),
            ChatMessage("10", "What should I do about this rash?", isSender = false)
        )
        "5" -> listOf(
            ChatMessage("11", "Lunch friday?", isSender = true),
            ChatMessage("12", "Cool- I will see you then!", isSender = false)
        )
        "6" -> listOf(
            ChatMessage("13", "Come in for a lobotomy!", isSender = true),
            ChatMessage("14", "What is your availability?", isSender = true),
            ChatMessage("15", "Hey!", isSender = false),
            ChatMessage("16", "Wonderful- I'm free Saturday or Sunday morning", isSender = false)
        )
        else -> emptyList()
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


@OptIn(ExperimentalMaterial3Api::class)
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
            if (messageText.isNotBlank()) {
                onSendClicked(messageText)
                messageText = ""
            }
        }) {
            Text("Send")
        }
    }
}