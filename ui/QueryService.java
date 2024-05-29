interface QueryService {
  public Object[][] students(Integer studentID, String eventType);

  public void updateStudentName(Integer studentID, String studentName) throws Exception;
}
