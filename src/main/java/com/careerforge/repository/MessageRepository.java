package com.careerforge.repository;

import com.careerforge.model.Message;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    // Chat between two users
    List<Message> findBySenderEmailAndReceiverEmailOrReceiverEmailAndSenderEmailOrderByTimeAsc(
        String sender1, String receiver1,
        String sender2, String receiver2
    );
    @Query("""
    SELECT DISTINCT 
    CASE 
        WHEN m.senderEmail = :email THEN m.receiverEmail 
        ELSE m.senderEmail 
    END
    FROM Message m
    WHERE m.senderEmail = :email OR m.receiverEmail = :email
""")
List<String> findDistinctUsers(@Param("email") String email);

@Transactional
@Modifying
@Query("UPDATE Message m SET m.seen = true " +
       "WHERE m.senderEmail = :sender AND m.receiverEmail = :receiver AND m.seen = false")
void markMessagesAsSeen(@Param("sender") String sender,
                        @Param("receiver") String receiver);

                        @Query("""
SELECT m.senderEmail, COUNT(m)
FROM Message m
WHERE m.receiverEmail = :email AND m.seen = false
GROUP BY m.senderEmail
""")
List<Object[]> getUnreadCounts(@Param("email") String email);

}
