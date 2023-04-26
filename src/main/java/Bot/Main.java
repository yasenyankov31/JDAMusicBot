package Bot;

import Bot.commands.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Main {
    public static void main(String[] args){
        JDA jda= JDABuilder.createDefault(Token.Token,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.MESSAGE_CONTENT,
                GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.GUILD_MEMBERS).build();
        jda.getPresence().setStatus(OnlineStatus.ONLINE);
        jda.getPresence().setActivity(Activity.playing("We jo jim"));

        CommandManager manager = new CommandManager();
        manager.add(new Ping());
        manager.add(new Play());
        manager.add(new Skip());
        manager.add(new Stop());
        manager.add(new Loop());
        manager.add(new Pause());
        manager.add(new Volume());
        manager.add(new Shuffle());
        manager.add(new Seek());
        jda.addEventListener(manager);
    }
}
