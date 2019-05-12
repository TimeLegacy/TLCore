package net.timelegacy.core.handler;

public enum Punishment {
  HACKING,
  PROFANITY,
  OTHER,
  NULL;

  public enum Type {
    BAN,
    MUTE,
    UNBAN,
    UNMUTE
  }
}
