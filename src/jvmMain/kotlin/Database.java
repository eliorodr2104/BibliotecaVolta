import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.sql.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Eliomar Alejandro Rodriguez Ferrer.
 * @version 1.0
 * @Description Oggetto Database, che gestisce tutta la logica dietro al database
 */
public class Database {
    private final String directory;
    static private final String TYPE_JSON = "data_type";

    /**
     * Instantiates a new Database.
     *
     * @param nameApplication the name application
     * @param urlDb           the url db
     * @param userDb          the user db
     * @param passwordDb      the password db
     */
    public Database(String nameApplication, String urlDb, String userDb, String passwordDb){
      /*  this.pgSimpleDataSource.setApplicationName(nameApplication);
        this.pgSimpleDataSource.setUrl(urlDb);
        this.pgSimpleDataSource.setSslMode("require");
        this.pgSimpleDataSource.setUser(userDb);
        this.pgSimpleDataSource.setPassword(passwordDb);*/
        this.directory = "Resources/table_file_conversion/conversion.json";
    }

    /**
     * Instantiates a new Database.
     */
    public Database(){
        this.directory = "Resources/table_file_conversion/conversion.json";
    }

    /**
     * Connect database connection.
     *
     * @return the connection
     */
    public Connection connectDB(){
       /* try {
            return pgSimpleDataSource.getConnection();

        } catch (SQLException ignored) {}*/

        return null;
    }

