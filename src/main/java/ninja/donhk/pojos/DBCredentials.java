package ninja.donhk.pojos;

public enum DBCredentials {
    USERNAME("sayayin"),
    DATABASE("/home/donhk/sayadb"),
    PASSWD("StrongPwd");

    private String val;

    public String val() {
        return val;
    }

    DBCredentials(String val) {
        this.val = val;
    }
}
