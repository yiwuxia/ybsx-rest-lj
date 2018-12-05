package com.ybsx.base.config;

import javax.sql.DataSource;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.jdbc.AbstractJDBCComponent;
import org.apache.mahout.cf.taste.impl.model.jdbc.AbstractJDBCDataModel;

public class LijinJdbcModel extends AbstractJDBCDataModel {
	

	  
	  /**
	   * <p>
	   * Creates a {@link LijinJdbcModel} using the default {@link DataSource} (named
	   * {@link #DEFAULT_DATASOURCE_NAME} and default table/column names.
	   * </p>
	   * 
	   * @throws TasteException
	   *           if {@link DataSource} can't be found
	   */
	  public LijinJdbcModel() throws TasteException {
	    this(DEFAULT_DATASOURCE_NAME);
	  }
	  
	  /**
	   * <p>
	   * Creates a {@link LijinJdbcModel} using the default {@link DataSource} found under the given name, and
	   * using default table/column names.
	   * </p>
	   * 
	   * @param dataSourceName
	   *          name of {@link DataSource} to look up
	   * @throws TasteException
	   *           if {@link DataSource} can't be found
	   */
	  public LijinJdbcModel(String dataSourceName) throws TasteException {
	    this(AbstractJDBCComponent.lookupDataSource(dataSourceName),
	         DEFAULT_PREFERENCE_TABLE,
	         DEFAULT_USER_ID_COLUMN,
	         DEFAULT_ITEM_ID_COLUMN,
	         DEFAULT_PREFERENCE_COLUMN,
	         DEFAULT_PREFERENCE_TIME_COLUMN);
	  }
	  
	  /**
	   * <p>
	   * Creates a {@link LijinJdbcModel} using the given {@link DataSource} and default table/column names.
	   * </p>
	   * 
	   * @param dataSource
	   *          {@link DataSource} to use
	   */
	  public LijinJdbcModel(DataSource dataSource) {
	    this(dataSource,
	         DEFAULT_PREFERENCE_TABLE,
	         DEFAULT_USER_ID_COLUMN,
	         DEFAULT_ITEM_ID_COLUMN,
	         DEFAULT_PREFERENCE_COLUMN,
	         DEFAULT_PREFERENCE_TIME_COLUMN);
	  }
	  
	  /**
	   * <p>
	   * Creates a {@link LijinJdbcModel} using the given {@link DataSource} and default table/column names.
	   * </p>
	   * 
	   * @param dataSource
	   *          {@link DataSource} to use
	   * @param preferenceTable
	   *          name of table containing preference data
	   * @param userIDColumn
	   *          user ID column name
	   * @param itemIDColumn
	   *          item ID column name
	   * @param preferenceColumn
	   *          preference column name
	   * @param timestampColumn timestamp column name (may be null)
	   */
	  public LijinJdbcModel(DataSource dataSource,
	                            String preferenceTable,
	                            String userIDColumn,
	                            String itemIDColumn,
	                            String preferenceColumn,
	                            String timestampColumn) {
	    super(dataSource, preferenceTable, userIDColumn, itemIDColumn, preferenceColumn,
	        // getPreferenceSQL
	        "SELECT " + preferenceColumn + " FROM " + preferenceTable + " WHERE " + userIDColumn + "=? AND "
	            + itemIDColumn + "=?",
	        // getPreferenceTimeSQL
	        "SELECT " + timestampColumn + " FROM " + preferenceTable + " WHERE " + userIDColumn + "=? AND "
	            + itemIDColumn + "=?",
	        // getUserSQL
	        "SELECT DISTINCT " + userIDColumn + ", " + itemIDColumn + ", " + preferenceColumn + " FROM " + preferenceTable
	            + " WHERE " + userIDColumn + "=? ORDER BY " + itemIDColumn,
	        // getAllUsersSQL
	        "SELECT DISTINCT " + userIDColumn + ", " + itemIDColumn + ", " + preferenceColumn + " FROM " + preferenceTable
	            + " ORDER BY " + userIDColumn + ", " + itemIDColumn,
	        // getNumItemsSQL
	        "SELECT COUNT(DISTINCT " + itemIDColumn + ") FROM " + preferenceTable,
	        // getNumUsersSQL
	        "SELECT COUNT(DISTINCT " + userIDColumn + ") FROM " + preferenceTable,
	        // setPreferenceSQL
	        "INSERT INTO " + preferenceTable + '(' + userIDColumn + ',' + itemIDColumn + ',' + preferenceColumn
	            + ") VALUES (?,?,?) ON DUPLICATE KEY UPDATE " + preferenceColumn + "=?",
	        // removePreference SQL
	        "DELETE FROM " + preferenceTable + " WHERE " + userIDColumn + "=? AND " + itemIDColumn + "=?",
	        // getUsersSQL
	        "SELECT DISTINCT " + userIDColumn + " FROM " + preferenceTable + " ORDER BY " + userIDColumn,
	        // getItemsSQL
	        "SELECT DISTINCT " + itemIDColumn + " FROM " + preferenceTable + " ORDER BY " + itemIDColumn,
	        // getPrefsForItemSQL
	        "SELECT DISTINCT " + userIDColumn + ", " + itemIDColumn + ", " + preferenceColumn + " FROM " + preferenceTable
	            + " WHERE " + itemIDColumn + "=? ORDER BY " + userIDColumn,
	        // getNumPreferenceForItemSQL
	        "SELECT COUNT(1) FROM " + preferenceTable + " WHERE " + itemIDColumn + "=?",
	        // getNumPreferenceForItemsSQL
	        "SELECT COUNT(1) FROM " + preferenceTable + " tp1 JOIN " + preferenceTable + " tp2 " + "USING ("
	            + userIDColumn + ") WHERE tp1." + itemIDColumn + "=? and tp2." + itemIDColumn + "=?",
	        "SELECT MAX(" + preferenceColumn + ") FROM " + preferenceTable,
	        "SELECT MIN(" + preferenceColumn + ") FROM " + preferenceTable);
	  }
	  
	  @Override
	  protected int getFetchSize() {
	    // Need to return this for MySQL Connector/J to make it use streaming mode
	    return Integer.MIN_VALUE;
	  }
	  

	
}
