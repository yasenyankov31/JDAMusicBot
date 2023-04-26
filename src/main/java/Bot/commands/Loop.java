package Bot.commands;

import Bot.ICommand;
import Lavaplayer.GuildMusicManager;
import Lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Loop implements ICommand {
    @Override
    public String getName() {
        return "loop";
    }

    @Override
    public String GetDescription() {
        return "Loops/Unloops a song.";
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
        guildMusicManager.getTrackScheduler().setRepeat();
    }

    @Override
    public void execute(ButtonInteractionEvent event) {
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
        guildMusicManager.getTrackScheduler().setRepeat();
    }
}
