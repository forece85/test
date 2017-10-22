package com.ganapathy.cricscorer;

public class dtoArchiveFile {
    private String _fileName;
    private ArchiveFileIconType _iconType;

    public enum ArchiveFileIconType {
        Unknown,
        Match,
        Team,
        MatchAdd,
        TeamAdd,
        MatchRaw
    }

    public int getIconResource() {
        if (this._iconType == ArchiveFileIconType.Match) {
            return C0252R.drawable.match_archive;
        }
        if (this._iconType == ArchiveFileIconType.Team) {
            return C0252R.drawable.team_archive;
        }
        if (this._iconType == ArchiveFileIconType.MatchAdd) {
            return C0252R.drawable.match_archive_add;
        }
        if (this._iconType == ArchiveFileIconType.TeamAdd) {
            return C0252R.drawable.team_archive_add;
        }
        if (this._iconType == ArchiveFileIconType.MatchRaw) {
            return C0252R.drawable.match_raw_archive;
        }
        return C0252R.drawable.match_archive;
    }

    public ArchiveFileIconType getIconType() {
        return this._iconType;
    }

    public void setIconType(ArchiveFileIconType value) {
        this._iconType = value;
    }

    public String getFileName() {
        return this._fileName;
    }

    public void setFileName(String value) {
        this._fileName = value;
    }

    public dtoArchiveFile(ArchiveFileIconType iconType, String fileName) {
        this._iconType = iconType;
        this._fileName = fileName;
    }
}
