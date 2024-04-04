package com.viettelpost.core.base;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.internal.OracleTypes;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;

import java.sql.Date;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


public class BaseDAO2 {
    @Value("${sys.dbUrl}")
    protected String host;
    @Value("${sys.dbUsername}")
    String username;
    @Value("${sys.dbPassword}")
    String password;
    protected Properties props;

    /**
     * @return connection with default connnect timeout 2500ms
     * @throws Exception
     */
    protected Connection getConnection() throws Exception {
        props = new Properties();
        props.setProperty("user", username);
        props.setProperty("password", password);
        props.setProperty(OracleConnection.CONNECTION_PROPERTY_THIN_NET_CONNECT_TIMEOUT, "2500");
        return DriverManager.getConnection(host, props);
    }

    /**
     * @param connectTimeoutSecond in second
     * @return connection with expected timeout
     * @throws Exception
     */
    protected Connection getConnection(int connectTimeoutSecond) throws Exception {
        props = new Properties();
        props.setProperty("user", username);
        props.setProperty("password", password);
        props.setProperty(OracleConnection.CONNECTION_PROPERTY_THIN_NET_CONNECT_TIMEOUT, String.valueOf(connectTimeoutSecond * 1000));
        return DriverManager.getConnection(host, props);
    }

    protected CallableStatement doCall(Connection connection, String procedureName, List<Object> params, int[] listOut, int timeoutSecond) throws Exception {
        CallableStatement statement = connection.prepareCall(generateSql(procedureName, params, listOut));
        bindParams(statement, params, listOut);
        statement.setQueryTimeout(timeoutSecond);
        statement.setFetchSize(500);
        statement.execute();
        return statement;
    }

    private String generateSql(String procedureNameNoParam, List params, int[] lsOut) {
        int count = lsOut.length;
        StringBuilder sql = new StringBuilder("{call ").append(procedureNameNoParam);
        sql.append("(");
        if (params != null) {
            int lenth = params.size() + count;
            for (int i = 0; i < lenth; i++) {
                if (i == (lenth - 1)) {
                    sql.append("?");
                } else {
                    sql.append("?, ");

                }
            }
        } else {
            if (lsOut != null && lsOut.length > 0) {
                sql.append("?");
                if (count > 1) {
                    sql.append(", ?");
                }
            }
        }
        sql.append(")}");
        return sql.toString();
    }

    private void bindParams(CallableStatement statement, List<Object> params, int[] lsOut) throws Exception {
        int outIndex = 1;
        if (params != null) {
            outIndex = params.size() + 1;
            for (int i = 0; i < params.size(); i++) {
                int j = i + 1;
                Object b = params.get(i);
                if (b != null) {
                    if (b instanceof java.util.Date) {
                        statement.setTimestamp(j, new Timestamp(((java.util.Date) b).getTime()));
                    } else {
                        if (b instanceof String) {
                            statement.setString(j, (String) b);
                        } else if (b instanceof StringBuilder && b != null) {
                            statement.setString(j, ((StringBuilder) b).toString());
                        } else if (b instanceof Long) {
                            statement.setLong(j, (Long) b);
                        } else if (b instanceof Double) {
                            statement.setDouble(j, (Double) b);
                        } else {
                            statement.setInt(j, Integer.valueOf(b.toString()));
                        }
                    }
                } else {
                    statement.setObject(j, b);
                }
            }
        }
        if (lsOut != null && lsOut.length > 0) {
            for (int i = 0; i < lsOut.length; i++) {
                statement.registerOutParameter(outIndex + i, lsOut[i]);
            }
        }
    }

    protected List toJsonArray(ResultSet rs) throws Exception {
        List jA = new ArrayList();
        ResultSetMetaData metaData = rs.getMetaData();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        int total = metaData.getColumnCount();

        while (rs.next()) {
            Map<String, Object> jO = new HashMap<>();
            for (int i = 0; i < total; i++) {
                buidObject(rs, metaData, jO, format, i);
            }
            jA.add(jO);
        }
        return jA;
    }

    protected List toJsonArrayNoFormat(ResultSet rs) throws Exception {
        List jA = new ArrayList();
        ResultSetMetaData metaData = rs.getMetaData();
        int total = metaData.getColumnCount();

        while (rs.next()) {
            Map<String, Object> jO = new HashMap<>();
            for (int i = 0; i < total; i++) {
                buidObjectNoFormat(rs, metaData, jO, i);
            }
            jA.add(jO);
        }
        return jA;
    }

    protected Map<String, Object> toJsonObject(ResultSet rs) throws Exception {
        ResultSetMetaData metaData = rs.getMetaData();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        int total = metaData.getColumnCount();
        if (rs.next()) {
            Map<String, Object> jO = new HashMap<>();
            for (int i = 0; i < total; i++) {
                buidObject(rs, metaData, jO, format, i);
            }
            return jO;
        }
        return null;
    }


