package ru.netology

data class Chat(val chatId: Int?, val usersId: List<Int>)

data class Message(
    val chatId: Int?,
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
        if (chats.none { it.usersId.containsAll(listOf(fromId, toId)) }) chats.add(
            Chat(
                ++chatLastId, listOf(fromId, toId)
            )
        )

        messages.add(
            Message(
                chatId = chats.find { it.usersId.containsAll(listOf(fromId, toId)) }?.chatId,
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
        if (messages.none { it.messageId == messageId }) throw MessageNotFoundException("message with messageId $messageId not found")
        messages.find { it.messageId == messageId }?.message = message
        return true
    }

    fun deleteMessage(messageId: Int): Boolean {
        if (messages.none { it.messageId == messageId }) throw MessageNotFoundException("message with messageId $messageId not found")
        messages.removeIf { it.messageId == messageId }
        return true
    }

    fun deleteChat(chatId: Int): Boolean {
        if (messages.none { it.chatId == chatId }) throw ChatNotFoundException("Chat with chatId $chatId not found")
        messages.removeIf { it.chatId == chatId }
        chats.removeIf { it.chatId == chatId }
        return true

    }

    fun readMessage(messageId: Int): Boolean {
        if (messages.none { it.messageId == messageId }) throw MessageNotFoundException("message with messageId $messageId not found")
        messages.find { it.messageId == messageId }?.isRead = true
        return true

    }


    fun getChats(userId: Int): Map<Chat, List<Message>> {
        if (chats.firstOrNull { it.usersId.contains(userId) } == null && userId > 0)
            throw UserNotFoundException("User with ID $userId not found")

        val messagesFilteredByUserId = if (userId > 0) messages.filter { message ->
            message.chatId in chats.filter { it.usersId.contains(userId) }.groupBy { it.chatId }.keys
        }.toMutableList() else messages


        return messagesFilteredByUserId.sortedWith(compareBy<Message> { it.chatId }.thenByDescending { it.messageId })
            .groupBy { Chat(it.chatId, chats.first { chat -> chat.chatId == it.chatId }.usersId) }

    }


//    fun getUnreadChatsCount(): Int {
//        val setOfChatsWithUnreadMessages = mutableSetOf<Int>()
//        messages.forEach { if (it.isRead == false) setOfChatsWithUnreadMessages.add(it.chatId) }
//        return setOfChatsWithUnreadMessages.size
//
//    }

//    fun getChatLastMessages(chatId: Int): List<Any> {
//        if (chats.none { it.chatId == chatId }) throw ChatNotFoundException("Chat with chatId $chatId not found")
//        if (messages.none { it.chatId == chatId }) return listOf("No messages")
//        return messages.sortedByDescending { it.messageId }.filter { it.chatId == chatId }
//
//    }

//    fun getMessagesByPeerId(peerId: Int, quantityOfMessages: Int): List<Message> {
//        if (messages.none { it.tooId == peerId }) throw PeerNotFoundException("Peer with peerId $peerId not found")
//        val messagesList = mutableListOf<Message>()
//        var countMessages = 1
//        messages.forEachIndexed() { index, it ->
//            if (it.tooId == peerId && countMessages <= quantityOfMessages) {
//                messages[index].isRead = true
//                messagesList.add(it)
//                ++countMessages
//
//            }
//        }
//        return messagesList.sortedByDescending { it.messageId }
//    }

    fun print() {
        messages.forEach { (println(it)) }
        println()
        chats.forEach { println(it) }
    }

}