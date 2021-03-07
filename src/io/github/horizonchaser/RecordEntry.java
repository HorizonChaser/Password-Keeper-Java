package io.github.horizonchaser;

import java.util.Objects;

public class RecordEntry {
    private String domain;
    private String username;
    private String password;
    private String note;
    private final int hashCode;

    public RecordEntry(String domain, String username, String password, String note) {
        this.domain = domain;
        this.username = username;
        this.password = password;
        this.note = note;
        this.hashCode = this.hashCode();
    }

    /**
     * Calculate hashcode of domain and username field only
     *
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(domain, username);
    }

    /**
     * Compare domain AND username only
     *
     * @param obj object to be compared
     * @return ture if domain and username BOTH equal
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof RecordEntry) {
            RecordEntry curr = (RecordEntry) obj;
            return curr.domain.equals(this.domain) && curr.username.equals(this.username);
        }
        return false;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
