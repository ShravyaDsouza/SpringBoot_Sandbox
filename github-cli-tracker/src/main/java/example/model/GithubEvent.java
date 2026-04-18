package example.model;

public class GithubEvent {
    private String type;
    private String repoName;
    private int commitCount;
    private String action;
    private String refType;

     public GithubEvent(String type, String repoName, int commitCount, String action, String refType) {
        this.type = type;
        this.repoName = repoName;
        this.commitCount = commitCount;
        this.action = action;
        this.refType = refType;
    }

    public String getType() {
        return type;
    }

    public String getRepoName() {
        return repoName;
    }

    public int getCommitCount() {
        return commitCount;
    }

    public String getAction() {
        return action;
    }

    public String getRefType() {
        return refType;
    }
}
