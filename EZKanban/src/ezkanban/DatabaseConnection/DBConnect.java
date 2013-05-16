package ezkanban.DatabaseConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DBConnect {
	private volatile static DBConnect uniqueInstance;
	private Connection connect;
	private Statement statement;
	private ResultSet resultSet;
	private DBConnect(){
		try {
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/EZKanban?"
							+ "user=superadmin&password=12345");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static DBConnect getInstance(){
		if( uniqueInstance == null ){
			synchronized(DBConnect.class){
				uniqueInstance= new DBConnect();
			}
		}
		return uniqueInstance;

	}

	public List<String> retrieveFromDb(String query){

		try {
			statement = connect.createStatement();
			ResultSet resultSet= statement.executeQuery(query);
			List<String> queryResult=parseResultSet(resultSet);
			return queryResult;
		} catch (SQLException e) {
			e.printStackTrace();
			return(null);
		} finally{close();}

	}


	public void updateDb(String query){
		try {
			statement = connect.createStatement();
			statement.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{ close();}


	}
	public boolean login(String user, String pass){

		String query="SELECT user, pass FROM usuarios WHERE user="+user+";";
		try {
		statement=connect.createStatement();
		resultSet=statement.executeQuery(query);
		if(resultSet.next()){
			List<String> result=parseResultSet(resultSet);
			if(result.contains(user) && result.contains(pass))
				return true;
		}
		}catch (SQLException e) {
			e.printStackTrace();
		} finally{ close();}
		return false;
	}


	private List<String> parseResultSet(ResultSet resultSet) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void close() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}

			if (statement != null) {
				statement.close();
			}

		} catch (Exception e) {

		}
	}
}