package com.app.biblio.service;

import java.util.List;
import java.util.Optional;

import com.app.biblio.bean.Livre;
import com.app.biblio.bean.Notification;
import com.app.biblio.bean.User;


public interface NotificationService {
	Notification createNotification(User user, String message);

	int countUnreadNotifications(User user);

	List<Notification> findByUserOrderByCreatedAtDesc(User user);

	void markAllAsRead(User user);

	boolean messageExistsForUser(User user, String message);

	void deleteById(Long id);

	boolean hasUserBeenNotifiedForBook(User user, Livre livre);

	List<Notification> findUnreadNotifications(User user);

	List<Notification> findReadNotifications(User user);

	List<Notification> findAllReadNotifications();

	List<Notification> findAllUnreadNotifications();

	List<Notification> findAll();

	Optional<Notification> findById(Long notificationId);
	

	
    }