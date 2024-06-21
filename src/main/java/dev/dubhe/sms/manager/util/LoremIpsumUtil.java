package dev.dubhe.sms.manager.util;

import jakarta.annotation.Nonnull;

import java.util.Random;

public class LoremIpsumUtil {
    private static final String[] WORDS = new String[]{
        "lorem", "ipsum", "dolor", "sit", "amet", "consectetur", "adipiscing", "elit",
        "curabitur", "morbi", "lectus", "id", "velit", "gravida", "aliquam", "viverra",
        "nunc", "metus", "porta", "sollicitudin", "adipiscing", "lacus", "urna",
        "pretium", "rhoncus", "est", "pellentesque", "arcu", "nec", "nisl", "volutpat",
        "lacus", "tincidunt", "sed", "cras", "dui", "eget", "ex", "orci", "proin",
        "leo", "lacus", "ipsum", "dolor", "sit", "amet", "consectetur", "adipiscing",
        "elit", "praesent", "lectus", "id", "leo", "pellentesque", "ornare", "mi",
        "sit", "amet", "mauris", "gravida", "quam", "bibendum", "id", "lacinia",
        "urna", "iaculis", "suscipit", "dolor", "nisl", "duis", "ac", "nisi",
        "congue", "nisl", "volutpat", "interdum", "non", "sapien", "ut", "mi",
        "eget", "orci", "molestie", "varius", "vel", "phasellus", "id", "metus",
        "ante", "metus", "ut", "vehicula", "tempus", "consequat", "morbi", "luctus",
        "mauris", "ullamcorper", "lacus", "lorem", "scelerisque", "ultrices", "augue"
    };

    public static @Nonnull String generateLoremIpsum(int wordsCount) {
        if (wordsCount <= 0) {
            return "";
        }
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < wordsCount; i++) {
            sb.append(WORDS[random.nextInt(WORDS.length)]);
            sb.append(" ");
        }
        sb.setCharAt(0, "%s".formatted(sb.charAt(0)).toUpperCase().charAt(0));
        return sb.toString().trim();
    }

    public static @Nonnull String generateLoremIpsum(int minWordsCount, int maxWordsCount) {
        Random random = new Random();
        return LoremIpsumUtil.generateLoremIpsum(random.nextInt(minWordsCount, maxWordsCount));
    }
}