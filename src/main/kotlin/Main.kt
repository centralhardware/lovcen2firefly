import dev.inmo.kslog.common.KSLog
import dev.inmo.kslog.common.configure
import dev.inmo.kslog.common.warning
import dev.inmo.tgbotapi.HealthCheck
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.telegramBotWithBehaviourAndLongPolling
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

suspend fun main() {
    KSLog.configure("lovcen2firefly")
    telegramBotWithBehaviourAndLongPolling(
        System.getenv("TOKEN"),
        CoroutineScope(Dispatchers.IO),
        defaultExceptionsHandler = {KSLog.warning(it)  }) {
        HealthCheck.addBot(this)
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