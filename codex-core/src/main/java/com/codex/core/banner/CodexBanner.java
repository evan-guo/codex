package com.codex.core.banner;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.ansi.AnsiStyle;
import org.springframework.core.env.Environment;

import java.io.PrintStream;

/**
 * 实现Banner接口，打印日志
 *
 * @author evan guo
 */
public class CodexBanner implements Banner {
    private static final String[] BANNER = { "",
            "   ██████                ██         ██     ██",
            "  ██░░░░██              ░██        ░░██   ██ ",
            " ██    ░░   ██████      ░██  █████  ░░██ ██  ",
            "░██        ██░░░░██  ██████ ██░░░██  ░░███   ",
            "░██       ░██   ░██ ██░░░██░███████   ██░██  ",
            "░░██    ██░██   ░██░██  ░██░██░░░░   ██ ░░██ ",
            " ░░██████ ░░██████ ░░██████░░██████ ██   ░░██",
            "  ░░░░░░   ░░░░░░   ░░░░░░  ░░░░░░ ░░     ░░" };
    private static final String SPRING_BOOT = " :: Code X :: ";
    private static final int STRAP_LINE_SIZE = 42;

    @Override
    public void printBanner(Environment environment, Class<?> sourceClass,
                            PrintStream printStream) {
        for (String line : BANNER) {
            printStream.println(line);
        }
        String version = "1.0.0";
        version = (version != null) ? " (v" + version + ")" : "";
        StringBuilder padding = new StringBuilder();
        while (padding.length() < STRAP_LINE_SIZE
                - (version.length() + SPRING_BOOT.length())) {
            padding.append(" ");
        }

        printStream.println(AnsiOutput.toString(AnsiColor.GREEN, SPRING_BOOT,
                AnsiColor.DEFAULT, padding.toString(), AnsiStyle.FAINT, version));
        printStream.println();
    }
}
