import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

val smsRegex = Regex("Kartica:\\s*(?<card>\\d+)\\n" +
        "Iznos:\\s*(?<amount>\\d+\\.\\d+)\\s*EUR\\n" +
        "Vrijeme:\\s*(?<ts>\\d{2}\\.\\d{2}\\.\\d{4} \\d{2}:\\d{2}:\\d{2})\\n" +
        "Status:\\s*(?<status>\\w+)\\n" +
        "Opis:\\s*(?<seller>.*)\\n" +
        "Raspolozivo:\\s*(?<balance>\\d+\\.\\d+)\\s*EUR")
fun parseSms(sms: String): Transaction {
    return smsRegex.matchEntire(sms)!!.let { matchRes ->
        Transaction(
            matchRes.groups["card"]!!.value,
            matchRes.groups["amount"]!!.value.toDouble(),
            matchRes.groups["ts"]!!.value.parse(),
            matchRes.groups["status"]!!.value == "ODOBRENO",
            matchRes.groups["seller"]!!.value,
            matchRes.groups["balance"]!!.value.toDouble()
        )
    }
}

data class Transaction(
    val card: String,
    val amount: Double,
    val ts: LocalDateTime,
    val isSuccessful: Boolean,
    val seller: String,
    val balance: Double
)

val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
fun String?.parse() = LocalDateTime.parse(this, formatter)

