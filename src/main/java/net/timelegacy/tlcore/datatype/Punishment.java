package net.timelegacy.tlcore.datatype;

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
