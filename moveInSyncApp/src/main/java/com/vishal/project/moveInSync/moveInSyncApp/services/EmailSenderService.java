package com.vishal.project.moveInSync.moveInSyncApp.services;

public interface EmailSenderService {
    public void sendEmail(String toEmail,String subject,String body);
    void sendEmails(String toEmail[],String subject,String body);
}