    /**
     * Insert sql row to database.
     *
     * @param connection      the connection
     * @param nomeTable       the nome table
     * @param objectArrayList the object array list
     * @return true se l'operazione è andata a buon fine
     */
    public boolean insertSqlDB(Connection connection, String nomeTable, ArrayList<Object> objectArrayList){
        PreparedStatement statement;

        int contatore = 0;

        StringBuilder sqlVariabili = new StringBuilder();
        String modificaVariabile;
        String classeObject;

        for (Object o : objectArrayList){
            classeObject = String.valueOf(o.getClass());

            if (classeObject.contains("String") || classeObject.contains("Character")){
                modificaVariabile = String.valueOf(o);

                modificaVariabile = modificaVariabile.replace("'", "''");
                modificaVariabile = "'" + modificaVariabile + "'";

                o = modificaVariabile;
            }

            if (contatore == objectArrayList.size() - 1)
                sqlVariabili.append(o);

            else
                sqlVariabili.append(o).append(", ");

            contatore++;
        }

        String sqlRow = "INSERT INTO " + nomeTable + " VALUES (" + sqlVariabili + ")";

        try {
            statement = connection.prepareStatement(sqlRow);

            statement.executeUpdate();

            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Create table sql to database.
     *
     * @param connection       the connection
     * @param nomeTabella      the nome tabella

     * @param primaryKey       the primary key
     * @return true se l'operazione è andata a buon fine
     */
    public boolean createTableDB(Connection connection, String nomeTabella, String primaryKey){
    /*    String pattern = createPatternTable(parametriTabella);

        String sql = "CREATE TABLE IF NOT EXISTS " + nomeTabella + " " + "(" + pattern + "PRIMARY KEY ( " + primaryKey + "))";

        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            return false;
        }*/

        return true;
    }

    /**
     * Create pattern table database.
     *
     *
     */
    private String createPatternTable(/*ArrayList<Parametro> parametroArrayList*/){
        StringBuilder pattern = new StringBuilder();

        FileReader fileReader;
        JSONParser jsonParser = new JSONParser();
        Object obj;
        JSONObject jsonObject;
        JSONArray arrayOggettoSalvare;

        try {
            fileReader = new FileReader(directory);

            obj = jsonParser.parse(new FileReader(directory)); //Oggetto con l'informazione del file json

            jsonObject = (JSONObject) obj; //Oggetto di tipo Json con l'informazione dell'oggetto precedente

            arrayOggettoSalvare = (JSONArray) jsonObject.get(TYPE_JSON); //Array di oggetti json

            if(arrayOggettoSalvare.size() > 0) {
                jsonObject = (JSONObject) arrayOggettoSalvare.get(0);

                /*for (Parametro parametro : parametroArrayList)
                    parametro.setTipoParametro((String) jsonObject.get(parametro.getTipoParametro()));*/
            }

        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }

     /*   for (Parametro parametro : parametroArrayList){
            if (!parametro.isTypeNull())
                pattern.append(parametro.getNomeParametro()).append(" ").append(parametro.getTipoParametro()).append(", ");

            else
                pattern.append(parametro.getNomeParametro()).append(" ").append(parametro.getTipoParametro()).append(" not NULL, ");

        }*/

        return pattern.toString();
    }

    /**
     * Drop table sql to database.
     *
     * @param connection  the connection
     * @param nomeTabella the nome tabella
     */
    public boolean dropTableSqlDB(Connection connection, String nomeTabella){
        try(Statement stmt = connection.createStatement()) {
            String sql = "DROP TABLE " + nomeTabella;

            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    
    /**
     * Select rows sql to database
     *
     * @param connection  the connection
     * @param nomeTabella the nome tabella
     * @return the string row select
     */
    public ArrayList<String> selectRowsSqlDB(Connection connection, String nomeTabella){
        int contaParametri = 0;

        ArrayList<String> utentiReturn = new ArrayList<>();

        String columnsType = columnsSqlDB(connection, nomeTabella);

        String QUERY = "SELECT " + columnsType + " FROM " + nomeTabella;

        String[] columns = columnsSqlDB(connection, nomeTabella).split(", ");

        StringBuilder selectRow = new StringBuilder();

        try(PreparedStatement stmt = connection.prepareStatement(QUERY);
            ResultSet rs = stmt.executeQuery();

        ) {

            while(rs.next()){
                for (String colum : columns) {
                    if (contaParametri == columns.length - 1)
                        selectRow.append(colum).append(": ").append(rs.getString(colum));

                    else
                        selectRow.append(colum).append(": ").append(rs.getString(colum)).append(", ");

                    contaParametri++;
                }

                utentiReturn.add(selectRow.toString());

                selectRow = new StringBuilder();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return utentiReturn;
    }

    /**
     * Delete row sql to database.
     *
     * @param connection  the connection
     * @param nomeTabella the nome tabella
     * @param idEliminare the id eliminare
     * @return the string row delete
     */
    public String deleteRowSqlDB(Connection connection, String nomeTabella, int idEliminare){
        int contatore = 0;

        String columnsType = columnsSqlDB(connection, nomeTabella);

        String QUERY = "SELECT " + columnsType + " FROM " + nomeTabella + " WHERE id = ?";

        String[] columns = columnsSqlDB(connection, nomeTabella).split(", ");

        StringBuilder rowDelete = new StringBuilder();

        try(PreparedStatement stmt = connection.prepareStatement(QUERY)) {
            stmt.setInt(1, idEliminare);

            String sql = "DELETE FROM " + nomeTabella + " " + "WHERE id = ?";

            stmt.executeUpdate(sql);

            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                for (String colum : columns) {
                    if (contatore == columns.length - 1)
                        rowDelete.append(colum).append(": ").append(rs.getString(colum)).append("\n");

                    else
                        rowDelete.append(colum).append(": ").append(rs.getString(colum)).append(", ");

                    contatore++;
                }
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rowDelete.toString();
    }

    /**
     * Update row sql to database.
     *
     * @param connection        the connection
     * @param nomeTabella       the nome tabella
     * @param parametroCambiare the parametro cambiare
     * @param valoreCambiare    the valore cambiare
     * @param idCambiare        the id cambiare
     * @return the string row update
     */
    public String updateRowSqlDB(Connection connection, String nomeTabella, String parametroCambiare, String valoreCambiare, int idCambiare){
        int contatore = 0;

        String columnsType = columnsSqlDB(connection, nomeTabella);

        String QUERY = "SELECT " + columnsType + " FROM " +nomeTabella + " WHERE id = ?";

        String[] columns = columnsType.split(", ");

        StringBuilder updateRow = new StringBuilder();

        try(PreparedStatement stmt = connection.prepareStatement(QUERY)) {
            stmt.setInt(1, idCambiare);

            String sql = "UPDATE " + nomeTabella + " " + "SET " + parametroCambiare + " = ? WHERE id = ?";

            PreparedStatement stmt2 = connection.prepareStatement(sql);
            stmt2.setString(1,valoreCambiare);
            stmt2.setInt(2, idCambiare);

            stmt2.executeUpdate();

            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                for (String colum : columns) {
                    if (contatore == columns.length - 1)
                        updateRow.append(colum).append(": ").append(rs.getString(colum)).append("\n");

                    else
                        updateRow.append(colum).append(": ").append(rs.getString(colum)).append(", ");

                    contatore++;
                }
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return updateRow.toString();
    }

    /**
     * Columns sql to database.
     *
     * @param connection the connection
     * @param tableName table name
     * @return the string columns table
     */
    public String columnsSqlDB(Connection connection, String tableName){
        PreparedStatement stmt;
        ResultSet rs;
        ResultSetMetaData resultSetMetaData;
        StringBuilder columnsTotal = new StringBuilder();

        try {
            String query = "SELECT * FROM " + tableName;
            stmt = connection.prepareStatement(query);
            rs = stmt.executeQuery();

            resultSetMetaData = rs.getMetaData();

            for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++){
                if (i == resultSetMetaData.getColumnCount())
                    columnsTotal.append(resultSetMetaData.getColumnName(i));

                else
                    columnsTotal.append(resultSetMetaData.getColumnName(i)).append(", ");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return columnsTotal.toString();
    }


    /**
     * Columns type sql to database.
     *
     * @param connection the connection
     * @return the string
     */
    public String columnsTypeSqlDB(Connection connection, String tableName){
        PreparedStatement stmt;
        ResultSet rs;
        ResultSetMetaData resultSetMetaData;
        StringBuilder columnsTotalType = new StringBuilder();

        FileReader fileReader;
        JSONParser jsonParser = new JSONParser();
        Object obj;
        JSONObject jsonObject;
        JSONArray arrayOggettoSalvare;

        try {
            fileReader = new FileReader(directory);

            obj = jsonParser.parse(fileReader); //Oggetto con l'informazione del file json

            jsonObject = (JSONObject) obj; //Oggetto di tipo Json con l'informazione dell'oggetto precedente

            arrayOggettoSalvare = (JSONArray) jsonObject.get(TYPE_JSON); //Array di oggetti json

            jsonObject = (JSONObject) arrayOggettoSalvare.get(1);

            String sql = "SELECT * FROM ? WHERE tableName = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, tableName);

            rs = stmt.executeQuery();

            resultSetMetaData = rs.getMetaData();

            for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++){
                if (i == resultSetMetaData.getColumnCount())
                    columnsTotalType.append(jsonObject.get(resultSetMetaData.getColumnTypeName(i)));

                else
                    columnsTotalType.append(jsonObject.get(resultSetMetaData.getColumnTypeName(i))).append(", ");
            }

        } catch (IOException | ParseException | SQLException e) {
            throw new RuntimeException(e);
        }

        return columnsTotalType.toString();
    }

    public boolean controlValueSql(Connection connection, String nomeTabella, String colonnaControllare, String rigaControllare){
        String sql;

        //Controllo d'input
        if(!nomeTabella.matches("[A-Za-z0-9_]+") || !colonnaControllare.matches("[A-Za-z0-9_]+") ||
                !rigaControllare.matches("^[a-zA-Z0-9_.]+$")){
            return false;
        }

        sql = "SELECT * FROM " + nomeTabella + " WHERE " + colonnaControllare + "='" + rigaControllare + "'";

        PreparedStatement stmt = null;

        try {
            stmt = connection.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return false;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    public Object returnValueSql(Connection connection, String nomeTabella,String valoreRestituire,
                                 String colonnaControllare, String rigaControllare){

        String sql;

        sql = "SELECT " + valoreRestituire + " FROM " + nomeTabella + " WHERE " + colonnaControllare + "='" + rigaControllare + "'";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
