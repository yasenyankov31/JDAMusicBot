package Lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

import java.time.Duration;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {

    private final AudioPlayer _player;
    private boolean isRepeat = false;
    private final BlockingQueue<AudioTrack> queue=new LinkedBlockingQueue<>();

    public TextChannel textChannel;

    public  TrackScheduler(AudioPlayer player)
    {
        this._player=player;
    }

    public void EmbedPlayer(AudioTrack track,TextChannel channel){
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("**Now playing**");
        builder.appendDescription(track.getInfo().title);
        builder.addField("Author: ",track.getInfo().author,true);
        Duration duration = Duration.ofSeconds(track.getDuration()/1000);
        String formattedDuration = String.format("%02d:%02d:%02d",
                duration.toHours(), // Get the hours component
                duration.toMinutesPart(), // Get the minutes component
                duration.toSecondsPart()); // Get the seconds component

        builder.addField("Duration: ", formattedDuration,true);
        builder.addField("Link", String.format("[Click me](%s)",track.getInfo().uri),true);

        builder.setThumbnail("https://img.youtube.com/vi/" + track.getIdentifier() + "/hqdefault.jpg");

        int r = (int) (Math.random() * 256);
        int g = (int) (Math.random() * 256);
        int b = (int) (Math.random() * 256);
        builder.setColor(new Color(r,g,b));

        Button stopButton = Button.secondary("stop",Emoji.fromUnicode("\uD83D\uDEAB")).withStyle(ButtonStyle.SECONDARY);
        Button skipButton = Button.secondary("skip",Emoji.fromUnicode("\u23ED\uFE0F")).withStyle(ButtonStyle.SECONDARY);
        Button pauseButton = Button.secondary("pause",Emoji.fromUnicode("\u23F8\uFE0F")).withStyle(ButtonStyle.SECONDARY);
        Button loopButton = Button.secondary("loop",Emoji.fromUnicode("\uD83D\uDD01")).withStyle(ButtonStyle.SECONDARY);
        Button shuffleButton = Button.secondary("shuffle",Emoji.fromUnicode("\uD83D\uDD00")).withStyle(ButtonStyle.SECONDARY);


        channel.sendMessageEmbeds(builder.build()).setActionRow(stopButton,skipButton,pauseButton,loopButton,shuffleButton).queue();
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if(isRepeat){
            player.startTrack(track.makeClone(), false);
            return;
        }
        player.startTrack(queue.poll(),false);

        if(player.getPlayingTrack()!=null)
            EmbedPlayer(player.getPlayingTrack(), textChannel);


    }
    public void shuffleQueue()
    {
        ArrayList<AudioTrack> list = new ArrayList<>(queue);
        // Shuffle the list
        Collections.shuffle(list);

        // Clear the original BlockingQueue
        queue.clear();

        // Add the shuffled list elements back to the BlockingQueue
        queue.addAll(list);
    }

    public void queue(AudioTrack track, TextChannel channel)
    {
        if(_player.getPlayingTrack()!=null) {
            channel.sendMessage(track.getInfo().title+" added to queue.").queue();
        }
        else {
            EmbedPlayer(track,channel);
        }

        if (!_player.startTrack(track,true)) {
            if(textChannel==null)
                textChannel=channel;
            queue.offer(track);
        }


    }

    public AudioPlayer getPlayer()
    {
        return _player;
    }

    public BlockingQueue<AudioTrack> getQueue() {
        return queue;
    }

    public void setRepeat() {
        isRepeat = !isRepeat;
    }
}
