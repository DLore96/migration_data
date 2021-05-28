package it.prova.migrationassicurati;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import it.prova.migrationassicurati.connection.MyConnection;
import it.prova.migrationassicurati.dao.Constants;
import it.prova.migrationassicurati.model.Assicurato;


public class test {
	
	public static void main (String args[]) {
		
		//TEST  CONNESSIONE E CARICAMENTO DEL RESULTSET
		Assicurato holderTemp= new Assicurato();
		try(Connection connection = MyConnection.getConnection(Constants.DRIVER_NAME, Constants.CONNECTION_URL_NEWSCHEMA);
				Statement st = connection.createStatement(); ResultSet rs = st.executeQuery("select * from assicurato; ");){
			
			while (rs.next()) {
				holderTemp.setId(rs.getLong("id"));
				holderTemp.setNome(rs.getString("nome"));
				holderTemp.setCognome(rs.getString("cognome"));
				holderTemp.setDataNascita(rs.getDate("datanascita"));
			}
			System.out.println(holderTemp.toString());
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
		//TEST: PRENDO I DAT DAL VECCHIO SCHEMA:
		String query="select d.id, d.codice_fiscale, a.nome, a.cognome, a.data_nascita, count(s.id) as numero_sinistri from dati_fiscali d join anagrafica a on  d.id = a.dati_fiscali_id left join sinistri s on a.id = s.anagrafica_id group by d.id;";
		List<Assicurato> assicurati= new ArrayList<Assicurato>();
		try(Connection connection = MyConnection.getConnection(Constants.DRIVER_NAME, Constants.CONNECTION_URL_OLDCHEMA);
				Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(query);){
			
			while (rs.next()) {
				Assicurato holderTemp1= new Assicurato();
				holderTemp1.setId(rs.getLong("id"));
				holderTemp1.setNome(rs.getString("nome"));
				holderTemp1.setCognome(rs.getString("cognome"));
				holderTemp1.setDataNascita(rs.getDate("data_nascita"));
				holderTemp1.setNuoviSinistri(rs.getInt("numero_sinistri"));
				holderTemp1.setCodiceFiscale(rs.getString("codice_fiscale"));
				assicurati.add(holderTemp1);
			}
			for(Assicurato app: assicurati) {
				System.out.println(app.toString()+ app.getCodiceFiscale());
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		//VALIDAZIONE E PREPARAZIONE MIGRAZIONE:
		for(Assicurato appItem: assicurati) {
			if(!validaAssicurati(appItem)) {
				
				System.out.println("attenzione, utente non processabile ");
				try (Connection connection = MyConnection.getConnection(Constants.DRIVER_NAME, Constants.CONNECTION_URL_NEWSCHEMA);
						PreparedStatement st = connection.prepareStatement(
						"INSERT INTO not_processed(codice_fiscale, old_id) VALUES (?, ?);")) {				
					st.setString(1, appItem.getCodiceFiscale());
					st.setLong(2, appItem.getId());
					st.executeUpdate();
				} catch (Exception e) {
					e.printStackTrace();
					
				}
			}else {
	
				try (Connection connection = MyConnection.getConnection(Constants.DRIVER_NAME, Constants.CONNECTION_URL_NEWSCHEMA);
						PreparedStatement st = connection.prepareStatement(
						"INSERT INTO assicurato(id, nome, cognome, datanascita,sinistri,codice_fiscale) VALUES (?, ?, ?, ?, ?, ?);")) {
					st.setLong(1, appItem.getId());
					st.setString(2, appItem.getNome());
					st.setString(3, appItem.getCognome());
					st.setDate(4, new java.sql.Date(appItem.getDataNascita().getTime()));
					st.setInt(5, appItem.getNuoviSinistri());
					st.setString(6, appItem.getCodiceFiscale());
					st.executeUpdate();
				} catch (Exception e) {
					e.printStackTrace();
					
				}
				
				System.out.println("valido");
			}
			
		}
	} 
	
	private static boolean validaAssicurati(Assicurato assicuratoItem) {
		
		if(assicuratoItem.getNome()==null || assicuratoItem.getCognome()==null || 
				assicuratoItem.getCodiceFiscale()==null || assicuratoItem.getCodiceFiscale().length()!=16
				|| assicuratoItem.getDataNascita()==null || assicuratoItem.getNuoviSinistri()==null) {
			return false;
		}
		
		return true;
		
	}

}
