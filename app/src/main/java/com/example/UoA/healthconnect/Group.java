package com.example.UoA.healthconnect;

/**
 * Created by Nancy on 8/29/14.
 */
public class Group {
    private long groupId;
    private String groupName;

    public Group(long groupId, String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
    }

    public String getGroupName() { return groupName; }

    public long getGroupId() { return groupId; }
}
