package ru.bmstu.bioinf.sequence;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Считыватель последовательностей в формате .fasta
 */
public class SequenceReader implements Iterator<Sequence> {
    private Sequence next;
    private File file;
    private List<String> lines;
    private int currentLine;

    public SequenceReader(String filePath) {
        this.file = new File(filePath);
        this.currentLine = 0;
    }

    @Override
    public boolean hasNext() {

        try {
            if (lines == null) {
                lines = Files.readAllLines(file.toPath());
            }
        } catch (IOException e) {
            throw new IllegalStateException("Can't read from file: " + file, e);
        }

        if (next != null) {
            return true;
        } else if (currentLine + 1 < lines.size()) {
            next = getNext();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Sequence next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No next elements was found");
        }

        if (next != null) {
            Sequence result = next;
            next = null;
            return result;
        } else {
            return getNext();
        }
    }

    private Sequence getNext() {
        String name = lines.get(currentLine);

        currentLine++;

        StringBuilder sequence = new StringBuilder();

        for (int i = currentLine; i < lines.size(); i++) {
            if (lines.get(i).charAt(0) == '>') {
                break;
            } else {
                sequence.append(lines.get(i));
                currentLine++;
            }
        }

        return new Sequence(name, sequence.toString());
    }
}
