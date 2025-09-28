package com.raf.learning.model;

/**
 * Data Transfer Object representing a test group available for student selection.
 * This class contains all information needed for students to identify and clone
 * a specific test repository from the browsing interface.
 *
 * @apiNote This is the final selection object in the hierarchical browsing flow.
 *          Students receive this object when they complete the subject → year → test type → group selection.
 */
public class TestGroupInfo {
    private Long testGroupId;
    private String groupNumber;
    private String gitPath;

    /**
     * Creates a new TestGroupInfo with the specified details.
     *
     * @param testGroupId The unique database identifier for this test group.
     *                   Used for tracking and database operations.
     * @param groupNumber The display number for this group (e.g., "15", "Group_A").
     *                   This is what students see in the interface.
     * @param gitPath The full filesystem path to the Git repository on the server.
     *               Format: "/srv/git/Subject/Year/TestType/GroupNumber"
     *               Example: "/srv/git/OOP/2024_25/Prvi_ispit/15"
     */
    public TestGroupInfo(Long testGroupId, String groupNumber, String gitPath) {
        this.testGroupId = testGroupId;
        this.groupNumber = groupNumber;
        this.gitPath = gitPath;
    }

    /**
     * Creates a user-friendly display name for this test group.
     * Used in UI components where students need to identify the group.
     *
     * @return A formatted string like "Group 15" or "Group Group_A".
     */
    public String getDisplayName() {
        return "Group " + groupNumber;
    }

    /**
     * Constructs the HTTP URL for Git operations from the filesystem path.
     * Converts the internal server path to a URL that can be used for
     * cloning and other Git operations.
     *
     * @param baseUrl The base URL of the Git server (e.g., "http://user:pass@server").
     *               Should include authentication credentials if required.
     * @return Complete Git URL for repository operations.
     *         Example: "http://user:pass@server/OOP/2024_25/Prvi_ispit/15"
     *
     * @apiNote This method strips the "/srv/git/" prefix and appends the path
     *          to the provided base URL for HTTP Git operations.
     */
    public String constructSourceUrl(String baseUrl) {
        return baseUrl + gitPath.replace("/srv/git/", "");
    }

    // Standard getters with documentation

    /**
     * @return The unique database identifier for this test group.
     */
    public Long getTestGroupId() {
        return testGroupId;
    }

    /**
     * @return The display number for this group as shown to students.
     */
    public String getGroupNumber() {
        return groupNumber;
    }

    /**
     * @return The full filesystem path to the Git repository on the server.
     */
    public String getGitPath() {
        return gitPath;
    }
}
