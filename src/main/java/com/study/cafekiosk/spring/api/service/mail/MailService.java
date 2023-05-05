package com.study.cafekiosk.spring.api.service.mail;

import com.study.cafekiosk.spring.client.mail.MailSendClient;
import com.study.cafekiosk.spring.domain.history.MailSendHistory;
import com.study.cafekiosk.spring.domain.history.MailSendHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final MailSendClient mailSendClient;
    private final MailSendHistoryRepository mailSendHistoryRepository;

    public boolean sendMail(String fromEmail, String toEmail, String subject, String content) {
        boolean result = mailSendClient.sendMail(fromEmail, toEmail, subject, content);
        if (result) {
            mailSendHistoryRepository.save(MailSendHistory.builder()
                    .fromEmail(fromEmail)
                    .toEmail(toEmail)
                    .subject(subject)
                    .content(content)
                    .build());
            return true;
        }
        return false;
    }
}
