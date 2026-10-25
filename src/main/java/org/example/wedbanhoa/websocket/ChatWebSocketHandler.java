package org.example.wedbanhoa.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.wedbanhoa.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final List<WebSocketSession> adminSessions = new CopyOnWriteArrayList<>();
    private final Map<Integer, List<WebSocketSession>> clientSessions = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ChatService chatService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Map<String, String> params = getQueryParams(session.getUri());
        String role = params.getOrDefault("role", "client");
        String clientIdStr = params.get("clientId");

        if ("admin".equalsIgnoreCase(role)) {
            adminSessions.add(session);
        } else if (clientIdStr != null) {
            try {
                int cId = Integer.parseInt(clientIdStr);
                clientSessions.computeIfAbsent(cId, k -> new CopyOnWriteArrayList<>()).add(session);
            } catch (NumberFormatException ignored) {}
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        JsonNode json = objectMapper.readTree(payload);

        String senderRole = json.has("senderRole") ? json.get("senderRole").asText() : "client";
        Integer clientId = json.has("clientId") ? json.get("clientId").asInt() : null;
        String text = json.has("msg") ? json.get("msg").asText() : (json.has("text") ? json.get("text").asText() : "");

        if ("client".equalsIgnoreCase(senderRole)) {
            // Lưu tin nhắn vào DB
            chatService.saveMessage(clientId, null, "client", text);

            // Gửi tin nhắn tới tất cả Admin đang online
            Map<String, Object> adminMsg = new HashMap<>();
            adminMsg.put("clientId", clientId);
            adminMsg.put("senderRole", "client");
            adminMsg.put("msg", text);
            adminMsg.put("text", text);
            String outJson = objectMapper.writeValueAsString(adminMsg);
            for (WebSocketSession s : adminSessions) {
                if (s.isOpen()) {
                    s.sendMessage(new TextMessage(outJson));
                }
            }
        } else {
            // Admin gửi cho client
            chatService.saveMessage(clientId, null, "admin", text);

            Map<String, Object> clientMsg = new HashMap<>();
            clientMsg.put("senderRole", "admin");
            clientMsg.put("text", text);
            clientMsg.put("msg", text);
            String outJson = objectMapper.writeValueAsString(clientMsg);

            // Gửi tới client tương ứng
            if (clientId != null && clientSessions.containsKey(clientId)) {
                for (WebSocketSession s : clientSessions.get(clientId)) {
                    if (s.isOpen()) {
                        s.sendMessage(new TextMessage(outJson));
                    }
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        adminSessions.remove(session);
        for (List<WebSocketSession> list : clientSessions.values()) {
            list.remove(session);
        }
    }

    private Map<String, String> getQueryParams(URI uri) {
        Map<String, String> map = new HashMap<>();
        if (uri == null || uri.getQuery() == null) return map;
        String query = uri.getQuery();
        for (String param : query.split("&")) {
            String[] pair = param.split("=");
            if (pair.length > 1) {
                map.put(pair[0], pair[1]);
            }
        }
        return map;
    }
}
