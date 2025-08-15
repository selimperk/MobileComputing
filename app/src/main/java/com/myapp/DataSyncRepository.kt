
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.util.concurrent.TimeUnit

class DataSyncRepository(applicationContext: Context) {
    val isUpToDate = mutableStateOf(false)

    suspend fun syncData() = withContext(Dispatchers.IO) {

        fetchDataFromServer()
    }


    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)

        .build()

    private suspend fun fetchDataFromServer() {
        try {
            // !!! IMPORTANT !!! Replace with your actual secure server URL using HTTPS
            val url = "https://your.actual.secure.server.com/api/data"
            val request = Request.Builder()
                .url(url)
                .build()


            val response: Response = okHttpClient.newCall(request).execute()


            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                Log.d("DataSyncRepository", "Server Response: $responseBody")
                // TODO: Hier kommt dein echter Datenabgleich und die Speicherung in der DB
                // Beispiel: parseResponseAndSaveToDatabase(responseBody)
                isUpToDate.value = true
            } else {
                Log.e("DataSyncRepository", "Server error: ${response.code} - ${response.message}")
                isUpToDate.value = false
            }
        } catch (e: IOException) {
            Log.e("DataSyncRepository", "Network error during sync: ${e.message}", e)
            isUpToDate.value = false
        } catch (e: Exception) {
            Log.e("DataSyncRepository", "Unexpected error during sync: ${e.message}", e)
            isUpToDate.value = false //
        }
    }

    fun checkIfUpToDate() {

    }
}