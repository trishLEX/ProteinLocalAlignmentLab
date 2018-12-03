package ru.bmstu.bioinf;

import ru.bmstu.bioinf.serialization.SerializableSequence;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

import static ru.bmstu.bioinf.MainDumpKMP.DUMP_BIGRAMM;

public class MainDeserialization {
    public static void main(String[] args) {
        List<SerializableSequence> sequences = null;
        try {
            FileInputStream fis = new FileInputStream(DUMP_BIGRAMM);
            ObjectInputStream ois = new ObjectInputStream(fis);
            sequences = (List<SerializableSequence>) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalArgumentException("PIZDA", e);
        }
    }
}
