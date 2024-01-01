
public class CountELOC {

    private static final String SINGLE_LINE_COMMENT = "//";
    private static final String START_MULTIPLE_LINES_COMMENT = "/*";
    private static final String END_MULTIPLE_LINES_COMMENT = "*/";
    private static final String REGEX_NEW_LINE = "\n";
    private static final String EMTPY_STRING = "";

    private boolean isInMultipleLinesComment = false;

    public int countExecutableLinesOfCode(String text) {

        int countExecutableLinesOfCode = 0;
        if (text == null) {
            return countExecutableLinesOfCode;
        }

        String[] textLines = text.split(REGEX_NEW_LINE);

        for (String line : textLines) {
            if (hasExecutableCode(line)) {
                ++countExecutableLinesOfCode;
            }
        }

        return countExecutableLinesOfCode;
    }

    private boolean hasExecutableCode(String line) {
        if (line.isBlank()) {
            return false;
        }
        boolean foundExecutableCode = false;

        for (int i = 0; i < line.length(); ++i) {
            if (Character.isWhitespace(line.charAt(i))) {
                continue;
            }
            String toCheck = (i < line.length() - 1) ? line.substring(i, i + 2) : EMTPY_STRING;

            if (!isInMultipleLinesComment && toCheck.startsWith(SINGLE_LINE_COMMENT)) {
                // 'foundExecutableCode = true' only if ELOC preceeds this single line comment.
                break;
            }

            if (!isInMultipleLinesComment && toCheck.startsWith(START_MULTIPLE_LINES_COMMENT)) {
                isInMultipleLinesComment = true;
                ++i;
            } else if (isInMultipleLinesComment && toCheck.startsWith(END_MULTIPLE_LINES_COMMENT)) {
                isInMultipleLinesComment = false;
                ++i;
            } else if (!isInMultipleLinesComment) {
                foundExecutableCode = true;
                // At this point the loop can be ended. We just need to know for a possible
                // valid start of multiple lines comment at positions following the current character
                // and set the value of 'isInMultipleLinesComment' accordingly.
                isInMultipleLinesComment = startsMultipleLinesCommentAtSomeLaterPointOnTheLine(line);
                break;
            }
        }
        return foundExecutableCode;
    }

    private boolean startsMultipleLinesCommentAtSomeLaterPointOnTheLine(String line) {
        int startMultipleLinesComment = line.lastIndexOf(START_MULTIPLE_LINES_COMMENT);
        int endMultipleLinesComment = line.lastIndexOf(END_MULTIPLE_LINES_COMMENT);
        int startSingleLineComment = line.lastIndexOf(SINGLE_LINE_COMMENT);

        return startMultipleLinesComment > endMultipleLinesComment
                && startMultipleLinesComment > startSingleLineComment;
    }
}
