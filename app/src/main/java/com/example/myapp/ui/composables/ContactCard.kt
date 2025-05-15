import android.icu.text.ListFormatter.Width
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ContactCard(
    firstname:String, lastname:String,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .height(30.dp)
                .width(30.dp)
                .clip(CircleShape)
                .background(Color.Gray),
            contentAlignment = Alignment.Center,
        ) {
            Text("H", color = Color.White)
        }

        Column {
            Text(firstname, fontSize = 10.sp)
            Text(lastname, fontSize = 10.sp)
        }

        IconButton(onClick = { /* doSomething() */ }) {
            Icon(Icons.Filled.Call, tint = Color.Blue ,contentDescription = "Localized description")
        }
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewContactCard() {
    ContactCard("Hallo", "Na du")
}