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

    @Test(expected = CantSendMessageNotToMeException::class)
    fun sendMessage_shouldThrowCantSendMessageNotToMeException() {
        ChatService.sendMessage("some", 2, 3)

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
        val expected = listOf(Message(1, 1, 3, 1, "some", false, true))
        assertEquals(expected, result)
    }

    @Test(expected = MessageNotFoundException::class)
    fun editMessage_shouldThrowMessageNotFoundException() {
        ChatService.sendMessage("some", 1, 3)
        ChatService.editMessage(2, "where")

    }

    @Test(expected = MessageAccessDeniedException::class)
    fun editMessage_shouldThrowMessageAccessDeniedException() {
        ChatService.sendMessage("some", 1, 2)
        ChatService.editMessage(1, "where")

    }

    @Test
    fun editMessage_checkReturn() {
        ChatService.sendMessage("Yes", 2, 1)
        val result = ChatService.editMessage(1, "No")
        assertEquals(true, result)
    }

    @Test
    fun editMessage_checkResult() {
        ChatService.sendMessage("Yes", 2, 1)
        ChatService.editMessage(1, "No")
        val result = ChatService.getChats()
        val expected = listOf(Message(1, 1, 2, 1, "No", true, false))
        assertEquals(expected, result)

    }

    @Test(expected = MessageNotFoundException::class)
    fun deleteMessage_shouldThrowMessageNotFoundException() {
        ChatService.sendMessage("yes", 1, 3)
        ChatService.sendMessage("no", 1, 3)
        ChatService.deleteMessage(3)

    }

    @Test
    fun deleteMessage_checkReturn() {
        ChatService.sendMessage("yes", 1, 3)
        ChatService.sendMessage("no", 1, 3)
        val result = ChatService.deleteMessage(2)
        assertEquals(true, result)
    }

    @Test
    fun deleteMessage_checkResult() {
        ChatService.sendMessage("yes", 1, 3)
        ChatService.sendMessage("no", 1, 3)
        ChatService.deleteMessage(2)
        val result = ChatService.getChats()
        val expected = listOf(Message(1, 1, 3, 1, "yes", false, true))

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
        val expected = listOf(Message(1, 1, 3, 1, "yes", false, true))
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
        val expected = listOf(Message(1, 1, 3, 1, "yes", true, true))
        assertEquals(expected, result)

    }

    @Test
    fun getChats_checkReturn() {
        ChatService.sendMessage("no", 1, 2)
        val result = ChatService.getChats()
        val expected = listOf(Message(1, 1, 2, 1, "no", false, true))
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

    @Test(expected = ChatNotFoundException::class)
    fun getChatLastMessages_shouldThrowChatNotFoundException() {
        ChatService.sendMessage("yes", 1, 3)
        ChatService.sendMessage("no", 1, 4)
        ChatService.getChatLastMessages(3)
    }

    @Test
    fun getChatLastMessages_checkNoMessages() {
        ChatService.sendMessage("hello", 1, 2)
        ChatService.deleteMessage(1)
        val result = ChatService.getChatLastMessages(1)
        val expected = listOf("No messages")
        assertEquals(expected, result)
    }

    @Test
    fun getChatLastMessages_checkReturn() {
        ChatService.sendMessage("hello", 1, 2)
        ChatService.sendMessage("hell", 1, 2)
        val result = ChatService.getChatLastMessages(1)
        val expected = listOf(
            Message(2, 1, 2, 1, "hell", false, true),
            Message(1, 1, 2, 1, "hello", false, true)
        )
        assertEquals(expected, result)
    }

    @Test(expected = PeerNotFoundException::class)
    fun getMessagesByPeerId_shouldThrowPeerNotFoundException() {
        ChatService.sendMessage("yes", 1, 2)
        ChatService.sendMessage("no", 1, 3)
        ChatService.getMessagesByPeerId(4, 1)
    }


    @Test
    fun getMessagesByPeerId_checkReturn() {
        ChatService.sendMessage("hello", 1, 2)
        ChatService.sendMessage("hell", 1, 2)
        ChatService.sendMessage("Bye", 1, 2)
        val result = ChatService.getMessagesByPeerId(2, 2)
        val expected = listOf(
            Message(2, 1, 2, 1, "hell", true, true),
            Message(1, 1, 2, 1, "hello", true, true)
        )
        assertEquals(expected, result)

    }
}