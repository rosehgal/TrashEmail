package io.github.teledot.SmtpServer;

import io.github.teledot.EmailInteraction.FetchEmailAndSendTelegramMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.subethamail.smtp.helper.SimpleMessageListenerAdapter;
import org.subethamail.smtp.server.SMTPServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class SMTPServerService {
    private static final Logger log = LoggerFactory.getLogger(FetchEmailAndSendTelegramMessages.class);
    @Value("${smtpserver.enabled}")
    String enabled="true";

    @Value("${smtpserver.hostName}")
    String hostName="localhost";

    @Value("${smtpserver.port}")
    String port="4225";

    SMTPServer smtpServer;
    public SMTPServerService() {
    }

    @PostConstruct
    public void start() {
        if(enabled.equalsIgnoreCase("true")){
            SimpleMessageListenerImpl l = new SimpleMessageListenerImpl();
            smtpServer = new SMTPServer(new SimpleMessageListenerAdapter(l));
            smtpServer.setHostName(this.hostName);
            smtpServer.setPort(Integer.valueOf(port));
            smtpServer.start();
            System.out.println("****** SMTP Server is running for domain "+smtpServer.getHostName()+" on port "+smtpServer.getPort());
        } else {
            System.out.println("****** SMTP Server NOT ENABLED by settings ");
        }
    }
    @PreDestroy
    public void stop() {
        if(enabled.equalsIgnoreCase("true")){
            System.out.println("****** Stopping SMTP Server for domain "+smtpServer.getHostName()+" on port "+smtpServer.getPort());
            smtpServer.stop();
        }
    }
    public boolean isRunning() {
        return smtpServer.isRunning();
    }
}
