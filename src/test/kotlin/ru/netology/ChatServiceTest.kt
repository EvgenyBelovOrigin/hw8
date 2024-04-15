package ru.netology

import org.junit.Assert.assertEquals
import org.junit.Test

import org.junit.Before

class ChatServiceTest {
    @Before
    fun clearBeforeTest() {
        ChatService.clean()
    }

    @Test(expected = CantSendMessageToYourselfException::class)
    fun sendMessage_shouldThrowCantSendMessageToYourselfException() {
        ChatService.sendMessage("some", 2, 2)

    }


    @Test
    fun sendMessage_checkReturn() {
        val result = ChatService.sendMessage("some", 1, 3)
        assertEquals(1, result)
    }

    @Test
    fun sendMessage_checkResult() {
        ChatService.sendMessage("some", 1, 3)
        val result = ChatService.getChats()
        val expected = mapOf(
            Chat(1, listOf(1, 3)) to
                    listOf(
                        Message(
                            Chat(1, listOf(1, 3)), 1, 1, 3, "some", false
                        )
                    )
        )
        assertEquals(expected, result)
    }

    @Test(expected = MessageNotFoundException::class)
    fun editMessage_shouldThrowMessageNotFoundException() {
        ChatService.sendMessage("some", 1, 3)
        ChatService.editMessage(2, "where")

    }


    @Test
    fun editMessage_checkResult() {
        ChatService.sendMessage("Yes", 2, 1)
        ChatService.editMessage(1, "No")
        val result = ChatService.getChats()
        val expected = mapOf(
            Chat(1, listOf(2, 1)) to
                    listOf(
                        Message(
                            Chat(1, listOf(2, 1)), 1, 2, 1, "No", false
                        )
                    )
        )
        assertEquals(expected, result)

    }

    @Test(expected = MessageNotFoundException::class)
    fun deleteMessage_shouldThrowMessageNotFoundException() {
        ChatService.sendMessage("yes", 1, 3)
        ChatService.sendMessage("no", 1, 3)
        ChatService.deleteMessage(3)

    }


    @Test
    fun deleteMessage_checkResult() {
        ChatService.sendMessage("yes", 1, 3)
        ChatService.sendMessage("no", 1, 3)
        ChatService.deleteMessage(2)
        val result = ChatService.getChats()
        val expected = mapOf(
            Chat(1, listOf(1, 3)) to
                    listOf(
                        Message(
                            Chat(1, listOf(1, 3)), 1, 1, 3, "yes", false
                        )
                    )
        )

        assertEquals(expected, result)
    }

    @Test(expected = ChatNotFoundException::class)
    fun deleteChat_shouldThrowChatNotFoundException() {
        ChatService.sendMessage("yes", 1, 3)
        ChatService.sendMessage("no", 1, 2)
        ChatService.deleteChat(3)

    }

    @Test
    fun deleteChat_checkReturn() {
        ChatService.sendMessage("yes", 1, 3)
        ChatService.sendMessage("no", 1, 2)
        val result = ChatService.deleteChat(2)
        assertEquals(true, result)
    }

    @Test
    fun deleteChat_checkResult() {
        ChatService.sendMessage("yes", 1, 3)
        ChatService.sendMessage("no", 1, 2)
        ChatService.deleteChat(2)
        val result = ChatService.getChats()
        val expected = mapOf(
            Chat(1, listOf(1, 3)) to
                    listOf(
                        Message(
                            Chat(1, listOf(1, 3)), 1, 1, 3, "yes", false
                        )
                    )
        )
        assertEquals(expected, result)
    }

    @Test(expected = MessageNotFoundException::class)
    fun readMessage_shouldThrowMessageNotFoundException() {
        ChatService.sendMessage("yes", 1, 3)
        ChatService.sendMessage("no", 1, 3)
        ChatService.readMessage(3)

    }

    @Test
    fun readMessage_checkReturn() {
        ChatService.sendMessage("yes", 1, 3)
        val result = ChatService.readMessage(1)
        assertEquals(true, result)

    }

    @Test
    fun readMessage_checkResult() {
        ChatService.sendMessage("yes", 1, 3)
        ChatService.readMessage(1)
        val result = ChatService.getChats()
        val expected = mapOf(
            Chat(1, listOf(1, 3)) to
                    listOf(
                        Message(
                            Chat(1, listOf(1, 3)), 1, 1, 3, "yes", true
                        )
                    )
        )
        assertEquals(expected, result)

    }

    @Test
    fun getChats_checkReturn() {
        ChatService.sendMessage("no", 1, 2)
        val result = ChatService.getChats()
        val expected = mapOf(
            Chat(1, listOf(1, 2)) to
                    listOf(
                        Message(
                            Chat(1, listOf(1, 2)), 1, 1, 2, "no", false
                        )
                    )
        )
        assertEquals(expected, result)
    }

    @Test
    fun getUnreadChatsCount_checkReturn() {
        ChatService.sendMessage("hello", 1, 2)
        ChatService.sendMessage("Bye", 1, 3)
        ChatService.sendMessage("Bye", 1, 4)
        ChatService.readMessage(3)
        val result = ChatService.getUnreadChatsCount()
        assertEquals(2, result)
    }


    @Test
    fun getChatLastMessages_checkReturn() {
        ChatService.sendMessage("yes", 1, 2)
        ChatService.sendMessage("no", 2, 1)
        ChatService.sendMessage("no", 3, 2)
        ChatService.deleteMessage(3)
        val result = ChatService.getChatLastMessages()
        val expected = mapOf(Chat(1, listOf(1, 2)) to "no", Chat(2, listOf(3, 2)) to "No Messages")

        assertEquals(expected, result)
    }

    @Test(expected = UserNotFoundException::class)
    fun getMessagesByUserId_shouldThrowPeerNotFoundException() {
        ChatService.sendMessage("yes", 1, 2)
        ChatService.sendMessage("no", 1, 3)
        ChatService.getMessagesByUserId(4, 2)
    }

    @Test
    fun getMessagesByUserId_checkReturn() {
        ChatService.sendMessage("hello", 1, 2)
        ChatService.sendMessage("hi", 1, 3)
        ChatService.sendMessage("Bye", 5, 2)
        ChatService.sendMessage("ok", 2, 7)

        val result = ChatService.getMessagesByUserId(2, 2)
        val expected = listOf(
            Message(Chat(1, listOf(1, 2)), 1, 1, 2, "hello", true),
            Message(Chat(3, listOf(5, 2)), 3, 5, 2, "Bye", true)
        )

        assertEquals(expected, result)

    }
}