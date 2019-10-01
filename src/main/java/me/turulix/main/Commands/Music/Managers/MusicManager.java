

package me.turulix.main.Commands.Music.Managers;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.turulix.main.DiscordBot;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;


public class MusicManager {
    @NotNull
    private final AudioPlayerManager playerManager;
    @NotNull
    private final Map<Long, GuildMusicManager> musicManagers;

    public MusicManager() {
        this.musicManagers = new HashMap<>();

        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(this.playerManager);
        AudioSourceManagers.registerLocalSource(this.playerManager);
    }

    public void Play(@NotNull CommandEvent e) {
        @NotNull String[] command = e.getMessage().getContentDisplay().split(" ", 2);
        Guild guild = e.getGuild();
        if ((guild != null) && (command.length == 2)) {
            loadAndPlay(command[1], e);
        }
    }

    public void Currentsong(@NotNull CommandEvent e) {
        Guild guild = e.getGuild();
        if (guild != null) {
            currentsong(e);
        }
    }

    public void Skip(@NotNull CommandEvent e) {
        Guild guild = e.getGuild();
        if (guild != null) {
            skipTrack(e.getTextChannel());
        }
    }

    public void Volume(@NotNull CommandEvent e) {
        Guild guild = e.getGuild();
        if (guild != null) {
            volume(e);
        }
    }

    public void Leave(@NotNull CommandEvent e) {
        e.getGuild().getAudioManager().closeAudioConnection();
        GuildMusicManager musicManager = getGuildAudioPlayer(e.getGuild());
        musicManager.player.destroy();
    }

    public void Leave(@NotNull Guild g) {
        g.getAudioManager().closeAudioConnection();
        GuildMusicManager musicManager = getGuildAudioPlayer(g);
        musicManager.player.destroy();
    }

    public void LeaveEmpty() {
        DiscordBot.instance.registerStuff.shardManager.getGuilds().forEach(guild -> {
            if (guild.getAudioManager().isConnected()) {
                if (guild.getAudioManager().getConnectedChannel().getMembers().size() == 1 || getGuildAudioPlayer(guild).player.isPaused()) {
                    Leave(guild);
                }
            }
        });
    }

    private synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
        long guildId = Long.parseLong(guild.getId());
        GuildMusicManager musicManager = this.musicManagers.get(guildId);
        if (this.musicManagers.get(guildId) == null) {
            musicManager = new GuildMusicManager(this.playerManager);
            this.musicManagers.put(guildId, musicManager);
        }
        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }

    private void loadAndPlay(final String trackUrl, @NotNull final CommandEvent e) {
        final GuildMusicManager musicManager = getGuildAudioPlayer(e.getGuild());
        this.playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(@NotNull AudioTrack audioTrack) {
                e.reply("Adding to queue " + audioTrack.getInfo().title);
                MusicManager.this.play(e.getGuild(), musicManager, audioTrack, e);
            }

            @Override
            public void playlistLoaded(@NotNull AudioPlaylist audioPlaylist) {
                AudioTrack firstTrack = audioPlaylist.getSelectedTrack();
                if (firstTrack == null) {
                    firstTrack = audioPlaylist.getTracks().get(0);
                }
                e.reply("Adding to queue " + firstTrack.getInfo().title + " (first track of playlist " + audioPlaylist.getName() + ")");

                MusicManager.this.play(e.getGuild(), musicManager, firstTrack, e);
            }

            @Override
            public void noMatches() {
                e.reply("Nothing found by " + trackUrl);
            }

            @Override
            public void loadFailed(@NotNull FriendlyException e) {
                //.sendMessage("Could not play: " + e.getMessage()).queue();
            }
        });
    }

    private void play(Guild guild, GuildMusicManager musicManager, @NotNull AudioTrack track, @NotNull CommandEvent e) {
        connectToFirstVoiceChannel(guild.getAudioManager(), e);
        musicManager.scheduler.queue(track);
    }

    private void currentsong(CommandEvent e) {
        GuildMusicManager musicManager = getGuildAudioPlayer(e.getGuild());
        if (musicManager.player.isPaused()) {
            e.reply("Im not playing anything...");
            return;
        }
        if (musicManager.player.getPlayingTrack() == null) {
            e.reply("Im not playing anything...");
            return;
        }
        e.reply("Now playing: " + musicManager.player.getPlayingTrack().getInfo().title + " By: " + musicManager.player.getPlayingTrack().getInfo().author);
    }

    private void volume(CommandEvent e) {
        GuildMusicManager musicManager = getGuildAudioPlayer(e.getGuild());
        String Message = e.getMessage().getContentRaw();
        @NotNull String[] args = Message.split(" ");
        if (args.length == 1) {
            e.getChannel().sendMessage("Volume: " + musicManager.player.getVolume()).queue();
        }
        if (args.length == 2) {
            try {
                int newVolume = Integer.parseInt(args[1]);
                int oldVolume = musicManager.player.getVolume();
                if ((newVolume <= 150) && (newVolume >= 0)) {
                    e.getChannel().sendMessage("Volume set to: " + newVolume + " (" + oldVolume + ")").queue();
                    musicManager.player.setVolume(newVolume);
                } else {
                    e.reply("Volume range is 0-150");
                }
            } catch (Exception er) {
                e.reply("Usage: !volume <volume>");
            }
        }
        if (args.length > 2) {
            e.reply("Usage: !volume <volume>");
        }
    }

    private void skipTrack(TextChannel channel) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        musicManager.scheduler.nextTrack();
        channel.sendMessage("Skipped to next track.").queue();
    }

    private void connectToFirstVoiceChannel(AudioManager audioManager, @NotNull CommandEvent e) {
        if ((!audioManager.isConnected()) && (!audioManager.isAttemptingToConnect())) {
            for (int i = 0; audioManager.getGuild().getVoiceChannels().size() > i; i++) {
                VoiceChannel voice = audioManager.getGuild().getVoiceChannels().get(i);
                for (int i2 = 0; voice.getMembers().size() > i2; i2++) {
                    Member member = voice.getMembers().get(i2);
                    if (member == e.getMember()) {
                        audioManager.openAudioConnection(voice);
                        return;
                    }
                }
            }
        }
    }
}

