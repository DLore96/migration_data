package it.prova.migrationassicurati.dao;

import it.prova.migrationassicurati.AbstractMySQLDAO;

public class ProvaDAO extends AbstractMySQLDAO{
	
	private static final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
	private static final String CONNECTION_URL_NEWSCHEMA = "jdbc:mysql://localhost:3306/nuovoschema?user=root&password=root&serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";
	
	
	public void setConnection() {
		this.connection=connection;
	}
	
	/*public void list() {
		if (isNotActive())
			throw new Exception("Connessione non attiva.");

		List<Assicurato> result = new ArrayList<Assicurato>();
		Assicurato holderTemp = null;
		try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery("select * from proprietario;  ");) {

			while (rs.next()) {
				holderTemp = new Proprietario();
				holderTemp.setId(rs.getLong("id"));
				holderTemp.setNome(rs.getString("nome"));
				holderTemp.setCognome(rs.getString("cognome"));
				holderTemp.setBirthDate(rs.getDate("data_nascita"));
				result.add(holderTemp);
			}
			return result;

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}*/

}
