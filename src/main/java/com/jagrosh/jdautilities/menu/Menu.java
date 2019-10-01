/*
 * Developed by Turulix on 20.01.19 21:13.
 * Last modified 20.01.19 21:12.
 * Copyright (c) 2019. All rights reserved
 */

package com.jagrosh.jdautilities.menu;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.core.entities.*;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public abstract class Menu {
    protected final EventWaiter waiter;
    protected final long timeout;
    protected final TimeUnit unit;
    protected Set<User> users;
    protected Set<Role> roles;

    protected Menu(EventWaiter waiter, Set<User> users, Set<Role> roles, long timeout, TimeUnit unit) {
        this.waiter = waiter;
        this.users = users;
        this.roles = roles;
        this.timeout = timeout;
        this.unit = unit;
    }

    public abstract void display(MessageChannel var1);

    public abstract void display(Message var1);

    protected boolean isValidUser(User user) {
        return this.isValidUser(user, null);
    }

    protected boolean isValidUser(User user, @Nullable Guild guild) {
        if (user.isBot()) {
            return false;
        }
        if (this.users.isEmpty() && this.roles.isEmpty()) {
            return true;
        }
        if (this.users.contains(user)) {
            return true;
        }
        if (guild == null || !guild.isMember(user)) {
            return false;
        }
        return guild.getMember(user).getRoles().stream().anyMatch(this.roles::contains);
    }

    public static abstract class Builder<T extends Builder<T, V>, V extends Menu> {
        protected EventWaiter waiter;
        protected Set<User> users = new HashSet<User>();
        protected Set<Role> roles = new HashSet<Role>();
        protected long timeout = 1L;
        protected TimeUnit unit = TimeUnit.MINUTES;

        public abstract V build();

        public final T setEventWaiter(EventWaiter waiter) {
            this.waiter = waiter;
            return (T) this;
        }

        public final /* varargs */ T addUsers(User... users) {
            this.users.addAll(Arrays.asList(users));
            return (T) this;
        }

        public final /* varargs */ T setUsers(User... users) {
            this.users.clear();
            this.users.addAll(Arrays.asList(users));
            return (T) this;
        }

        public final /* varargs */ T addRoles(Role... roles) {
            this.roles.addAll(Arrays.asList(roles));
            return (T) this;
        }

        public final /* varargs */ T setRoles(Role... roles) {
            this.roles.clear();
            this.roles.addAll(Arrays.asList(roles));
            return (T) this;
        }

        public final T setTimeout(long timeout, TimeUnit unit) {
            this.timeout = timeout;
            this.unit = unit;
            return (T) this;
        }
    }

}

