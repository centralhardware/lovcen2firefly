import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import java.time.format.DateTimeFormatter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

val baseUrl = System.getenv("FIREFLY_HOST") + "/api/v1"
val transactionUrl = "$baseUrl/transactions"

val client =
    HttpClient(CIO) {
        install(Logging)
        install(ContentNegotiation) { json() }
    }

suspend fun createTransaction(transaction: Transaction) {
    client.post(transactionUrl) {
        setBody(
            Transactions(
                listOf(
                    FireflyTransaction(
                        "withdrawal",
                        transaction.seller,
                        transaction.ts.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        transaction.amount,
                        "",
                        System.getenv("SOURCE_NAME"),
                        transaction.seller,
                    )
                )
            )
        )
        bearerAuth(System.getenv("FIREFLY_TOKEN"))
        contentType(ContentType.Application.Json)
    }
}

@Serializable data class Transactions(val transactions: List<FireflyTransaction>)

@Serializable
data class FireflyTransaction(
    val type: String,
    val description: String,
    val date: String,
    val amount: Double,
    @SerialName("category_name") val category: String,
    @SerialName("source_name") val sourceName: String,
    @SerialName("destination_name") val destinationName: String,
)
