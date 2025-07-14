package rs.ac.uns.ftn.informatika.jpa.dto;

public class AnalyticsDTO {
    private long weeklyPostsCount;
    private long monthlyPostsCount;
    private long yearlyPostsCount;
    private long weeklyCommentsCount;
    private long monthlyCommentsCount;
    private long yearlyCommentsCount;
    private double postPercentage;
    private double commentOnlyPercentage;
    private double noActivityPercentage;

    public AnalyticsDTO() {}

    public AnalyticsDTO(long weeklyPostsCount, long monthlyPostsCount, long yearlyPostsCount, long weeklyCommentsCount, long monthlyCommentsCount, long yearlyCommentsCount, double postPercentage, double commentOnlyPercentage, double noActivityPercentage) {
        this.weeklyPostsCount = weeklyPostsCount;
        this.monthlyPostsCount = monthlyPostsCount;
        this.yearlyPostsCount = yearlyPostsCount;
        this.weeklyCommentsCount = weeklyCommentsCount;
        this.monthlyCommentsCount = monthlyCommentsCount;
        this.yearlyCommentsCount = yearlyCommentsCount;
        this.postPercentage = postPercentage;
        this.commentOnlyPercentage = commentOnlyPercentage;
        this.noActivityPercentage = noActivityPercentage;
    }

    public long getWeeklyPostsCount() {
        return weeklyPostsCount;
    }

    public void setWeeklyPostsCount(long weeklyPostsCount) {
        this.weeklyPostsCount = weeklyPostsCount;
    }

    public long getMonthlyPostsCount() {
        return monthlyPostsCount;
    }

    public void setMonthlyPostsCount(long monthlyPostsCount) {
        this.monthlyPostsCount = monthlyPostsCount;
    }

    public long getWeeklyCommentsCount() {
        return weeklyCommentsCount;
    }

    public void setWeeklyCommentsCount(long weeklyCommentsCount) {
        this.weeklyCommentsCount = weeklyCommentsCount;
    }

    public long getYearlyPostsCount() {
        return yearlyPostsCount;
    }

    public void setYearlyPostsCount(long yearlyPostsCount) {
        this.yearlyPostsCount = yearlyPostsCount;
    }

    public long getMonthlyCommentsCount() {
        return monthlyCommentsCount;
    }

    public void setMonthlyCommentsCount(long monthlyCommentsCount) {
        this.monthlyCommentsCount = monthlyCommentsCount;
    }

    public long getYearlyCommentsCount() {
        return yearlyCommentsCount;
    }

    public void setYearlyCommentsCount(long yearlyCommentsCount) {
        this.yearlyCommentsCount = yearlyCommentsCount;
    }

    public double getPostPercentage() {
        return postPercentage;
    }

    public void setPostPercentage(double postPercentage) {
        this.postPercentage = postPercentage;
    }

    public double getCommentOnlyPercentage() {
        return commentOnlyPercentage;
    }

    public void setCommentOnlyPercentage(double commentOnlyPercentage) {
        this.commentOnlyPercentage = commentOnlyPercentage;
    }

    public double getNoActivityPercentage() {
        return noActivityPercentage;
    }

    public void setNoActivityPercentage(double noActivityPercentage) {
        this.noActivityPercentage = noActivityPercentage;
    }
}
