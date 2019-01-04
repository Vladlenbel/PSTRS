package passage_system;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Database {
	
	private final String user = /*"root"*//* "root"*/"passagesys";
    private final String url = "jdbc:mysql://localhost:3306/passage_system?useUnicode=true&characterEncoding=UTF-8";
    private final String password =/*"serverps"*//*"valdistroer" */"AstZhq4";

    private Connection connection;
    private Statement statement;
    private SQLException ex = new SQLException();
    private ResultSet resultSet;
    
    public Database() {
    	 try {
         	// System.out.println("class foundStart");
         	 Class.forName("com.mysql.jdbc.Driver");
              //System.out.println("class foundFin");
          } catch (Exception e) {
         	 	//System.out.println("class problem1");
         	 	//System.out.println(e);
         	 	//System.out.println("class problem2");
         	 	e.toString();
         	 	System.out.println(e.toString());
         	 	System.out.println("error connection");
          }
     }
    
    private void sendQuery(String query) {
        try {
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
        		ex = e;
        } finally {
            try {
            	System.out.println(ex.toString()); 
                connection.close();
                statement.close();
            } catch (SQLException e) {

            }
        }
    }
    
    private void closeDB() {
        try {
            connection.close();
            statement.close();
            resultSet.close();
        } catch (SQLException e) {
        	System.out.println("Ошибка");
        }
    }  
    
    public void addRecordTime(String  id ) {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        String curDate = dateFormat.format(new Date());
        
        String isNormal ;
    	if (statusWorker(curDate) == 0) {
    		 isNormal = "101";
    	}
    	else {
    		isNormal = "100";
    	}
    	//System.out.println(curDate);
    	
    	
    		String query = "INSERT INTO worker VALUES(" + "\"" +  id + "\""  + "," +
    					"\"" + curDate.toString( ) + "\"" +  "," +
    					"\"" + isNormal + "\"" + ");" ;
    		
    	sendQuery(query);
    	
    }
    
    public String getSoundName(String id) {
    	String answ = null;
    	String query = "SELECT filename FROM sound INNER JOIN workerfio ON "
    			+ "  sound.personellNumber = workerfio.personellNumber WHERE workerIdCard = '" + id + "';";
    	try{
	        connection = DriverManager.getConnection(url, user, password);
	        statement = connection.createStatement();
	        resultSet = statement.executeQuery(query);
	        while (resultSet.next()) {
	        answ = resultSet.getString("filename");
	        }

    	}catch (SQLException e) {

        } finally {
            closeDB();
        }
    	return answ ;
    	
    }
    
    public String dateFinder (String dateStart, String dateFin, String surn, String statusCom, String depart) {
    //	System.out.println("FIO ="+statusCom.replaceAll("\n","")+"sgdf");
    	//if (surn.equals("null") == false) {
    	String[] surname = surn.split("\\s+");
    	int status = 0;
    	if (statusCom.equals("") == false) {
	    	if (statusCom.replaceAll("\n","").equals("Опоздание")) {
	    		status = 101;
	    		}
	    	if (statusCom.replaceAll("\n","").equals("Вход")) {
	    		status = 100;
	    		}
	    	if (statusCom.replaceAll("\n","").equals("Выход")) {
	    		status = 200;
	    		}
    	}
    	String answ = new String();
    	
    	String query = "SELECT * FROM (worker INNER JOIN workerfio ON worker.workerId = workerfio.personellNumber)"
    			+ "INNER JOIN department ON workerfio.department = department.id"
    			+ " WHERE eventTime >=" + "\"" + dateStart+ "\"";	
    	if (dateFin.equals("") == false) {
    		dateFin = dateFin + " 23:59:59";
    		//System.out.println(dateFin);
    		query += "AND eventTime <=" + "\"" + dateFin + "\"";
    	}
    	if (surn.equals("") == false) {
    		query += "AND surname =" + "\"" + surname[0] + "\"";
    	}
    	if (surn.equals("") == false) {
    		query += "AND name =" + "\"" + surname[1] + "\"";
    	}
    	if (surn.equals("") == false) {
    		query += "AND patronymic =" + "\"" + surname[2] + "\"";
    	}
    	if (statusCom.equals("") == false) {
    		query += "AND statuscom =" + "\"" + status + "\"";
    	}
    	if (depart.equals("") == false) {
    		query += "AND title =" + "\"" + depart + "\"";
    	}
    	query += "ORDER BY eventTime" ;
    	System.out.println(query);
    	try{
	        connection = DriverManager.getConnection(url, user, password);
	        statement = connection.createStatement();
	        resultSet = statement.executeQuery(query);;
	      //  answ += "<html><head><meta charset= \"UTF-8\" ></head>";
	        answ += "<table border= \" 1\">"+  
	        		   "<tr>"+  
	        		   "<th>ID</th>" + 
	        		  "<th>ФИО</th>"+ 
	        		  "<th>Время</th>"+ 
	        		  "<th>Статус</th>"+ 
	        		  "<th>Отдел</th>"+
	        		  "</tr>";
	        while (resultSet.next()) {
	        	String statusWord = null;
	        		if (Integer.parseInt( resultSet.getString("statuscom")) == 101) {
	        			statusWord = "Опоздание";
	        		}
	        		if (Integer.parseInt( resultSet.getString("statuscom")) == 100) {
	        			statusWord = "Вход";
	        		}
	        		if (Integer.parseInt( resultSet.getString("statuscom")) == 200) {
	        			statusWord = "Выход";
	        		}
	        	int count = 0;
	        	if( resultSet.getString("surname").equals("Unknown") ) {
		        	
	        		   answ += "<tr>";
		        	   answ += "<td style=\"color:#0000cd\">"+ resultSet.getString("personellNumber") +" </td>";
		        	   answ += "<td style=\"color:#0000cd\">"+ resultSet.getString("surname")+" "+ resultSet.getString("name")+ " " +resultSet.getString("patronymic") +" </td>";
		        	   answ += "<td style=\"color:#0000cd\">"+ resultSet.getString("eventTime") +" </td>";
		        	   answ += "<td style=\"color:#0000cd\">"+ statusWord +" </td>";
		        	   answ += "<td style=\"color:#0000cd\">"+ resultSet.getString("title") +" </td>";
		        	   answ += "</tr>";
		        	   count++;
	        	}
	        	if( Integer.parseInt( resultSet.getString("statuscom")) == 101 && count == 0) {
	        	
	        		   answ += "<tr>";
		        	   answ += "<td style=\"color:#ff0000\">"+ resultSet.getString("personellNumber") +" </td>";
		        	   answ += "<td style=\"color:#ff0000\">"+ resultSet.getString("surname")+" "+ resultSet.getString("name")+ " " +resultSet.getString("patronymic") +" </td>";
		        	   answ += "<td style=\"color:#ff0000\">"+ resultSet.getString("eventTime") +" </td>";
		        	   answ += "<td style=\"color:#ff0000\">"+ statusWord +" </td>";
		        	   answ += "<td style=\"color:#ff0000\">"+ resultSet.getString("title") +" </td>";
		        	   answ += "</tr>";
		        	   count++;
	        	}

	        	if( Integer.parseInt( resultSet.getString("statuscom")) == 101 && count == 0) {
		        	
	        		   answ += "<tr>";
		        	   answ += "<td style=\"color:#ff00ff\">"+ resultSet.getString("personellNumber") +" </td>";
		        	   answ += "<td style=\"color:#ff00ff\">"+ resultSet.getString("surname")+" "+ resultSet.getString("name")+ " " +resultSet.getString("patronymic") +" </td>";
		        	   answ += "<td style=\"color:#ff00ff\">"+ resultSet.getString("eventTime") +" </td>";
		        	   answ += "<td style=\"color:#ff00ff\">"+ statusWord +" </td>";
		        	   answ += "<td style=\"color:#ff00ff\">"+ resultSet.getString("title") +" </td>";
		        	   answ += "</tr>";
		        	   count++;
	        	}
	        	if ((Integer.parseInt( resultSet.getString("statuscom")) == 100 || 
	        			Integer.parseInt( resultSet.getString("statuscom")) == 200) && count == 0) {
	        	
	        	   answ += "<tr>";
	        	   answ += "<td>"+ resultSet.getString("personellNumber") +" </td>";
	        	   answ += "<td>"+ resultSet.getString("surname")+" "+ resultSet.getString("name")+ " " +resultSet.getString("patronymic") +" </td>";
	        	   answ += "<td>"+ resultSet.getString("eventTime") +" </td>";
	        	   answ += "<td>"+ statusWord +" </td>";
	        	   answ += "<td>"+ resultSet.getString("title") +" </td>";
	        	    
	        	  answ += "</tr>";
	        	}

	        
	        }

    	}catch (SQLException e) {

        } finally {
            closeDB();
        }
    	
    	//System.out.println(answ);
    	return answ;
    }
    
    private int statusWorker (String date) {
    	int good = 0;
    	String query = "SELECT * FROM workingHours WHERE id = 1;";
    	try{
	        connection = DriverManager.getConnection(url, user, password);
	        statement = connection.createStatement();
	        resultSet = statement.executeQuery(query);
	        while (resultSet.next()) {
	        	good++;
	        	//System.out.println("aaa11");
	        }
	       // System.out.println(good);
	        if (good == 0) {
	        	return 0;
	        }
	  
	        	
    	}catch (SQLException e) {

        } finally {
            closeDB();
        }   	
    
    	
    return  1;	
    }

    public String addMesForWorker (String surnMes, String soundMes, String typeCom) {
    	
    	if(surnMes.equals("")) {
    		return "<font size=\"3\" color=\"red\" face=\"Arial\">Введите фамилию сотрудника </font>";
    	}
    	String[] fio = surnMes.split("\\s+");
    	String personalNum = new String();
    	String isCompl = new String();
		String tableNum = " SELECT personellNumber FROM workerfio WHERE surname =" + "\"" + fio[0] + "\""+
    	"AND name =" + "\"" + fio[1] + "\"" + "AND patronymic =" + "\"" + fio[2] + "\"";
    	try{
	        connection = DriverManager.getConnection(url, user, password);
	        statement = connection.createStatement();
	        resultSet = statement.executeQuery(tableNum);;
	        while (resultSet.next()) {
	        	personalNum += resultSet.getString("personellNumber") ;
	        }
	        if (personalNum.equals("")) {
	        	return "<font size=\"3\" color=\"red\" face=\"Arial\">Сотрудник c фамилией " +surnMes +" не найден </font>";
	        }

    	}catch (SQLException e) {

        } finally {
            closeDB();
        }
        if (soundMes.equals("Выберите сообщение")) {
        	return "<font size=\"3\" color=\"red\" face=\"Arial\">Не указано сообщение, которое следует оставить </font>";
        }
        if (typeCom.equals("Выберите способ оповещения")) {
        	return "<font size=\"3\" color=\"red\" face=\"Arial\">Не выбран способ оповещения </font>";
        }
    	//System.out.println(personalNum);
    	String sendMess = "INSERT INTO message ( personellNumber,fileToPlay, notification_type,"
    			+ " isComplete) VALUES(" + "\"" +  personalNum + "\""  + "," +
				"\"" + soundMes  + "\"" +  "," + "\"" + typeCom + "\"" +  "," +
				"\"" + "20" + "\"" +  ");" ;
    	
    	sendQuery(sendMess);
		return "<font size=\"3\" color=\"green\" face=\"Arial\">Сообщение для сотрудника "+surnMes+" успешно добавлено"+" </font>";
    }
    
  /*  public void task() throws SchedulerException
    {
        // Запускаем Schedule Factory
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        // Извлекаем планировщик из schedule factory
        Scheduler scheduler = schedulerFactory.getScheduler();
         
        // текущее время
        long ctime = System.currentTimeMillis(); 
         
        // Запускаем JobDetail с именем задания,
        // группой задания и классом выполняемого задания
        JobDetail jobDetail = 
            new JobDetail("jobDetail2", "jobDetailGroup2", AddRecord.class);
        // Запускаем CronTrigger с его именем и именем группы
        CronTrigger cronTrigger = new CronTrigger("cronTrigger", "triggerGroup2");
        try {
            // Устанавливаем CronExpression
            CronExpression cexp = new CronExpression("0/5 * * * * ?");
            // Присваиваем CronExpression CronTrigger'у
            cronTrigger.setCronExpression(cexp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Планируем задание с помощью JobDetail и Trigger
        scheduler.scheduleJob(jobDetail, cronTrigger);
         
        // Запускаем планировщик
        scheduler.start();
    }*/
   public String getLastName() {
    	String lastName = new String();
    	int index = 0;
    	String query = "SELECT *  FROM workerfio;";
    	try{
	        connection = DriverManager.getConnection(url, user, password);
	        statement = connection.createStatement();
	        resultSet = statement.executeQuery(query);
	        while (resultSet.next()) {
	        	lastName += "<option value='";
	        	//System.out.println(resultSet.getString("surname"));
	        		lastName+= resultSet.getString("surname");
	        		lastName+= "'>";
	        		lastName+= resultSet.getString("surname");
	        		lastName+= "</option>";
	        	//index++;
	        }
	        	
    	}catch (SQLException e) {

        } finally {
            closeDB();
        } 
    	// System.out.println(lastName);
    	return lastName;
    }
   
   public String getDepar() {
   	String depar = new String();
   	int index = 0;
   	String query = "SELECT *  FROM department;";
   	try{
	        connection = DriverManager.getConnection(url, user, password);
	        statement = connection.createStatement();
	        resultSet = statement.executeQuery(query);
	        int kol = 0;
	        while (resultSet.next()) {
	        	if (kol != 0) {
	        		depar += ",";
	        	}
	        	depar+= resultSet.getString("title");
	        	kol++;
	        	/*depar += "<option value='";
	        	//System.out.println(resultSet.getString("surname"));
	        	depar+= resultSet.getString("title");
	        	depar+= "'>";
	        	depar+= resultSet.getString("title");
	        	depar+= "</option>";
	        	//index++;*/
	        }
	        	
   	}catch (SQLException e) {

       } finally {
           closeDB();
       } 
   	 //System.out.println(depar);
   	return depar;
   }
   public String getFIO() {
	   	String fio = new String();
	   	int index = 0;
	   	String query = "SELECT *  FROM workerfio;";
	   	try{
		        connection = DriverManager.getConnection(url, user, password);
		        statement = connection.createStatement();
		        resultSet = statement.executeQuery(query);
		        int kol = 0;
		        while (resultSet.next()) {
		        	if (kol != 0) {
		        		fio += ",";
		        	}
		        	fio+= resultSet.getString("surname");
		        	fio+= " ";
		        	fio+= resultSet.getString("name");
		        	fio+= " ";
		        	fio+= resultSet.getString("patronymic");
		        	   kol++;
		        	/*fio += "<option value='";
		        	//System.out.println(resultSet.getString("surname"));
		        	fio+= resultSet.getString("surname");
		        	fio+= " ";
		        	fio+= resultSet.getString("name");
		        	fio+= " ";
		        	fio+= resultSet.getString("patronymic");
		        	fio+= "'>";
		        	fio+= resultSet.getString("surname");
		        	fio+= " ";
		        	fio+= resultSet.getString("name");
		        	fio+= " ";
		        	fio+= resultSet.getString("patronymic");
		        	fio+= "</option>";
		        	//index++;*/
		        }
		        	
	   	}catch (SQLException e) {

	       } finally {
	           closeDB();
	       } 
	   	// System.out.println(fio);
	   	return fio;
	   }
   
   public String getStatus() {
	   	String status = new String();
	   	int index = 0;
	   	String query = "SELECT DISTINCT statuscom  FROM worker;";
	   	try{
		        connection = DriverManager.getConnection(url, user, password);
		        statement = connection.createStatement();
		        resultSet = statement.executeQuery(query);
		        int kol = 0;
		        while (resultSet.next()) {
		        	if (kol != 0) {
		        		status += ",";
		        	}
		        	
		    	    	if (resultSet.getString("statuscom").equals("101")) {
		    	    		status += "Опоздание";
		    	    		}
		    	    	if (resultSet.getString("statuscom").equals("100")) {
		    	    		status += "Вход";
		    	    		}
		    	    	if (resultSet.getString("statuscom").equals("200")) {
		    	    		status += "Выход";
		    	    		}
		        	
		        	//status+= resultSet.getString("statuscom");
		        	kol++;
		        	/*depar += "<option value='";
		        	//System.out.println(resultSet.getString("surname"));
		        	depar+= resultSet.getString("title");
		        	depar+= "'>";
		        	depar+= resultSet.getString("title");
		        	depar+= "</option>";
		        	//index++;*/
		        }
		        	
	   	}catch (SQLException e) {

	       } finally {
	           closeDB();
	       } 
	   	// System.out.println(status);
	   	return status;
	   }
}
