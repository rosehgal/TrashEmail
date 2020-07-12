package io.github.trashemail.EmailsToTelegramService;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;
import com.sun.mail.imap.protocol.IMAPProtocol;
import com.sun.mail.iap.ProtocolException;

import io.github.trashemail.EmailsToTelegramService.context.SpringContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.mail.*;
import javax.mail.event.MessageCountAdapter;
import javax.mail.event.MessageCountEvent;
import java.util.Properties;
@EnableAsync
public class ImapClient {
    private static final Logger log = LoggerFactory.getLogger(ImapClient.class);

    private String username;
    private String password;
    private String imapHost;
    private String imapPort;
    private ForwardMailsToTelegram forwardMailsToTelegram;

    public ImapClient(
            String imapHost,
            String imapPort,
            String username,
            String password) {

        this.username = username;
        this.password = password;
        this.imapHost = imapHost;
        this.imapPort = imapPort;
        this.forwardMailsToTelegram =
                SpringContext.getBean(ForwardMailsToTelegram.class);
    }

    @Async("threadPooltaskExecutor")
    public void fetchNewEmails()
    throws Exception {

        Properties mailProps = new Properties();
        mailProps.put("mail.store.protocol", "imaps");
        mailProps.put("mail.imaps.host", this.imapHost);
        mailProps.put("mail.imaps.port", this.imapPort);
        mailProps.put("mail.imaps.timeout", "10000");
        mailProps.put("mail.imaps.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(mailProps);
        IMAPStore store = null;
        Folder inbox = null;

        try {
            store = (IMAPStore) session.getStore("imaps");
            store.connect(this.username, this.password);

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
                    log.info(
                        String.format("IMAP client: Connecting %s IDLE...",
                                      this.username)
                    );
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

    private class KeepAliveRunnable implements Runnable {

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
                    log.debug(
                        String.format("NOOP connecting to %s",
                                      username)
                    );
                    folder.doCommand(new IMAPFolder.ProtocolCommand() {
                        public Object doCommand(IMAPProtocol protocol)
                                throws ProtocolException {
                            protocol.simpleCommand("NOOP", null);
                            return null;
                        }
                    });
                } catch (InterruptedException e) {
                } catch (MessagingException e) {
                    log.warn("Unexpected exception while keeping " +
                                     "alive the IDLE connection", e);
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

    public void close(final Store store) {
        try {
            if (store != null && store.isConnected()) {
                store.close();
            }
        } catch (final Exception e) {
        }

    }

    public void ensureOpen(final Folder folder)
            throws MessagingException {

        if (folder != null) {
            Store store = folder.getStore();
            if (store != null && !store.isConnected()) {
                store.connect(this.username, this.password);
            }
        } else {
            throw new MessagingException("Unable to open a null folder");
        }

        if (folder.exists() && !folder.isOpen() && (folder.getType() &
                Folder.HOLDS_MESSAGES) != 0) {
            log.info("Opening folder " + folder.getFullName() +" for user "
                             + username);
            folder.open(Folder.READ_ONLY);
            if (!folder.isOpen())
                throw new MessagingException("Unable to open folder " +
                        folder.getFullName());
        }

    }
}
