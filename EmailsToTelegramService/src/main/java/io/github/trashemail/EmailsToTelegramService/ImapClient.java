package io.github.trashemail.EmailsToTelegramService;


import io.github.trashemail.EmailsToTelegramService.Configuration.ImapClientServiceConfig;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;
import com.sun.mail.imap.protocol.IMAPProtocol;
import com.sun.mail.iap.ProtocolException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.mail.*;
import javax.mail.event.MessageCountAdapter;
import javax.mail.event.MessageCountEvent;
import java.util.Properties;

@Configuration
@Component
@EnableAsync
public class ImapClient {

    @Autowired
    ImapClientServiceConfig imapClientServiceConfig;
    @Autowired
    ForwardMailsToTelegram forwardMailsToTelegram;

    private static final Logger log = LoggerFactory.getLogger(ImapClient.class);

    private static String username;
    private static String password;
    private static String imapHost;
    private static String imapPort;

    @PostConstruct
    public void init() {
        username = imapClientServiceConfig.getImap().getEmail();
        password = imapClientServiceConfig.getImap().getPassword();
        imapHost = imapClientServiceConfig.getImap().getHost();
        imapPort = imapClientServiceConfig.getImap().getPort();
    }

    @Async("threadPoolTaskExecutor")
    public void fetchNewEmails() throws Exception {
        Properties mailProps = new Properties();
        mailProps.put("mail.store.protocol", "imaps");
        mailProps.put("mail.imaps.host", imapHost);
        mailProps.put("mail.imaps.port", imapPort);
        mailProps.put("mail.imaps.timeout", "10000");
        mailProps.put("mail.imaps.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(mailProps);
        IMAPStore store = null;
        Folder inbox = null;

        try {
            store = (IMAPStore) session.getStore("imaps");
            store.connect(username, password);

            if (!store.hasCapability("IDLE")) {
                throw new RuntimeException("IDLE not supported");
            }

            inbox = store.getFolder("INBOX");
            inbox.addMessageCountListener(new MessageCountAdapter() {

                @Override
                public void messagesAdded(MessageCountEvent event) {
                    Message[] messages = event.getMessages();

                    for (Message message : messages) {
                        try {
                            forwardMailsToTelegram.sendToTelegram(message);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            Thread idleThread = new Thread(new KeepAliveRunnable((IMAPFolder) inbox));

            idleThread.start();
            while (!Thread.interrupted()) {
                try {
                    ensureOpen(inbox);
                    log.info("IMAP client: IDLE Listening state ...");
                    ((IMAPFolder) inbox).idle();
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e1) {
                    }
                }
            }

            if (idleThread.isAlive()) {
                idleThread.interrupt();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(inbox);
            close(store);
        }
    }

    private static class KeepAliveRunnable implements Runnable {

        private static final long KEEP_ALIVE_FREQ = 240000; // 4 minutes

        private IMAPFolder folder;

        public KeepAliveRunnable(IMAPFolder folder) {
            this.folder = folder;
        }

        @Override
        public void run() {
            while (!Thread.interrupted()) {
                try {
                    Thread.sleep(KEEP_ALIVE_FREQ);

                    // Perform a NOOP just to keep alive the connection
                    log.debug("Performing a NOOP to keep alive the connection");
                    folder.doCommand(new IMAPFolder.ProtocolCommand() {
                        public Object doCommand(IMAPProtocol protocol)
                                throws ProtocolException {
                            protocol.simpleCommand("NOOP", null);
                            return null;
                        }
                    });
                } catch (InterruptedException e) {
                } catch (MessagingException e) {
                    log.warn("Unexpected exception while keeping alive the IDLE connection", e);
                }
            }
        }
    }

    public static void close(final Folder folder) {
        try {
            if (folder != null && folder.isOpen()) {
                folder.close(false);
            }
        } catch (final Exception e) {
        }

    }

    public static void close(final Store store) {
        try {
            if (store != null && store.isConnected()) {
                store.close();
            }
        } catch (final Exception e) {
        }

    }

    public static void ensureOpen(final Folder folder)
            throws MessagingException {

        if (folder != null) {
            Store store = folder.getStore();
            if (store != null && !store.isConnected()) {
                store.connect(username, password);
            }
        } else {
            throw new MessagingException("Unable to open a null folder");
        }

        if (folder.exists() && !folder.isOpen() && (folder.getType() &
                Folder.HOLDS_MESSAGES) != 0) {
            log.info("Opening folder " + folder.getFullName());
            folder.open(Folder.READ_ONLY);
            if (!folder.isOpen())
                throw new MessagingException("Unable to open folder " +
                        folder.getFullName());
        }

    }
}
