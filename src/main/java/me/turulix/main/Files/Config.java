package me.turulix.main.Files;

/**
 * @author Turulix
 * @project NotDankMemer
 * @since 22.03.2019 23:29
 */
public class Config {
    public Boolean testMode = false;
    public Boolean runOneTimeCode = false;
    public SQL sql = new SQL();
    public Tokens tokens = new Tokens();
    public Auth auth = new Auth();

    public class Tokens {
        public String token = "";
        public String testToken = "";
        public String imageServerToken = "";
        public String DiscordBotsToken = "";
        public String mashapeToken = "";
    }

    public class SQL {
        public String SQLLocation = "";
        public String SQLUsername = "";
        public String SQLPassword = "";
    }

    public class Auth {
        public String webHookSecret = "";
    }
}

