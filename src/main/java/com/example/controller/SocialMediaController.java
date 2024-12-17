package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {
    AccountService accountService;
   MessageService messageService;
    
    @Autowired
    SocialMediaController(AccountService accountService, MessageService messageService){
        this.accountService = accountService;
        this.messageService = messageService;
    }


    /**
     * The registration will be successful if and only if the username is not blank, the password is at least 4 characters long, and an Account with that username does not already exist. 
     * If all these conditions are met, the response body should contain a JSON of the Account, including its accountId. 
     * The response status should be 200 OK, which is the default. The new account should be persisted to the database.
     * If the registration is not successful due to a duplicate username, the response status should be 409.
     * If the registration is not successful for some other reason, the response status should be 400.
     * @param account
     * @return posted account status
     */

    @PostMapping("/register")
    public ResponseEntity<Account> postAccount(@RequestBody Account account){
        if(accountService.checkUser(account) == true){
            return ResponseEntity.status(409).body(null);
        }
        else if(!account.getUsername().isBlank() && account.getPassword().length() >= 4){
            Account nAcc = accountService.addAccount(account);
            return ResponseEntity.status(200).body(nAcc);
        }
        else{
            return ResponseEntity.status(400).body(null);
        }
    }

    /**
     * The login will be successful if and only if the username and password provided in the request body JSON match a real account existing on the database. 
     * If successful, the response body should contain a JSON of the account in the response body, including its accountId. 
     * The response status should be 200 OK, which is the default.
     * @param account
     * @return status OK
     */
    @PostMapping("/login")
    public ResponseEntity<Account> loginAccount(@RequestBody Account account){
        Account nAcc = accountService.getAccount(account);
            if(nAcc != null){
                return ResponseEntity.status(200).body(nAcc);
            }
        return ResponseEntity.status(401).body(null);
    }

    /**
     * The creation of the message will be successful if and only if the messageText is not blank, is not over 255 characters, and postedBy refers to a real, existing user. 
     * If successful, the response body should contain a JSON of the message, including its messageId. 
     * The response status should be 200, which is the default. The new message should be persisted to the database.
     * @param msg
     * @return newly created message
     */
    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message msg){
        if(!msg.getMessageText().isBlank() && msg.getMessageText().length() <= 255 && accountService.getUserById(msg.getPostedBy()) != null){
            return ResponseEntity.status(200).body(messageService.createMessage(msg.getPostedBy(), msg.getMessageText(), msg.getTimePostedEpoch()));
        }
        else{
            return ResponseEntity.status(400).body(null);
        }
        
    }

    /**
     * The response body should contain a JSON representation of a list containing all messages retrieved from the database. 
     * It is expected for the list to simply be empty if there are no messages. 
     * The response status should always be 200, which is the default.
     * @return List of all messages
     */
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages(){
        return ResponseEntity.ok(messageService.getAllMessages());
    }

    /**
     * The response body should contain a JSON representation of the message identified by the messageId. 
     * It is expected for the response body to simply be empty if there is no such message. 
     * The response status should always be 200, which is the default.
     * @param messageId
     * @return message from a user
     */
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageFromUser(@PathVariable int messageId){
        Message store = messageService.getMessageById(messageId);
        if(store != null){
            return ResponseEntity.ok(store);
        }
        return ResponseEntity.ok(store);
    }

    /**
     * The deletion of an existing message should remove an existing message from the database. 
     * If the message existed, the response body should contain the number of rows updated (1). 
     * The response status should be 200, which is the default.
     * If the message did not exist, the response status should be 200, but the response body should be empty. 
     * This is because the DELETE verb is intended to be idempotent, ie, multiple calls to the DELETE endpoint should respond with the same type of response.
     * @param messageId
     * @return
     */
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageFromUser(@PathVariable int messageId){
        Message store = messageService.getMessageById(messageId);
        if(store != null){
            messageService.deleteMessage(store);
            return ResponseEntity.ok(1);
        }
        return ResponseEntity.ok(null);
    }

    /**
     * The update of a message should be successful if and only if the message id already exists and the new messageText is not blank and is not over 255 characters. 
     * If the update is successful, the response body should contain the number of rows updated (1), and the response status should be 200, which is the default. 
     * The message existing on the database should have the updated messageText.
     * @param messageId
     * @param message
     * @return updated message
     */
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> updateMessageFromUser(@PathVariable int messageId, @RequestBody Message message){
        Message msg = messageService.getMessageById(messageId);
        if(msg != null && !message.getMessageText().isBlank() && message.getMessageText().length() <= 255){
            messageService.updateMessageById(msg.getMessageId(), message.getMessageText());
            return ResponseEntity.ok(1);
        } 
        return ResponseEntity.status(400).body(null);
    }
    /**
     * The response body should contain a JSON representation of a list containing all messages posted by a particular user, which is retrieved from the database. 
     * It is expected for the list to simply be empty if there are no messages. 
     * The response status should always be 200, which is the default.
     * @param accountId
     * @return all messages from a specific user
     */
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> retrieveAllMessagesFromUser(@PathVariable int accountId){
        return ResponseEntity.ok(messageService.getAllMessagesFromUserId(accountId));
    }

}
