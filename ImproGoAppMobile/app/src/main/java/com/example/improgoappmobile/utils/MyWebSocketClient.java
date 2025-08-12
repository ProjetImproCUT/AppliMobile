package com.example.improgoappmobile.utils;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class MyWebSocketClient extends WebSocketClient {

    public interface MessageListener {
        void onMessageReceived(String message);
    }

    // Thread-safe set of listeners
    private final Set<MessageListener> listeners = new CopyOnWriteArraySet<>();

    /** New API: add a listener (call in onStart of your Activity/Fragment) */
    public void addMessageListener(MessageListener listener) {
        if (listener != null) listeners.add(listener);
    }

    /** New API: remove the listener (call in onStop) */
    public void removeMessageListener(MessageListener listener) {
        if (listener != null) listeners.remove(listener);
    }

    /** Optional: clear all listeners (e.g., when logging out) */
    public void clearMessageListeners() {
        listeners.clear();
    }

    /**
     * Backward compatibility with your old code.
     * Prefer add/removeMessageListener instead.
     */
    @Deprecated
    public void setMessageListener(MessageListener listener) {
        listeners.clear();
        if (listener != null) listeners.add(listener);
    }

    public MyWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        // Optionally log/notify
    }

    @Override
    public void onMessage(String message) {
        for (MessageListener l : listeners) {
            try {
                l.onMessageReceived(message);
            } catch (Exception e) {
                // Prevent one bad listener from breaking others
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        // Optionally log/notify
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }
}
