package ke.co.ideagalore.olyxstore.models;

public class Attendant {
    String attendant, attendantId, store;
    boolean accessStatus;

    public Attendant() {
    }

    public Attendant(String attendant, String attendantId, String store, boolean accessStatus) {
        this.attendant = attendant;
        this.attendantId = attendantId;
        this.store = store;
        this.accessStatus = accessStatus;
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

    public boolean isAccessStatus() {
        return accessStatus;
    }

    public void setAccessStatus(boolean accessStatus) {
        this.accessStatus = accessStatus;
    }
}
