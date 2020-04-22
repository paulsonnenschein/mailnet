package dev.sonnenschein.mailnet;

import com.google.gson.*;
import com.sun.mail.util.BASE64DecoderStream;

import javax.mail.Address;
import javax.mail.Header;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.internet.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Base64;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;

import static javax.mail.Message.RecipientType.*;

public class MessageSerializer implements JsonSerializer<MimeMessage> {
    @Override
    public JsonElement serialize(MimeMessage src, Type typeOfSrc, JsonSerializationContext context) {
        var obj = new JsonObject();

        try {
            obj.addProperty("id", src.getMessageID());
            obj.add("from", serializeAddress(src.getFrom()));
            obj.add("to", serializeAddress(src.getRecipients(TO)));
            obj.add("cc", serializeAddress(src.getRecipients(CC)));
            obj.add("bcc", serializeAddress(src.getRecipients(BCC)));
            obj.add("replyTo", serializeAddress(src.getReplyTo()));
            obj.addProperty("subject", src.getSubject());
            obj.add("sendDate", context.serialize(src.getSentDate()));
            obj.add("receivedDate", context.serialize(src.getReceivedDate()));
            obj.add("allHeaders", serializeHeaders(src.getAllHeaders(), context));

            obj.add("content", serializeContent(src));

        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }

        return obj;
    }

    public JsonArray serializeAddress(Address... a) {
        if (a == null) return new JsonArray();
        JsonArray array = new JsonArray(a.length);

        for (var ad : a) {
            JsonObject object = new JsonObject();
            if (ad instanceof InternetAddress) {
                object.addProperty("name", ((InternetAddress) ad).getPersonal());
                object.addProperty("email", ((InternetAddress) ad).getAddress());
            } else {
                object.addProperty("email", ad.toString());
            }
            array.add(object);
        }

        return array;
    }

    public JsonArray serializeHeaders(Enumeration<Header> hs, JsonSerializationContext context) {
        JsonArray array = new JsonArray();
        if (hs == null) return array;


        for (var h : Collections.list(hs)) {
            array.add(context.serialize(h));
        }

        return array;
    }

    public JsonElement serializeContent(Part part) throws MessagingException, IOException {
        JsonObject object = new JsonObject();
        object.add("contentType", serializeContentType(part.getContentType()));
        object.addProperty("filename", part.getFileName());
        var content = part.getContent();
        if (content instanceof String) {
            object.addProperty("content", (String) content);
        } else if (content instanceof MimeMultipart) {
            var multi = (MimeMultipart) content;
            JsonArray parts = new JsonArray(multi.getCount());
            for (int i = 0; i < multi.getCount(); i++) {
                parts.add(serializeContent(multi.getBodyPart(i)));
            }
            object.add("content", parts);
        } else if (content instanceof BASE64DecoderStream) {
            String base64 = Base64.getEncoder().encodeToString(((BASE64DecoderStream) content).readAllBytes());
            object.addProperty("content", base64);
        } else {
            object.add("content", null);
            object.addProperty("debugClassType", content.getClass().toString());
        }
        return object;
    }

    public JsonElement serializeContentType(String contentType) throws ParseException {
        ContentType parsed = new ContentType(contentType);
        JsonObject object = new JsonObject();
        JsonObject params = new JsonObject();

        object.addProperty("type", parsed.getBaseType());

        if (parsed.getParameterList() != null) {
            for (var name : Collections.list(parsed.getParameterList().getNames())) {
                params.addProperty(name, parsed.getParameter(name));
            }
        }

        object.add("params", params);

        return object;
    }
}
