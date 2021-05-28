package it.prova.migrationassicurati.dao;

public interface Constants {
	
	public static final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
	public static final String CONNECTION_URL_NEWSCHEMA = "jdbc:mysql://localhost:3306/nuovoschema?user=root&password=root&serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";
	public static final String CONNECTION_URL_OLDCHEMA = "jdbc:mysql://localhost:3306/newschema?user=root&password=root&serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";

}
