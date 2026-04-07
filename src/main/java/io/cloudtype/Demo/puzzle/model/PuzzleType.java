package io.cloudtype.Demo.puzzle.model;

public class PuzzleType
{
    private final String code;
    private final String title;
    private final String summary;
    private final String difficultyLabel;

    public PuzzleType(String code, String title, String summary, String difficultyLabel)
    {
        this.code = code;
        this.title = title;
        this.summary = summary;
        this.difficultyLabel = difficultyLabel;
    }

    public String getCode()
    {
        return code;
    }

    public String getTitle()
    {
        return title;
    }

    public String getSummary()
    {
        return summary;
    }

    public String getDifficultyLabel()
    {
        return difficultyLabel;
    }
}
