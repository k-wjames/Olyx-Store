package ke.co.ideagalore.olyxstore.models;

public class Attendant {
    String attendant, attendantId, store;

    public Attendant() {
    }

    public Attendant(String attendant, String attendantId, String store) {
        this.attendant = attendant;
        this.attendantId = attendantId;
        this.store = store;
    }

    public String getAttendant() {
        return attendant;
    }

    public void setAttendant(String attendant) {
        this.attendant = attendant;
    }

    public String getAttendantId() {
        return attendantId;
    }

    public void setAttendantId(String attendantId) {
        this.attendantId = attendantId;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }
}
