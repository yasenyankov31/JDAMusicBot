package Bot.commands;

import Bot.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Ping implements ICommand {
    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public String GetDescription() {
        return "Gets latency.";
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("**Pong!**");
        builder.addField("Latency:",event.getJDA().getGatewayPing()+"ms",false);

        int r = (int) (Math.random() * 256);
        int g = (int) (Math.random() * 256);
        int b = (int) (Math.random() * 256);
        builder.setColor(new Color(r,g,b));

        MessageChannel channel = event.getChannel();
        channel.sendMessageEmbeds(builder.build()).queue();

    }

    @Override
    public void execute(ButtonInteractionEvent event) {

    }
}
