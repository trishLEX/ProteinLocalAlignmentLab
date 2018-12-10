package ru.bmstu.bioinf.sequence;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

/**
 * Считыватель последовательностей в формате .fasta
 */
public class SequenceReader implements Iterator<Sequence> {
    private Sequence next;
    private BufferedReader reader;
    private String lastName;

    public SequenceReader(String filePath) {
        File file = new File(filePath);

        try {
            this.reader = new BufferedReader(new FileReader(file));
            this.lastName = reader.readLine();
        } catch (IOException e) {
            throw new IllegalArgumentException("Can't read from file: " + filePath, e);
        }
    }

    @Override
    public boolean hasNext() {
        if (next != null) {
            return true;
        } else {
            if (lastName == null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
                return false;
            }

            next = getNext();
            return true;
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

    public Sequence[] next(int number) {
        Sequence[] res = new Sequence[number];
        for (int i = 0; i < number; i++) {
            if (hasNext()) {
                res[i] = next();
            } else {
                return res;
            }
        }
        return res;
    }

    private Sequence getNext() {
        LinkedList<String> lines = new LinkedList<>();
        String name = lastName;
        try {
            String line = reader.readLine();

            while (line.charAt(0) != '>') {
                lines.add(line);
                line = reader.readLine();
                if (line == null) {
                    break;
                }
            }

            lastName = line;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        StringBuilder builder = new StringBuilder();
        for (String line : lines) {
            builder.append(line);
        }

        return new Sequence(name, builder.toString());
    }
}
