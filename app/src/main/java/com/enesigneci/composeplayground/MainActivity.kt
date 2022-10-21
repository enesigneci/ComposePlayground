package com.enesigneci.composeplayground

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.enesigneci.composeplayground.ui.theme.MyApplicationTheme
import com.skydoves.landscapist.glide.GlideImage

class MainActivity : ComponentActivity() {
    data class Message(
        val profileImageUrl: String,
        val author: String,
        val message: String
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Conversation(items = createDummyMessages())
            }
        }
    }
}
private fun createDummyMessages(): List<MainActivity.Message> {
    val dummyMessages = arrayListOf<MainActivity.Message>()
    for (i in 1..10){
        if (i % 2 == 0) {
            dummyMessages.add(MainActivity.Message("https://randomuser.me/api/portraits/men/79.jpg", "User 1", "Message"))
        } else {
            dummyMessages.add(MainActivity.Message("https://randomuser.me/api/portraits/men/80.jpg", "User 2", "Message"))
        }
    }
    return dummyMessages
}
@Composable
fun Conversation(items: List<MainActivity.Message>){
    val context = LocalContext.current
    LazyColumn{
        items(items) { message ->
            ConversationItem(message, onClick = {
                Toast.makeText(context, message.message, Toast.LENGTH_SHORT).show()
            })
        }
    }
}

@Composable
fun ConversationItem(message: MainActivity.Message, onClick: (message: MainActivity.Message) -> Unit) {
    Column(Modifier.clickable {
        onClick(message)
    }) {
        Spacer(modifier = Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(0.25f)) {
                GlideImage(
                    imageModel = { message.profileImageUrl },
                    requestBuilder = {
                        Glide
                            .with(LocalContext.current)
                            .asBitmap()
                            .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                            .transition(withCrossFade())
                    },
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                        .clip(CircleShape),
                    loading = {
                        Box(modifier = Modifier.matchParentSize()) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    },
                    failure = {
                        Text(text = "image request failed.")
                    })
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(0.75f)
                    .align(CenterVertically)
            ) {
                Text(text = message.author)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = message.message)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}