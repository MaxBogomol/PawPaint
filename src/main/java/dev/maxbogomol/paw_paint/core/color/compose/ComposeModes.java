package dev.maxbogomol.paw_paint.core.color.compose;

public class ComposeModes {
    public static ComposeMode SRC = new SrcComposeMode();
    public static ComposeMode SRC_ATOP = new SrcAtopComposeMode();
    public static ComposeMode SRC_OVER = new SrcOverComposeMode();
    public static ComposeMode SRC_IN = new SrcInComposeMode();
    public static ComposeMode SRC_OUT = new SrcOutComposeMode();
    public static ComposeMode DEST = new DestComposeMode();
    public static ComposeMode DEST_ATOP = new DestAtopComposeMode();
    public static ComposeMode DEST_OVER = new DestOverComposeMode();
    public static ComposeMode DEST_IN = new DestInComposeMode();
    public static ComposeMode DEST_OUT = new DestOutComposeMode();
    public static ComposeMode CLEAR = new ClearComposeMode();
    public static ComposeMode XOR = new XorComposeMode();
}