    private void buidObject(ResultSet rs, ResultSetMetaData metaData, Map<String, Object> jO, DateFormat format, int i) throws Exception {
        String colName = metaData.getColumnName(i + 1);
        if (metaData.getColumnType(i + 1) == OracleTypes.TIMESTAMP || metaData.getColumnType(i + 1) == OracleTypes.DATE) {
            if (rs.getTimestamp(colName) == null) {
                jO.put(colName, null);
            } else {
                jO.put(colName, format.format(rs.getTimestamp(colName)));
            }
        } else {
            jO.put(colName, rs.getObject(colName));
        }
    }

    private void buidObjectNoFormat(ResultSet rs, ResultSetMetaData metaData, Map<String, Object> jO, int i) throws Exception {
        String colName = metaData.getColumnName(i + 1);
        if (metaData.getColumnType(i + 1) == OracleTypes.TIMESTAMP || metaData.getColumnType(i + 1) == OracleTypes.DATE) {
            if (rs.getTimestamp(colName) == null) {
                jO.put(colName, null);
            } else {
                jO.put(colName, rs.getTimestamp(colName).getTime());
            }
        } else {
            jO.put(colName, rs.getObject(colName));
        }
    }

    protected Exception throwException(Exception e) throws Exception {
        if (e.getLocalizedMessage() != null) {
            if (e.getLocalizedMessage().contains("ORA-20001")
                    || e.getLocalizedMessage().contains("ORA-20100")
                    || e.getLocalizedMessage().contains("ORA-20000")
                    || e.getLocalizedMessage().contains("ORA-20006")
                    || e.getLocalizedMessage().contains("ORA-20007")
                    || e.getLocalizedMessage().contains("ORA-20002")
                    || e.getLocalizedMessage().contains("ORA-20580")
                    || e.getLocalizedMessage().contains("ORA-20")
                    || e.getLocalizedMessage().contains("ORA-01403")
            ) {
                throw new VtException(e.getLocalizedMessage());
            }
            if (e.getLocalizedMessage().contains("unique constraint")) {
                throw new VtException("ORA-EXCEPTION: Đã tồn tại");
            }
        }
        throw e;
    }

    protected List<Object> toListObject(ResultSet resultSet) throws Exception {
        JSONArray jsonArray = new JSONArray();
        while (resultSet.next()) {
            int columns = resultSet.getMetaData().getColumnCount();
            JSONObject obj = new JSONObject();
            for (int i = 1; i <= columns; i++) {
                if (Objects.nonNull(resultSet.getObject(i))) {
                    //Xu ly neu column sql la kieu Date thi se tra ve String
                    if (resultSet.getMetaData().getColumnType(i) == Types.TIMESTAMP) {
                        Timestamp timestamp = (Timestamp) resultSet.getObject(i);
                        Date date = new Date(timestamp.getTime());
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        String dateString = simpleDateFormat.format(date);
                        obj.put(resultSet.getMetaData().getColumnLabel(i).toLowerCase(), dateString);
                        obj.put(resultSet.getMetaData().getColumnLabel(i).toLowerCase() + "_timestamp", resultSet.getObject(i));
                    } else {
                        obj.put(resultSet.getMetaData().getColumnLabel(i).toLowerCase(), resultSet.getObject(i));
                    }
                } else {
                    obj.put(resultSet.getMetaData().getColumnLabel(i).toLowerCase(), Optional.empty());
                }
            }
            jsonArray.put(obj);
        }
        return jsonArray.toList();
    }

    protected List<Object> toListObject(List<ResultSet> lstResultSet) throws Exception {
        JSONArray jsonArray = new JSONArray();
        for (int n = 0; n < lstResultSet.size(); n++) {
            ResultSet resultSet = lstResultSet.get(n);
            while (resultSet.next()) {
                int columns = resultSet.getMetaData().getColumnCount();
                JSONObject obj = new JSONObject();
                for (int i = 1; i <= columns; i++) {
                    if (Objects.nonNull(resultSet.getObject(i))) {
                        //Xu ly neu column sql la kieu Date thi se tra ve String
                        if (resultSet.getMetaData().getColumnType(i) == Types.TIMESTAMP) {
                            Timestamp timestamp = (Timestamp) resultSet.getObject(i);
                            Date date = new Date(timestamp.getTime());
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            String dateString = simpleDateFormat.format(date);
                            obj.put(resultSet.getMetaData().getColumnLabel(i).toLowerCase(), dateString);
                            obj.put(resultSet.getMetaData().getColumnLabel(i).toLowerCase() + "_timestamp", resultSet.getObject(i));
                        } else {
                            obj.put(resultSet.getMetaData().getColumnLabel(i).toLowerCase(), resultSet.getObject(i));
                        }
                    } else {
                        obj.put(resultSet.getMetaData().getColumnLabel(i).toLowerCase(), Optional.empty());
                    }
                }
                jsonArray.put(obj);
            }
        }

        return jsonArray.toList();
    }
}