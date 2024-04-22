package ru.netology

data class Chat(val chatId: Int, val usersId: List<Int>)

data class Message(
    val chat: Chat,
    val messageId: Int,
    val fromId: Int,
    val tooId: Int,
    var message: String,
    var isRead: Boolean,
)

class MessageNotFoundException(message: String) : RuntimeException(message)
class ChatNotFoundException(message: String) : RuntimeException(message)
class CantSendMessageToYourselfException(message: String) : RuntimeException(message)
class UserNotFoundException(message: String) : RuntimeException(message)

object ChatService {
    private var messages = mutableListOf<Message>()
    private var chats = mutableListOf<Chat>()
    private var chatLastId = 0
    private var messageLastID = 0


    fun clean() {
        messages = mutableListOf<Message>()
        chats = mutableListOf<Chat>()
        chatLastId = 0
        messageLastID = 0
    }

    fun sendMessage(message: String, fromId: Int, toId: Int): Int {
        if (toId == fromId) throw CantSendMessageToYourselfException("You can't send message to yourself")
        chats.firstOrNull { it.usersId.containsAll(listOf(fromId, toId)) } ?: chats.add(
            Chat(
                ++chatLastId, listOf(fromId, toId)
            )
        )

        messages.add(
            Message(
                chat = chats.first { it.usersId.containsAll(listOf(fromId, toId)) },
                messageId = ++messageLastID,
                fromId = fromId,
                tooId = toId,
                message = message,
                isRead = false
            )
        )
        return messageLastID
    }

    fun editMessage(messageId: Int, message: String): Boolean {

        messages.firstOrNull { it.messageId == messageId }
            .let { it ?: throw MessageNotFoundException("message with messageId $messageId not found") }
            .message = message
        return true
    }

    fun deleteMessage(messageId: Int): Boolean {

        return if (messages.removeIf { it.messageId == messageId }) true
        else throw MessageNotFoundException("message with messageId $messageId not found")

    }

    fun deleteChat(chatId: Int): Boolean {
        chats.firstOrNull { it.chatId == chatId } ?: throw ChatNotFoundException("Chat with chatId $chatId not found")
        messages.removeIf { it.chat.chatId == chatId }
        chats.removeIf { it.chatId == chatId }
        return true

    }

    fun readMessage(messageId: Int): Boolean {
        messages.firstOrNull() { it.messageId == messageId }
            ?: throw MessageNotFoundException("message with messageId $messageId not found")
        messages.first { it.messageId == messageId }.isRead = true
        return true
    }


    fun getChats(): Map<Chat, List<Message>> {

        return messages.asSequence().groupBy { it.chat }

    }


    fun getUnreadChatsCount(): Int {

        return messages.asSequence().filter { !it.isRead }.associateBy { it.chat }.size

    }


    fun getChatLastMessages(): Map<Chat, String> {

        return chats.asSequence().associateBy(keySelector = { it }, valueTransform = {
            messages.lastOrNull { message -> message.chat == it }?.message ?: "No Messages"
        })


    }

    fun getMessagesByUserId(userId: Int, quantityOfMessages: Int): List<Message> {
        chats.firstOrNull { it.usersId.contains(userId) }
            ?: throw UserNotFoundException("User with ID $userId not found")

        return messages.asSequence().filter { it.chat.usersId.contains(userId) }.take(quantityOfMessages)
            .onEach { it.isRead = true }.toList()
    }


}