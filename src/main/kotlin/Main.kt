import dev.inmo.kslog.common.KSLog
import dev.inmo.kslog.common.configure
import dev.inmo.kslog.common.warning
import dev.inmo.tgbotapi.bot.ktor.HealthCheckKtorPipelineStepsHolder
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.telegramBotWithBehaviourAndLongPolling
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

val healthChecker = HealthCheckKtorPipelineStepsHolder()
suspend fun main() {
    KSLog.configure("lovcen2firefly")
    telegramBotWithBehaviourAndLongPolling(
        System.getenv("TOKEN"),
        CoroutineScope(Dispatchers.IO),
        defaultExceptionsHandler = {KSLog.warning(it)  },
        builder = { pipelineStepsHolder = healthChecker },) {
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