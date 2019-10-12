

package me.turulix.main.Commands.Music.Managers;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.turulix.main.Commands.Music.PlayCommand;
import me.turulix.main.DiscordBot;
import me.turulix.main.OAuth2.SpotifyOAuth2Token;
import me.turulix.main.UtilClasses.TextUtilities;
import me.turulix.main.UtilClasses.Utils;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.managers.AudioManager;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
        if (command.length != 2) {
            TextUtilities.SendUsage(e, PlayCommand.instance);
            return;
        }
        Guild guild = e.getGuild();
        Pattern spotifyPatter = Pattern.compile("(https://|http://)(open.spotify.com/track/)([a-zA-Z0-9]+).*");
        Matcher spotifyMatcher = spotifyPatter.matcher(command[1]);
        Pattern youtubePatter = Pattern.compile("(https://|http://)((www\\.|)youtube.com(/watch\\?v=)|youtu.be/)(.*)");
        Matcher youtubeMatcher = youtubePatter.matcher(command[1]);
        String youtubeURL = command[1];

        if (spotifyMatcher.matches()) {
            String apiURL = "https://api.spotify.com/v1/tracks/" + spotifyMatcher.group(3);
            String trackData = Utils.getUrl(apiURL, "Bearer " + SpotifyOAuth2Token.getAccessToken());
            //Get Songtitle from Spotify And Check Youtube for song.
            //TODO: Might be null check
            JSONObject obj = new JSONObject(trackData);
            String songname = obj.getString("name");
            String artist = obj.getJSONArray("artists").getJSONObject(0).getString("name");
            //----------------------------------------Search Youtube--------------------------------------------//
            String searchQuarry = (artist + " - " + songname);
            String youtubeJSON = Utils.getUrl(
                    "https://www.googleapis.com/youtube/v3/search?key=" +
                            DiscordBot.instance.tomlManager.getToml().tokens.youtubeToken +
                            "&part=id&q=" + searchQuarry + "&maxResults=1&type=video"
            );
            //TODO: Might be null check
            obj = new JSONObject(youtubeJSON);
            JSONArray array = obj.getJSONArray("items");
            if (!array.isEmpty()) {
                youtubeURL = "https://www.youtube.com/watch?v=" + array.getJSONObject(0).getJSONObject("id").getString("videoId");
            }
        } else if (youtubeMatcher.matches()) {
            youtubeURL = command[1];
        } else {
            String searchQuarry = command[1];
            String youtubeJSON = Utils.getUrl(
                    "https://www.googleapis.com/youtube/v3/search?key=" +
                            DiscordBot.instance.tomlManager.getToml().tokens.youtubeToken +
                            "&part=id&q=" + searchQuarry + "&maxResults=1&type=video"
            );
            //TODO: Might be null check
            JSONObject obj = new JSONObject(youtubeJSON);
            JSONArray array = obj.getJSONArray("items");
            if (!array.isEmpty()) {
                youtubeURL = "https://www.youtube.com/watch?v=" + array.getJSONObject(0).getJSONObject("id").getString("videoId");
            }
        }
        if ((guild != null) && (command.length == 2)) {
            loadAndPlay(youtubeURL, e);
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
            public void loadFailed(@NotNull FriendlyException ex) {
                e.reply("Could not play: " + e.getMessage());
            }
        });
    }

    private void play(Guild guild, GuildMusicManager musicManager, @NotNull AudioTrack track, @NotNull CommandEvent e) {
        connectToFirstVoiceChannel(guild.getAudioManager(), e);
        musicManager.scheduler.queue(track);
    }

    private void currentsong(CommandEvent e) {
        GuildMusicManager musicManager = getGuildAudioPlayer(e.getGuild());
        if (musicManager.player.isPaused() || musicManager.player.getPlayingTrack() == null) {
            e.reply("Im not playing anything...");
            return;
        }
        e.reply("Now playing: " + musicManager.player.getPlayingTrack().getInfo().title + " By: " + musicManager.player.getPlayingTrack().getInfo().author);
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

