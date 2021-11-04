package seedu.duke.ui;

import java.util.ArrayList;

public class Wrapping {
    private final String originalMessage;
    private final int lineLimit;
    private ArrayList<String> lines;

    public Wrapping(String message, int lineLimit) {
        lines = new ArrayList<>();
        originalMessage = message;
        this.lineLimit = lineLimit;
        autoWrap();
    }

    public void autoWrap() {
        String temp = originalMessage;
        String extracted = temp.substring(0, lineLimit);
        outerLoop:
        while (!temp.isBlank()) {
            // check if longer than line limit
            if (temp.length() <= lineLimit) {
                lines.add(temp);
                break;
            }
            for (int i = lineLimit - 1; i >= 0; i--) {
                if (temp.charAt(i) == ' ') {
                    int lastIndexOfSpace = i;
                    lines.add(temp.substring(0, lastIndexOfSpace));
                    try {
                        temp = temp.substring(lastIndexOfSpace + 1);
                    } catch (IndexOutOfBoundsException e) {
                        temp = "";
                    }
                    continue outerLoop;
                }
            }
            // if cannot find space, hard wrap
            lines.add(temp.substring(0, lineLimit - 1) + "-");
            temp = temp.substring(lineLimit - 1);
        }
    }

    public String nextLine() {
        if (lines.size() == 0) {
            return "";
        }
        return lines.remove(0);
    }

    public boolean isEmpty() {
        return lines.isEmpty();
    }

    public static String restrictMessageLength(String message, int length) {
        if (message.length() < length) {
            return message + " ".repeat(length - message.length());
        }
        return message.substring(0, length - 3) + "...";
    }
}