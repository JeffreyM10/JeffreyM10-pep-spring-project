package com.example.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.entity.Message;



@Repository
public interface MessageRepository extends JpaRepository<Message, Integer>{
    //get a message by messageId
    public Message findByMessageId(int messageId);

    //get all of the messages from a specific user (postedBy col)
    public List<Message> findByPostedBy(int postedBy);

}
