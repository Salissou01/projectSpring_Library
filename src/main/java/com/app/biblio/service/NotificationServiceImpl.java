package com.app.biblio.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.biblio.bean.Livre;
import com.app.biblio.bean.Notification;
import com.app.biblio.bean.User;
import com.app.biblio.repository.NotificationRepository;


@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public Notification createNotification(User user, String message) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        return notificationRepository.save(notification);
    }

    @Override
    public int countUnreadNotifications(User user) {
        return notificationRepository.countByUserAndReadFalse(user);
    }

    @Override
    public List<Notification> findByUserOrderByCreatedAtDesc(User user) {
        return notificationRepository.findByUserOrderByCreatedAtDesc(user);
    }

    @Override
    public void markAllAsRead(User user) {
        notificationRepository.markAllAsRead(user);
    }

  @Override
    public boolean hasUserBeenNotifiedForBook(User user, Livre livre) {
        String message = "Le livre " + livre.getTitle() + " est maintenant disponible pour retrait.";
        return messageExistsForUser(user, message);
    }

    @Override
    public boolean messageExistsForUser(User user, String message) {
        List<Notification> notifications = notificationRepository.findByUserOrderByCreatedAtDesc(user);
        for (Notification notification : notifications) {
            if (notification.getMessage().equals(message)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void deleteById(Long id) {
        notificationRepository.deleteById(id);
    }
    
    @Override
    public List<Notification> findUnreadNotifications(User user) {
        return notificationRepository.findByUserAndReadFalseOrderByCreatedAtDesc(user);
    }
    @Override
    public List<Notification> findReadNotifications(User user) {
        return notificationRepository.findByUserAndReadTrueOrderByCreatedAtDesc(user);
    }
    @Override
    public List<Notification> findAllReadNotifications() {
        return notificationRepository.findByReadTrueOrderByCreatedAtDesc();
    }
@Override
public List<Notification> findAllUnreadNotifications() {
    return notificationRepository.findByReadFalseOrderByCreatedAtDesc();
}
@Override
public List<Notification> findAll() {
    return notificationRepository.findAll();
}

@Override
public Optional<Notification> findById(Long notificationId) {

	return notificationRepository.findById(notificationId);
}



}

