package com.asafalima.websocket.endpoints.data;

import static com.asafalima.websocket.endpoints.data.ServerMessage.TYPE.*;

public class ServerMessage {


    public enum TYPE {
        BC, INFO, DM, ST
    }

    public final TYPE type;
    public final Object content;
    public final URef uref;

    public ServerMessage(TYPE type, Object content) {
        this(type, content, null);
    }

    public ServerMessage(TYPE type, Object content, URef uref) {
        this.type = type;
        this.content = content;
        this.uref = uref;
    }
    public static ServerMessage bc(URef uref,Object content) {
        return new ServerMessage( BC, content, uref);
    }
    public static ServerMessage info(Object content) {
        return new ServerMessage( INFO, content);
    }
    public static ServerMessage dm(URef uref, Object content) {
        return new ServerMessage( DM, content, uref);
    }

    public static class URef {
        public final String ref;
        public final String caption;

        public URef(String ref, String caption) {
            this.ref = ref;
            this.caption = caption;
        }

    }
}
