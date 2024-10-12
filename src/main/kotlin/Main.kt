import dev.inmo.kslog.common.KSLog
import dev.inmo.kslog.common.configure
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onText
import dev.inmo.tgbotapi.longPolling

suspend fun main() {
    KSLog.configure("lovcen2firefly")
    longPolling {
        onText {
            val transaction = parseSms(it.content.text)

            if (!transaction.isSuccessful) {
                sendTextMessage(it.chat, "Не успешная транзакция")
                return@onText
            }

            createTransaction(transaction)

            sendTextMessage(it.chat, "Сохранено")
        }

    }.second.join()
}