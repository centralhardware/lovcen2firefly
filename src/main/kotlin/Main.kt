import dev.inmo.micro_utils.common.Warning
import dev.inmo.tgbotapi.AppConfig
import dev.inmo.tgbotapi.extensions.api.send.sendTextMessage
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onText
import dev.inmo.tgbotapi.longPolling
import me.centralhardware.telegram.EnvironmentVariableUserAccessChecker
import me.centralhardware.telegram.restrictAccess

@OptIn(Warning::class)
suspend fun main() {
    AppConfig.init("lovcen2firefly")
    longPolling({ restrictAccess(EnvironmentVariableUserAccessChecker()) }) {
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