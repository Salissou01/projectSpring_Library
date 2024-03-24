package com.app.biblio.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.biblio.bean.Notification;
import com.app.biblio.bean.User;
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
	@Query("SELECT COUNT(n) FROM Notification n WHERE n.user = :user AND n.read = false")
    int countByUserAndReadFalse(User user);
	List<Notification> findByUserOrderByCreatedAtDesc(User user);
	@Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.read = true WHERE n.user = :user")
    void markAllAsRead(User user);
	

    @Query("SELECT n FROM Notification n WHERE n.user = :user AND n.read = false ORDER BY n.createdAt DESC")
    List<Notification> findByUserAndReadFalseOrderByCreatedAtDesc(User user);
    
    @Query("SELECT n FROM Notification n WHERE n.user = :user AND n.read = true ORDER BY n.createdAt DESC")
    List<Notification> findByUserAndReadTrueOrderByCreatedAtDesc(User user);
    @Query("SELECT n FROM Notification n WHERE n.read = true ORDER BY n.createdAt DESC")
    List<Notification> findByReadTrueOrderByCreatedAtDesc();
    @Query("SELECT n FROM Notification n WHERE n.read = false ORDER BY n.createdAt DESC")
    List<Notification> findByReadFalseOrderByCreatedAtDesc();

    
}
