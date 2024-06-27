package pt.isec.pa.javalife.model.memento;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Memento implements IMemento {
    byte[] snapshot;

    public Memento(Object object) {
            try (ByteArrayOutputStream baos =
                         new ByteArrayOutputStream();
                 ObjectOutputStream oos =
                         new ObjectOutputStream(baos)) {
                oos.writeObject(object);
                snapshot = baos.toByteArray();
            } catch (Exception e) {
                System.out.println("Error creating snapshot");
                snapshot = null;
            }
    }

    @Override
    public Object getSnapshot() {
            if (snapshot == null){
                System.out.println("Snapshot is null");
                return null;
            }
            try (ByteArrayInputStream bais =
                         new ByteArrayInputStream(snapshot);
                 ObjectInputStream ois =
                         new ObjectInputStream(bais)) {
                return ois.readObject();
            } catch (Exception e) {
                System.out.println("Error reading snapshot");
                return null;
            }
    }
}
