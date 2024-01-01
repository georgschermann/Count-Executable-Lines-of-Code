
import java.util.regex.Matcher;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TestCountELOC {

    private static CountELOC countELOC;
    private static String PRINTABLE_CHARS_ASCII;
    private static final int[] RANGE_PRINTABLE_CHARS_ASCII = {32, 127};
    private static final int NO_ELOC = 0;
    private static final int EXPECTED_ELOC = 5;

    @BeforeAll
    public static void setUpClass() {

        char[] printableASCII = new char[1 + RANGE_PRINTABLE_CHARS_ASCII[1] - RANGE_PRINTABLE_CHARS_ASCII[0]];

        for (int i = RANGE_PRINTABLE_CHARS_ASCII[0]; i <= RANGE_PRINTABLE_CHARS_ASCII[1]; ++i) {
            printableASCII[i - RANGE_PRINTABLE_CHARS_ASCII[0]] = (char) (i);
        }
        PRINTABLE_CHARS_ASCII = String.valueOf(printableASCII);
    }

    public TestCountELOC() {
        countELOC = new CountELOC();
    }

    @Test
    public void inputIsNull() {
        String input = null;
        int countExecutableLinesOfCode = countELOC.countExecutableLinesOfCode(input);
        assertTrue(countExecutableLinesOfCode == NO_ELOC);
    }

    @Test
    public void inputIsEmtpy() {
        String input = "";
        int countExecutableLinesOfCode = countELOC.countExecutableLinesOfCode(input);
        assertTrue(countExecutableLinesOfCode == NO_ELOC);
    }

    @Test
    public void inputIsWhteSpace() {
        String input = """
                       
                       
                       """;
        int countExecutableLinesOfCode = countELOC.countExecutableLinesOfCode(input);
        assertTrue(countExecutableLinesOfCode == NO_ELOC);
    }

    @Test
    public void inputHasNoELOC_andAllKindsOfBlankComments() {
        String input = """
                       /*      *//**//**//**//*       *//**/
                       /**//**//*     *//**/  /*
                       
                       
                       *///
                      
                       /*
                       
                       */
                       
                        //

                       /*       *//*    *//**//**//**/
                       /**/   /**/  /*    *//*     *//**/
                       
                       """.replaceAll("PRINTABLE_CHARS_ASCII", Matcher.quoteReplacement(PRINTABLE_CHARS_ASCII));;
        int countExecutableLinesOfCode = countELOC.countExecutableLinesOfCode(input);
        assertTrue(countExecutableLinesOfCode == NO_ELOC);
    }

    @Test
    public void inputHasNoELOC_andAllKindsOfNonBlankComments() {
        String input = """
                        /* PRINTABLE_CHARS_ASCII This_is_Commment */  /* This_is_Commment PRINTABLE_CHARS_ASCII*/
                       
                       /* 
                        This_is_Commment PRINTABLE_CHARS_ASCII
                        *//*
                       This_is_Commment PRINTABLE_CHARS_ASCII
                        *//* This_is_Commment PRINTABLE_CHARS_ASCII */
                       
                        // This_is_Commment PRINTABLE_CHARS_ASCII
                       
                        // PRINTABLE_CHARS_ASCII /* This_is_Commment */
                       
                        //// PRINTABLE_CHARS_ASCII This_is_Commment PRINTABLE_CHARS_ASCII
                       
                        /* This_is_Commment PRINTABLE_CHARS_ASCII */// This_is_Commment PRINTABLE_CHARS_ASCII
                       """.replaceAll("PRINTABLE_CHARS_ASCII", Matcher.quoteReplacement(PRINTABLE_CHARS_ASCII));
        int countExecutableLinesOfCode = countELOC.countExecutableLinesOfCode(input);
        assertTrue(countExecutableLinesOfCode == NO_ELOC);
    }

    @Test
    public void inputHasELOC_andNoComments() {
        String input = """
                       PRINTABLE_CHARS_ASCII This_is_ELOC_No_1 PRINTABLE_CHARS_ASCII
                       This_is_ELOC_No_2 PRINTABLE_CHARS_ASCII
                       
                            PRINTABLE_CHARS_ASCII This_is_ELOC_No_3 PRINTABLE_CHARS_ASCII
                       
                              This_is_ELOC_No_4 PRINTABLE_CHARS_ASCII
                       
                       
                       This_is_ELOC_No_5 PRINTABLE_CHARS_ASCII PRINTABLE_CHARS_ASCII
                       """.replaceAll("PRINTABLE_CHARS_ASCII", Matcher.quoteReplacement(PRINTABLE_CHARS_ASCII));;
        int countExecutableLinesOfCode = countELOC.countExecutableLinesOfCode(input);
        assertTrue(countExecutableLinesOfCode == EXPECTED_ELOC);
    }

    @Test
    public void inputHasELOC_andSingleLineComments() {
        String input = """
                       This_is_ELOC_No_1 PRINTABLE_CHARS_ASCII
                       PRINTABLE_CHARS_ASCII This_is_ELOC_No_2 // This_is_Commment PRINTABLE_CHARS_ASCII
                                              
                       // This_is_Commment PRINTABLE_CHARS_ASCII //// PRINTABLE_CHARS_ASCII This_is_Commment
                       ////////// This_is_Commment PRINTABLE_CHARS_ASCII
                       This_is_ELOC_No_3 PRINTABLE_CHARS_ASCII
                                ////////// This_is_Commment PRINTABLE_CHARS_ASCII
                            PRINTABLE_CHARS_ASCII   This_is_ELOC_No_4 PRINTABLE_CHARS_ASCII
                                              
                       //  This_is_Commment PRINTABLE_CHARS_ASCII
                       //  PRINTABLE_CHARS_ASCII This_is_Commment 
                       //  PRINTABLE_CHARS_ASCII This_is_Commment PRINTABLE_CHARS_ASCII
                       This_is_ELOC_No_5 PRINTABLE_CHARS_ASCII PRINTABLE_CHARS_ASCII
                       """.replaceAll("PRINTABLE_CHARS_ASCII", Matcher.quoteReplacement(PRINTABLE_CHARS_ASCII));;
        int countExecutableLinesOfCode = countELOC.countExecutableLinesOfCode(input);
        assertTrue(countExecutableLinesOfCode == EXPECTED_ELOC);
    }

    @Test
    public void inputHasELOC_andSingleLineCommentsContainingStartAndEndOfMutlipleLinesComment() {
        String input = """
                       // /*
                       
                       // */
                       
                       //  /*
                       This_is_ELOC_No_1 PRINTABLE_CHARS_ASCII
                       // */
                       
                       // This_is_Commment /* PRINTABLE_CHARS_ASCII This_is_Commment
                       PRINTABLE_CHARS_ASCII This_is_ELOC_No_2 
                       //// This_is_Commment PRINTABLE_CHARS_ASCII */
                       
                       
                        // /**/ /* */ /* This_is_Commment PRINTABLE_CHARS_ASCII
                        PRINTABLE_CHARS_ASCII This_is_ELOC_No_3 PRINTABLE_CHARS_ASCII
                        //// This_is_Commment PRINTABLE_CHARS_ASCII */ 
                       
                        This_is_ELOC_No_4 PRINTABLE_CHARS_ASCII  PRINTABLE_CHARS_ASCII   
                                              
                       //  This_is_Commment PRINTABLE_CHARS_ASCII /*
                       This_is_ELOC_No_5 PRINTABLE_CHARS_ASCII PRINTABLE_CHARS_ASCII
                       """.replaceAll("PRINTABLE_CHARS_ASCII", Matcher.quoteReplacement(PRINTABLE_CHARS_ASCII));;
        int countExecutableLinesOfCode = countELOC.countExecutableLinesOfCode(input);
        assertTrue(countExecutableLinesOfCode == EXPECTED_ELOC);
    }

    @Test
    public void inputHasELOC_andMultipleLinesCommentsContainingStartOfSingleAndMutlipleLinesComment() {
        String input = """
                       /*
                       /*
                       /*
                       
                       */
                       
                       /*This_is_Commment PRINTABLE_CHARS_ASCII
                       /* This_is_Commment PRINTABLE_CHARS_ASCII
                       /* /* /* This_is_Commment PRINTABLE_CHARS_ASCII
                       This_is_Commment PRINTABLE_CHARS_ASCII
                       */
                       
                       This_is_ELOC_No_1 PRINTABLE_CHARS_ASCII
                       
                       /*
                       ////////
                       ////////
                       ////////
                       */
                       This_is_ELOC_No_2 PRINTABLE_CHARS_ASCII
                       /*
                       This_is_Commment PRINTABLE_CHARS_ASCII
                       /////
                       /////
                       */
                       
                       This_is_ELOC_No_3 PRINTABLE_CHARS_ASCII
                       This_is_ELOC_No_4 PRINTABLE_CHARS_ASCII
  
                                              
                       This_is_ELOC_No_5 PRINTABLE_CHARS_ASCII /* // This_is_Commment PRINTABLE_CHARS_ASCII */
                       
                       """.replaceAll("PRINTABLE_CHARS_ASCII", Matcher.quoteReplacement(PRINTABLE_CHARS_ASCII));;
        int countExecutableLinesOfCode = countELOC.countExecutableLinesOfCode(input);
        assertTrue(countExecutableLinesOfCode == EXPECTED_ELOC);
    }

    @Test
    public void inputHasELOC_andMultipleLinesComments() {
        String input = """
                       /*
                       This_is_Commment PRINTABLE_CHARS_ASCII
                       PRINTABLE_CHARS_ASCII This_is_Commment PRINTABLE_CHARS_ASCII
                       PRINTABLE_CHARS_ASCII This_is_Commment
                       */
                       
                       This_is_ELOC_No_1 PRINTABLE_CHARS_ASCII
                       
                       PRINTABLE_CHARS_ASCII This_is_ELOC_No_2 /* This_is_Commment  PRINTABLE_CHARS_ASCII
                       This_is_Commment  PRINTABLE_CHARS_ASCII
                           This_is_Commment  PRINTABLE_CHARS_ASCII
                               This_is_Commment  PRINTABLE_CHARS_ASCII                        
                           */ PRINTABLE_CHARS_ASCII This_is_ELOC_No_3 PRINTABLE_CHARS_ASCII
                               
                         PRINTABLE_CHARS_ASCII This_is_ELOC_No_4 PRINTABLE_CHARS_ASCII
                                              
                         PRINTABLE_CHARS_ASCII This_is_ELOC_No_5
                       
                       """.replaceAll("PRINTABLE_CHARS_ASCII", Matcher.quoteReplacement(PRINTABLE_CHARS_ASCII));;
        int countExecutableLinesOfCode = countELOC.countExecutableLinesOfCode(input);
        assertTrue(countExecutableLinesOfCode == EXPECTED_ELOC);
    }

    @Test
    public void inputHasELOC_andBlankInlineComments() {
        String input = """
                       /*      *//**//**//**//*       *//**/
                       /**//**//*     *//**/ This_is_ELOC_No_1 PRINTABLE_CHARS_ASCII /**/
                       PRINTABLE_CHARS_ASCII This_is_ELOC_No_2/**//**//**//**/ This_is_ELOC_No_2(continued)PRINTABLE_CHARS_ASCII   
                       
                         PRINTABLE_CHARS_ASCII  This_is_ELOC_No_3
                               
                       This_is_ELOC_No_4 PRINTABLE_CHARS_ASCII /**/ This_is_ELOC_No_4(continued) PRINTABLE_CHARS_ASCII /**/ This_is_ELOC_No_4(continued) PRINTABLE_CHARS_ASCII
                                              
                       /**/  This_is_ELOC_No_5 PRINTABLE_CHARS_ASCII
                       /**//*    *//**//**//**/
                       /**/   /**/  /*    */  /**/  /**/
                       
                       """.replaceAll("PRINTABLE_CHARS_ASCII", Matcher.quoteReplacement(PRINTABLE_CHARS_ASCII));;
        int countExecutableLinesOfCode = countELOC.countExecutableLinesOfCode(input);
        assertTrue(countExecutableLinesOfCode == EXPECTED_ELOC);
    }

    @Test
    public void inputHasELOC_andInlineComments() {
        String input = """
                       /* PRINTABLE_CHARS_ASCII This_is_Commment PRINTABLE_CHARS_ASCII */
                       
                       This_is_ELOC_No_1 PRINTABLE_CHARS_ASCII
                       
                       PRINTABLE_CHARS_ASCII This_is_ELOC_No_2 /*This_is_Commment PRINTABLE_CHARS_ASCII*/ This_is_ELOC_No_2(continued) PRINTABLE_CHARS_ASCII
                       
                       /* This_is_Commment PRINTABLE_CHARS_ASCII*/ This_is_ELOC_No_3 PRINTABLE_CHARS_ASCII /* This_is_Commment */ This_is_ELOC_No_3(continued) PRINTABLE_CHARS_ASCII   
                               
                         This_is_ELOC_No_4 PRINTABLE_CHARS_ASCII /*This_is_Commment PRINTABLE_CHARS_ASCII*/ This_is_ELOC_No_4(continued) PRINTABLE_CHARS_ASCII
                                              
                       /* PRINTABLE_CHARS_ASCII This_is_Commment */  This_is_ELOC_No_5 PRINTABLE_CHARS_ASCII
                       /*PRINTABLE_CHARS_ASCII This_is_Commment PRINTABLE_CHARS_ASCII */
                       """.replaceAll("PRINTABLE_CHARS_ASCII", Matcher.quoteReplacement(PRINTABLE_CHARS_ASCII));;
        int countExecutableLinesOfCode = countELOC.countExecutableLinesOfCode(input);
        assertTrue(countExecutableLinesOfCode == EXPECTED_ELOC);
    }

    @Test
    public void inputHasELOC_andAllKindsOfComments() {
        String input = """
                       /**//**////////
                       
                       /*
                       /*
                       /*
                       /*
                       
                       PRINTABLE_CHARS_ASCII This_is_Commment PRINTABLE_CHARS_ASCII
                       
                        *//////PRINTABLE_CHARS_ASCII This_is_Commment PRINTABLE_CHARS_ASCII */
                       
                       /*
                       /*
                       /*
                       /*
                       //////////
                       PRINTABLE_CHARS_ASCII This_is_Commment PRINTABLE_CHARS_ASCII
                       
                        */ This_is_ELOC_No_1 PRINTABLE_CHARS_ASCII /////PRINTABLE_CHARS_ASCII This_is_Commment PRINTABLE_CHARS_ASCII */
                       
                       /////PRINTABLE_CHARS_ASCII This_is_Commment PRINTABLE_CHARS_ASCII /**//**/
                       
                       /*PRINTABLE_CHARS_ASCII This_is_Commment PRINTABLE_CHARS_ASCII*/ This_is_ELOC_No_2 PRINTABLE_CHARS_ASCII /*PRINTABLE_CHARS_ASCII This_is_Commment PRINTABLE_CHARS_ASCII*/
                       
                       This_is_ELOC_No_3 PRINTABLE_CHARS_ASCII /*
                       
                       PRINTABLE_CHARS_ASCII This_is_Commment PRINTABLE_CHARS_ASCII
                       */
                       
                       /// /*
                       This_is_ELOC_No_4 PRINTABLE_CHARS_ASCII
                       /// */
                       
                       This_is_ELOC_No_5 PRINTABLE_CHARS_ASCII /**//**//*
                       PRINTABLE_CHARS_ASCII
                       PRINTABLE_CHARS_ASCII This_is_Commment PRINTABLE_CHARS_ASCII
                       PRINTABLE_CHARS_ASCII
                       */
                       
                       """.replaceAll("PRINTABLE_CHARS_ASCII", Matcher.quoteReplacement(PRINTABLE_CHARS_ASCII));;
        int countExecutableLinesOfCode = countELOC.countExecutableLinesOfCode(input);
        assertTrue(countExecutableLinesOfCode == EXPECTED_ELOC);
    }

    @Test
    public void inputHasELOC_andAllELOC_isNestedBetweenAllKindsOfComments() {
        String input = """
                       /* This_is_Commment PRINTABLE_CHARS_ASCII 
                       */ This_is_ELOC_No_1 PRINTABLE_CHARS_ASCII // This_is_Commment PRINTABLE_CHARS_ASCII
                                              
                        /* This_is_Commment PRINTABLE_CHARS_ASCII 
                         */ This_is_ELOC_No_2 PRINTABLE_CHARS_ASCII /*This_is_Commment PRINTABLE_CHARS_ASCII
                       This_is_Commment PRINTABLE_CHARS_ASCII
                       This_is_Commment PRINTABLE_CHARS_ASCII
                       */   This_is_ELOC_No_3 PRINTABLE_CHARS_ASCII ///////// This_is_Commment PRINTABLE_CHARS_ASCII
                       
                       /* This_is_Commment PRINTABLE_CHARS_ASCII*/ This_is_ELOC_No_4 PRINTABLE_CHARS_ASCII /*This_is_Commment PRINTABLE_CHARS_ASCII*/
                       
                        /* This_is_Commment PRINTABLE_CHARS_ASCII 
                        *//*This_is_Commment PRINTABLE_CHARS_ASCII*/ This_is_ELOC_No_5 PRINTABLE_CHARS_ASCII /*This_is_Commment PRINTABLE_CHARS_ASCII
                            This_is_Commment PRINTABLE_CHARS_ASCII
                            This_is_Commment PRINTABLE_CHARS_ASCII
                                  */            
                       """.replaceAll("PRINTABLE_CHARS_ASCII", Matcher.quoteReplacement(PRINTABLE_CHARS_ASCII));;
        int countExecutableLinesOfCode = countELOC.countExecutableLinesOfCode(input);
        assertTrue(countExecutableLinesOfCode == EXPECTED_ELOC);
    }
}
