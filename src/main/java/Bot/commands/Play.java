package Bot.commands;

import Bot.ICommand;
import Lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.net.URI;
import java.net.URISyntaxException;


public class Play implements ICommand {
    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String GetDescription() {
        return "Plays song from url/name!";
    }

    @Override
    public void execute(MessageReceivedEvent event) {

        Member member = event.getMember();
        MessageChannel channel = event.getChannel();
        GuildVoiceState memberVoiceState = member.getVoiceState();


        if(!memberVoiceState.inAudioChannel()) {
            channel.sendMessage("You must be in a voice channel!").queue();
            return;
        }

        Member self = event.getGuild().getSelfMember();
        GuildVoiceState selfVoiceState = self.getVoiceState();

        if(!event.getGuild().getAudioManager().isConnected())
            event.getGuild().getAudioManager().openAudioConnection(memberVoiceState.getChannel());


        if(selfVoiceState.getChannel() != memberVoiceState.getChannel() && selfVoiceState.inAudioChannel()) {
                channel.sendMessage("You must be in the same voice channel!").queue();
                return;
        }

        String query=event.getMessage().getContentRaw().replace("?play","");
        try {
            new URI(query);
        } catch (URISyntaxException e) {
            query = "ytsearch:" + query;
        }


        TextChannel textChannel = (TextChannel)event.getMessage().getChannel();
        PlayerManager.get().player(textChannel, query);



    }

    @Override
    public void execute(ButtonInteractionEvent event) {

    }

}
