/*
 * Developed by Turulix on 20.01.19 21:13.
 * Last modified 20.01.19 21:12.
 * Copyright (c) 2019. All rights reserved
 */

package me.turulix.main.Commands.Music.Managers;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;


public class AudioPlayerSendHandler implements AudioSendHandler {
    private final AudioPlayer audioPlayer;
    @Nullable
    private AudioFrame lastFrame;

    AudioPlayerSendHandler(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }

    public boolean canProvide() {
        if (this.lastFrame == null) {
            this.lastFrame = this.audioPlayer.provide();
        }
        return this.lastFrame != null;
    }

    public ByteBuffer provide20MsAudio() {
        if (this.lastFrame == null) {
            this.lastFrame = this.audioPlayer.provide();
        }
        @Nullable byte[] data = this.lastFrame != null ? this.lastFrame.getData() : null;
        this.lastFrame = null;

        assert data != null;
        return ByteBuffer.wrap(data);
    }

    public boolean isOpus() {
        return true;
    }
}
