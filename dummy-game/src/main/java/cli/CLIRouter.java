package cli;

import ch.taburett.jass.game.ChatMessage;
import ch.taburett.jass.game.IMessageRouter;
import ch.taburett.jass.game.spi.messages.IJassMessage;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.io.PrintStream;
import java.util.function.Consumer;

public class CLIRouter implements IMessageRouter {
    private final PrintStream out;
    private final Terminal terminal;

    public CLIRouter() throws IOException {
        this.out = System.out;
        terminal = TerminalBuilder.builder().build();
    }

    void message(IJassMessage message) {
        if( message instanceof ChatMessage m)
        {
            chat_print(m);
        }

    }

    private void chat_print(ChatMessage m) {
        terminal.writer().print(m.getFrom());
        terminal.writer().print(": ");
        terminal.writer().println(m.getPayload());
    }

    @Override
    public void register(Consumer<IJassMessage> consumer) {

    }

    @Override
    public void send(IJassMessage message) {
        message(message);
    }
}
