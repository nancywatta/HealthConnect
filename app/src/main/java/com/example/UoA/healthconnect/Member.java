package com.example.UoA.healthconnect;

/**
 * Created by Nancy on 8/29/14.
 */
public class Member {

    private long accountId;
    private String email;
    private String userName;
    private Dictionary role;

    public Member(long accountId, String email, String userName, Dictionary role) {
        this.accountId = accountId;
        this.email = email;
        this.userName = userName;
        this.role = role;
    }

    public String getUserName() { return userName; }

    public Dictionary getRole() { return role; }
}
