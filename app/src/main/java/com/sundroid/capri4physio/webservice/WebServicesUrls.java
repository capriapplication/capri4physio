package com.sundroid.capri4physio.webservice;

/**
 * Created by sunil on 20-01-2017.
 */

public class WebServicesUrls {

//        public static final String BASE_URL = "http://10.0.2.2/caprispine/";
        public static final String BASE_URL = "http://192.168.0.102/caprispine/";
//    public static final String BASE_URL = "http://www.caprispine.in/api/caprispine/";

    public static final String CHAT_MEDIA_URL = BASE_URL + "studentchat/upload/";

    public static final String USER_CRUD = BASE_URL + "student/usermanagement/usercrud.php";

    public static final String STUDENT_APPLICATION_CRUD = BASE_URL+"student/studentapplication/studentapplicationcrud.php";
    public static final String COURSE_CRUD = BASE_URL + "student/course/coursecrud.php";
    public static final String STUDENT_ATTENDANCE_CRUD = BASE_URL + "student/studentattendance/attendancecrud.php";
    public static final String ATTENDANCE_TAKEN_CRUD = BASE_URL + "student/studentattendance/attendancetaken.php";
    public static final String STUDENT_COURSE_CRUD = BASE_URL + "student/studentcoursereg/studentcoursecrud.php";
    public static final String CHAT_CRUD = BASE_URL + "studentchat/stuchatcrud.php";
}
