package stubmaker.domain;

import stubmaker.annotation.MakeStub;

@MakeStub
public interface EmailService {
    void send(String fromEmail, String toEmail, String subject, String message);
}
