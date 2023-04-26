package Bot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CommandManager  extends ListenerAdapter {

    private List<ICommand> commands= new ArrayList<>();

    public void add(ICommand command){
        commands.add(command);
    }

    @Override
    public void onReady(ReadyEvent event) {
        System.out.println("Bot ready!");
        for(var guild:event.getJDA().getGuilds())
        {
            for(var command:commands)
                guild.upsertCommand(command.getName(),command.GetDescription()).queue();

        }

    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        for (var command: commands) {
            if (command.getName().equals(event.getButton().getId())){
                command.execute(event);
                var msg=event.reply("Command "+command.getName()+" executed.").complete();
                ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
                executor.schedule(() -> msg.deleteOriginal().queue(), 5, TimeUnit.SECONDS);
                return;
            }

        }

    }


    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getAuthor().isBot())
            return;
        //help command
        if(event.getMessage().getContentRaw().equals("?help")) {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("**List of commands**");
            builder.addField("help","This command.",false);
            for (var command: commands) {
                builder.addField(command.getName(),command.GetDescription(),false);
            }
            event.getChannel().sendMessageEmbeds(builder.build()).queue();
            return;
        }

        String[] message=event.getMessage().getContentRaw().substring(1).toLowerCase().split(" ");
        if(event.getMessage().getContentRaw().charAt(0)=='?') {

            for (var command: commands) {
                if (command.getName().equals(message[0])){
                    command.execute(event);
                    return;
                }

            }

        }

    }
}
