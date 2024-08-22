import java.io.BufferedReader;
import java.io.StringReader;

public class CountELOC {

    private static final String SINGLE_LINE_COMMENT = "//";
    private static final String START_MULTIPLE_LINES_COMMENT = "/*";
    private static final String END_MULTIPLE_LINES_COMMENT = "*/";
    private static final String REGEX_NEW_LINE = "\n";
    private static final String EMTPY_STRING = "";

    private boolean isInMultipleLinesComment = false;

    public long countExecutableLinesOfCode(String text) {

        long countExecutableLinesOfCode = 0;
        if (text == null) {
            return countExecutableLinesOfCode;
        }
        
        text = text.repeat(500000);
        
        long start = System.nanoTime();
//        long startMem = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());

        String[] textLines = text.split(REGEX_NEW_LINE);
        for (String line : textLines) {
            if (hasExecutableCode(line)) {
                ++countExecutableLinesOfCode;
            }
        }
        
        long time = System.nanoTime() - start;
//        long mem = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) - startMem;
//        System.out.println("Memory usage of split: " + mem);
        System.out.printf("Took %f seconds to read %d lines and split%n", time / 1e9, countExecutableLinesOfCode);
//        countExecutableLinesOfCode = 0;
//        start = System.nanoTime();
//        
//        try(Scanner scanner = new Scanner(text))
//        {
//	        String line;
//	        while (scanner.hasNextLine()) {
//	        	if (hasExecutableCode(scanner.nextLine())) {
//              ++countExecutableLinesOfCode;
//	        	}
//	        }
//        } catch (Exception e) {}
// 
//        time = System.nanoTime() - start;
//        System.out.printf("Took %f seconds for %d lines with scanner%n", time / 1e9, countExecutableLinesOfCode);
      countExecutableLinesOfCode = 0;
      start = System.nanoTime();             
      try (BufferedReader reader = new BufferedReader(new StringReader(text))) {
      	String line;
      	while ((line = reader.readLine()) != null) {
      		if (hasExecutableCode(line)) {
      			++countExecutableLinesOfCode;
      		}
      	}
      } catch (Exception exc) {}
      time = System.nanoTime() - start;
      System.out.printf("Took %f seconds for %d lines with reader%n", time / 1e9, countExecutableLinesOfCode);

        countExecutableLinesOfCode = 0;
        start = System.nanoTime();
//        startMem = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());

        countExecutableLinesOfCode = text.lines().filter(s -> hasExecutableCode(s)).count();
 
        time = System.nanoTime() - start;
//        mem = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) - startMem;
//        System.out.println("Memory usage of split: " + mem);
        System.out.printf("Took %f seconds for %d lines with streams%n", time / 1e9, countExecutableLinesOfCode);
        
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
