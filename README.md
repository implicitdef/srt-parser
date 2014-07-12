srt-dissector
=============

A scala parser for .srt (subtitles) files.


    1
    00:00:48,825 --> 00:00:51,725
    - Sir, she's still closing.
    - We simply cannot outrun her.

    2
    00:00:51,843 --> 00:00:54,011
    We must surrender
    while we still can.

    3
    00:00:54,649 --> 00:00:56,405
    Gun crews... at the ready!


This parser is able to read not only the .srt files that respect the syntax, but also the real-life .srt files found on the web which sometimes have a _very_ loose interpretation of it.
It is successfully tested against a set of 440+ .srt files coming from various sources and for various medias. Hopefully, if VLC can run a .srt file, this parser should be able to parse it.

The only constraint is that the given file has to be UTF-8 encoded. Auto-detection of the encoding may come in the future (or not).
