package fakegen.usage;

import fakegen.annotation.ImplementStub;

@ImplementStub
public interface EmailService {
    void send(String fromEmail, String toEmail, String subject, String message);
}
