package org.github.chatroom.server.session;

public abstract class SessionFactory {

    private static final Session session = new SessionMemoryImpl();

    public static Session getSession() {
        return session;
    }
}
