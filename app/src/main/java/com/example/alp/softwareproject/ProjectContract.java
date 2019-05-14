package com.example.alp.softwareproject;

import android.provider.BaseColumns;

public class ProjectContract {
    private ProjectContract(){}

    public static final class ProjectEntry implements BaseColumns{
        public static final String TABLE_NAME = "AttendanceDemoo";
        public static final String COLUMN_DEMO_ID = "Demo_ID";
        public static final String COLUMN_DEMO_STUDENT_INFO = "Demo_Student_Info";
        public static final String COLUMN_DEMO_DATE = "Timestamp";
        public static final String COLUMN_DEMO_LECTURE_CODE = "Demo_Lecture_Code";
    }

    public static final class ProjectEntry2 implements BaseColumns{
        public static final String TABLE_NAME_2 = "Attendancee";
        public static final String COLUMN_ATTENDANCE_ID = "Attendance_ID";
        public static final String COLUMN_ATT_STUDENT_INFO = "Att_Student_Info";
        public static final String COLUMN_ATT_LECTURE_CODE = "Att_Lecture_Code";
        public static final String COLUMN_DATE = "DATE";
    }

}
