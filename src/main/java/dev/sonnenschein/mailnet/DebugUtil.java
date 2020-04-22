package dev.sonnenschein.mailnet;

import com.sun.mail.util.BASE64DecoderStream;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DebugUtil {
    public static void debugMessage(MimeMessage m, PrintStream out) throws MessagingException, IOException {
        List.of(m.getAllRecipients()).forEach(r -> out.format("%s (%s)", r.toString(), r.getType()));
        out.println();
        out.println(Arrays.toString(m.getFrom()));
        out.println(m.getSender());
        out.println(m.getSubject());
        out.println("headers:");
        Collections.list(m.getAllHeaders()).forEach(header -> out.format("    %s: %s\n", header.getName(), header.getValue()));
        out.println();

        printDebugContent(m.getContent(), m.getContentType(), m.getFileName(), "base", out);

        out.println();
    }

    private static void printDebugContent(Object content, String contentType, String fileName, String prefix, PrintStream out) throws MessagingException, IOException {
        out.printf("%s (%s) ", contentType, fileName);

        if (content instanceof String) {
            out.format("part %s-string:\n", prefix);
            out.println(content);
        } else if (content instanceof MimeMultipart) {
            MimeMultipart parsed = (MimeMultipart) content;
            out.format("part %s-multipart:\n", prefix);
            for (int i = 0; i < parsed.getCount(); i++) {
                BodyPart part = parsed.getBodyPart(i);
                printDebugContent(part.getContent(), part.getContentType(), part.getFileName(), prefix + "." + i, out);
            }
        } else if (content instanceof BASE64DecoderStream) {
            BASE64DecoderStream parsed = (BASE64DecoderStream) content;
            out.format("part %s-base64:\n", prefix);
        } else {
            out.println("unknown content format: " + content.getClass());
        }
    }
}
