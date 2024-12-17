package com.example.service;

import java.util.List;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.entity.Message;
import com.example.repository.MessageRepository;


@Service
@Transactional
 public class MessageService {
    MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

    public Message createMessage(int postedBy, String messageText, long timePostedEpoch){
        Message msg = new Message(postedBy, messageText, timePostedEpoch);
        return messageRepository.save(msg);
    }

    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }

    public Message getMessageById(int userId){
        return messageRepository.findByMessageId(userId);
    }

    public void deleteMessage(Message msg){
        messageRepository.delete(msg);
    }

    public Message updateMessageById(int messageId, String messageText){
        Message updatedMessage = messageRepository.getById(messageId);
        updatedMessage.setMessageText(messageText);
        return messageRepository.save(updatedMessage);
    }

    public List<Message> getAllMessagesFromUserId(int id){
        return messageRepository.findByPostedBy(id);
    }
 }
