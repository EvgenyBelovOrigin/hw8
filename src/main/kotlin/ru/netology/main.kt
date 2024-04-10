package ru.netology

fun main() {
    // Send meessage
    println()
    println("Send Message")
    ChatService.sendMessage("Hello", 2, 1)
    ChatService.sendMessage("Hy", 1, 2)
    ChatService.sendMessage("nihao", 3, 1)
    ChatService.sendMessage("hello", 1, 3)
    ChatService.sendMessage("whoman choo chifan ba", 3, 1)
    ChatService.sendMessage("There are you", 4, 1)
    ChatService.sendMessage("I need results", 4, 1)
    ChatService.sendMessage("5 minutes please", 1, 4)

    //Edit message
    println()
    println("Edit message")
    println(ChatService.editMessage(7, "I need results yesterday"))

    //Delete message
    println()
    println("Delete message")
    ChatService.deleteMessage(6)

//    Delete chat
//    println()
//    println("Delete chat")
//    ChatService.deleteChat(1)
//    ChatService.printMessages()

    //Read message
    println()
    println("Read message")
    ChatService.readMessage(4)
    //Get Chats
    println()
    println("Get Chats")
    ChatService.sendMessage("Bye", 1, 2)
//    println(ChatService.getChats().toString())
    println()

    println(ChatService.getChats())

    //Get Unread Chats Cont
    println()
    println("Get Unread Chats Count")
    println(ChatService.getUnreadChatsCount())
    ChatService.readMessage(4)
    println(ChatService.getUnreadChatsCount())
    //Get Chat Last Messages
    println()
    println("Get chats last message")
    ChatService.sendMessage("hello",1,5)
    ChatService.deleteMessage(10)
    println(ChatService.getChatLastMessages(3))
    // get messages by peerId and count
    println()
    println("get messages by peerID and count")
    println()
    println( ChatService.getMessagesByPeerId(2,2))



}


