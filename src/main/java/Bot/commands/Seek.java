package Bot.commands;

import Bot.ICommand;
import Lavaplayer.GuildMusicManager;
import Lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;


public class Seek implements ICommand {
    @Override
    public String getName() {
        return "seek";
    }

    @Override
    public String GetDescription() {
        return "Seeks song at specific time stamp(use this format HH:MM:SS).";
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

        if(selfVoiceState.getChannel() != memberVoiceState.getChannel()) {
            channel.sendMessage("You must be in the same voice channel!").queue();
            return;
        }

        GuildMusicManager guildMusicManager = PlayerManager.get().getGuildMusicManager(event.getGuild());
        try {
            String timeString=event.getMessage().getContentRaw().replace("?seek","").strip();
            String[] parts = timeString.split(":");
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            int seconds = Integer.parseInt(parts[2]);
            long totalMilliseconds = hours * 3600000L + minutes * 60000L + seconds * 1000L;

            guildMusicManager.getTrackScheduler().getPlayer().getPlayingTrack().setPosition(totalMilliseconds);
        }
        catch (Exception ex)
        {
            channel.sendMessage("Wrong format in the command parameter it should be like this HH:MM:SS !").queue();
        }


    }

    @Override
    public void execute(ButtonInteractionEvent event) {

    }
}
