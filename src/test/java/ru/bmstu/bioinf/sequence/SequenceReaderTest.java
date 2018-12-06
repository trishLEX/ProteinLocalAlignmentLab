package ru.bmstu.bioinf.sequence;

import org.junit.Test;

import java.io.File;

/**
 * ТОЛЬКО функциональное тестирование SequenceReader'a
 */
public class SequenceReaderTest {
    private static final String TEST_FILE = new File("src/test/java/ru/bmstu/bioinf/sequence/test.txt").getAbsolutePath();

    @Test
    public void readerTest() {
        SequenceReader reader = new SequenceReader(TEST_FILE);
        while (reader.hasNext()) {
            System.out.println(reader.next());
        }
    }

    @Test
    public void readerNextTest() {
        SequenceReader reader = new SequenceReader(TEST_FILE);
        System.out.println(reader.next());
    }
}
