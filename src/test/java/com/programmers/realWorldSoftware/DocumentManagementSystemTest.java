package com.programmers.realWorldSoftware;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static com.programmers.realWorldSoftware.Attributes.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
//import static org.springframework.test.util.AssertionErrors.assertEquals;


public class DocumentManagementSystemTest {
    private static final String RESOURCES =
            "src" + File.separator + "test" + File.separator + "resources" + File.separator;
    private static final String LETTER = RESOURCES + "patient.letter";
    private static final String REPORT = RESOURCES + "patient.report";
    private static final String XRAY = RESOURCES + "xray.jpg";
    private static final String INVOICE = RESOURCES + "patient.invoice";
    private static final String JOE_BLOGGS = "Joe Bloggs";

    private DocumentManagementSystem system = new DocumentManagementSystem();

    @Test
    public void shouldImportFile() throws Exception
    {
        system.importFile(LETTER);

        final Document document = onlyDocument();

        assertAttributeEquals(document, Attributes.PATH, LETTER);
    }

    //파일 없을 때
    @Test(expected = FileNotFoundException.class)
    public void shouldNotImportMissingFile() throws Exception
    {
        system.importFile("gobbledygook.txt");
    }
//
//    //확장자 오류일 때
//    @Test(expected = UnknownFileTypeException.class)
//    public void shouldNotImportUnknownFile() throws Exception
//    {
//        system.importFile(RESOURCES + "unknown.txt");
//    }

    @Test
    public void shouldImportReportAttributes() throws Exception
    {
        system.importFile(REPORT);

        assertIsReport(onlyDocument());
    }


    private Document onlyDocument()
    {
        //1개의 문서를 포함하는지 테스트
        final List<Document> documents = system.contents();
        assertThat(documents, hasSize(1));
        return documents.get(0);
    }

    private void assertAttributeEquals(
            final Document document,
            final String attributeName,
            final String expectedValue)
    {
        assertEquals(
                "Document has the wrong value for " + attributeName,
                expectedValue,
                document.getAttribute(attributeName));
    }

    private void assertIsReport(final Document document)
    {
        assertAttributeEquals(document, PATIENT, JOE_BLOGGS);
        assertAttributeEquals(document, BODY,
                "On 5th January 2017 I examined Joe's teeth.\n" +
                        "We discussed his switch from drinking Coke to Diet Coke.\n" +
                        "No new problems were noted with his teeth.");
        assertTypeIs("REPORT", document);
    }

    private void assertTypeIs(final String type, final Document document)
    {
        assertAttributeEquals(document, TYPE, type);
    }
}
