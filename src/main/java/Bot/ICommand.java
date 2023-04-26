package Bot;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;


public interface ICommand {

    String getName();

    String GetDescription();

    void execute(MessageReceivedEvent event);

    void execute(ButtonInteractionEvent event);
}
