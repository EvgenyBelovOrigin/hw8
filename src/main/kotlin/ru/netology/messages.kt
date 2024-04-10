package ru.netology

data class Chat(val chatId: Int, val ownerId: Int, val peerId: Int)

data class Message(
    val messageId: Int,
    val ownerId: Int,
    val peerId: Int,
    val chatId: Int,
    var message: String,
    var isRead: Boolean,
    val isInbox: Boolean
)

class MessageNotFoundException(message: String) : RuntimeException(message)
class MessageAccessDeniedException(message: String) : RuntimeException(message)
class ChatNotFoundException(message: String) : RuntimeException(message)
class PeerNotFoundException(message: String) : RuntimeException(message)
class CantSendMessageToYourselfException(message: String) : RuntimeException(message)
class CantSendMessageNotToMeException(message: String) : RuntimeException(message)


object ChatService {
    private var messages = mutableListOf<Message>()
    private var chats = mutableListOf<Chat>()
    private var chatLastId = 0
    private var messageLastID = 0
    private var myId = 1


    fun clean() {
        messages = mutableListOf<Message>()
        chats = mutableListOf<Chat>()
        chatLastId = 0
        messageLastID = 0
    }

    fun sendMessage(message: String, tooId: Int, ownerId: Int): Int { //only to me or from me
        if (tooId == ownerId) throw CantSendMessageToYourselfException("You can't send message to yourself")
        if (tooId != 1 && ownerId != 1) throw CantSendMessageNotToMeException(
            "It's impossible to send message not to me or not from me according to conditions of current HW"
        )
        var peerId: Int = tooId
        var isInbox: Boolean = true
        if (tooId == myId) peerId = ownerId else isInbox = false

        messages.add(
            Message(
                ++messageLastID,
                myId,
                peerId,
                if (messages.none { it.peerId == peerId }) {
                    chats.add(
                        Chat(++chatLastId, myId, peerId)
                    )
                    chatLastId

                } else messages.last { it.peerId == peerId }.chatId,
                message,
                myId == ownerId,
                isInbox
            )
        )
        return messageLastID


    }

    fun editMessage(messageId: Int, message: String): Boolean {
        if (messages.none { it.messageId == messageId })
            throw MessageNotFoundException("message with messageId $messageId not found")
        when (messages.find { it.messageId == messageId }?.isInbox) {
            false -> messages.find { it.messageId == messageId }?.message = message
            else -> throw MessageAccessDeniedException("Access to edit message $messageId denied")

        }
        return true
    }

    fun deleteMessage(messageId: Int): Boolean {
        if (messages.none { it.messageId == messageId })
            throw MessageNotFoundException("message with messageId $messageId not found")
        messages.removeIf { it.messageId == messageId }
        return true
    }

    fun deleteChat(chatId: Int): Boolean {
        if (messages.none { it.chatId == chatId })
            throw ChatNotFoundException("Chat with chatId $chatId not found")
        messages.removeIf { it.chatId == chatId }
        chats.removeIf { it.chatId == chatId }
        return true

    }

    fun readMessage(messageId: Int): Boolean {
        if (messages.none { it.messageId == messageId })
            throw MessageNotFoundException("message with messageId $messageId not found")
        messages.find { it.messageId == messageId }?.isRead = true
        return true

    }


    fun getChats(): List<Message> {

        val chatsSortedDescendingByMessageId =
            messages.sortedWith(compareBy<Message> { it.chatId }.thenByDescending { it.messageId })
        val chatList = mutableListOf<Message>()
        var chatsId = chatsSortedDescendingByMessageId.first().chatId

        chatsSortedDescendingByMessageId.forEach {
            if (it.chatId == chatsId) {
                chatList.add(it)
                ++chatsId
            }
        }

        return chatList
    }


    fun getUnreadChatsCount(): Int {
        val setOfChatsWithUnreadMessages = mutableSetOf<Int>()
        messages.forEach { if (it.isRead == false) setOfChatsWithUnreadMessages.add(it.chatId) }
        return setOfChatsWithUnreadMessages.size

    }

    fun getChatLastMessages(chatId: Int): List<Any> {
        if (chats.none { it.chatId == chatId }) throw ChatNotFoundException("Chat with chatId $chatId not found")
        if (messages.none { it.chatId == chatId }) return listOf("No messages")
        return messages.sortedByDescending { it.messageId }.filter { it.chatId == chatId }

    }

    fun getMessagesByPeerId(peerId: Int, quantityOfMessages: Int): List<Message> {
        if (messages.none { it.peerId == peerId }) throw PeerNotFoundException("Peer with peerId $peerId not found")
        val messagesList = mutableListOf<Message>()
        var countMessages = 1
        messages.forEachIndexed() { index, it ->
            if (it.peerId == peerId && countMessages <= quantityOfMessages) {
                messages[index].isRead = true
                messagesList.add(it)
                ++countMessages

            }
        }
        return messagesList.sortedByDescending { it.messageId }
    }

}