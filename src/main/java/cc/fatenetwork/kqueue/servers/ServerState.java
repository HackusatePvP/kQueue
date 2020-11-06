package cc.fatenetwork.kqueue.servers;

public enum ServerState {

    OFFLINE("OFFLINE"), ONLINE("ONLINE"), WHITELIST("WHITELIST");

    private String name;

    ServerState(String toName) {
        this.name = toName;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public String getName() {
        return name;
    }

    public static ServerState parse(String state) {
        switch (state.toUpperCase()) {
            case "OFFLINE":
                return OFFLINE;
            case "ONLINE":
                return ONLINE;
            case "WHITELIST":
                return WHITELIST;
        }
        return null;
    }
}
