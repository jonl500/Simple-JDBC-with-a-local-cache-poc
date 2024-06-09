package com.ProductDb;
import java.sql.*;
import java.util.HashMap;

public class DbHandler {
   private Connection connection;
   final private HashMap<Integer, Product> products = new HashMap<Integer, Product>();

    public DbHandler() {
    try {
        // Load the JDBC driver
        Class.forName("com.mysql.cj.jdbc.Driver");

        // Establish the connection
        String url = "jdbc:mysql://localhost:3306/practice";
        String username = "user";
        String password = "1234Abcd!";
        connection = DriverManager.getConnection(url, username, password);

        // Create the users table if it doesn't exist
        createTable();
    } catch (ClassNotFoundException | SQLException e) {
        e.printStackTrace();
        }
    }


    public HashMap<Integer, Product> getProducts() {
        return products;
    }

    public void addProduct(Product product) {
        if (product.isUnique()) {
            products.put(product.getId(), product);
        }else {
            product.generateUniqueId();
            products.put(product.getId(), product);
        }
    }

    // Step 1: Retrieve data from the SQL table and populate the HashMap
    public void retrieveData() throws SQLException {
        String sql = "SELECT * FROM products";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            double price = resultSet.getDouble("price");
            int stock = resultSet.getInt("stock");
            if (!products.containsKey(id)) {
                Product product = new Product(name, price, stock, id);

                products.put(id, product);
            }
            
        }

        resultSet.close();
        statement.close();
    }

    // Step 2: Move data from HashMap to SQL
    /**
     * Moves data from the products HashMap to the products table in the database.
     * If a duplicate product is found, an IllegalArgumentException is thrown.
     *
     * @throws SQLException if there is an error executing the SQL statements
     */
    public void moveData() throws SQLException {
        // Iterate over each product in the products HashMap
        for (Product product : products.values()) {
            // Check if a product with the same id already exists in the products table
            String sql = "SELECT COUNT(*) FROM products WHERE id = ?";
            PreparedStatement countStatement = connection.prepareStatement(sql);
            countStatement.setInt(1, product.getId());
            ResultSet countResultSet = countStatement.executeQuery();

            // Check if a duplicate product is found
            if (countResultSet.next()) {
                int count = countResultSet.getInt(1);
                if (count > 0) {
                updateData(product);
                } else {
                insertData(product);
                }
            }

            // Insert the product into the products table
            insertData(product);
            // Close the countStatement and countResultSet
            countStatement.close();
            countResultSet.close();
        }
    }

    // Method to update a product in the products table
    public void updateData(Product product) throws SQLException {
        // Update the product in the products table the query is based on the id
        String sql = "UPDATE products SET name = ?, price = ?, stock = ? WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, product.getName());
        statement.setDouble(2, product.getPrice());
        statement.setInt(3, product.getStock());
        statement.setInt(4, product.getId());
        statement.executeUpdate();
        statement.close();
    }


    public void removeProduct(int id) {
        products.remove(id);
    }

    public void removeDeletedRows() throws SQLException {
        // Grab values from products table
        String sql = "SELECT id FROM products";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        //iterate over id vals
        while (resultSet.next()) {
            int id = resultSet.getInt("id");

            //chec if the id is not present in the hashmap
            if(!products.containsKey(id)) {
                String deleteQuery = "DELETE FROM products WHERE id = ?";
                PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                deleteStatement.setInt(1, id);
                deleteStatement.executeUpdate();
                deleteStatement.close();
            }


        }
       resultSet.close();
       statement.close();

    }

    public Product viewProduct(int id) {
        for (Product product : products.values()) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }

    // Method to create the users table if it doesn't exist
    private void createTable() throws SQLException {
        // Create the users table if it doesn't exist auto increment id is primary key
        String sql = "CREATE TABLE IF NOT EXISTS products (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255), price DOUBLE, stock INT)";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
    }

    // Method to insert data into the users table
    /**
     * Inserts data into the "products" table in the database.
     * If a record with the same id already exists, it updates the existing record.
     *
     * @param product the product object containing the data to be inserted or updated
     * @throws SQLException if there is an error executing the SQL statements
     */
    public void insertData(Product product) throws SQLException {
    String sql = "SELECT COUNT(*) FROM products WHERE id = ?";
    PreparedStatement countStatement = connection.prepareStatement(sql);
    countStatement.setInt(1, product.getId());
    ResultSet cntResultSet = countStatement.executeQuery();
    if (!cntResultSet.next()) {
        String insertSql = "INSERT INTO products (id, name, price, stock) VALUES (?, ?, ?, ?)";
        PreparedStatement insertStatement = connection.prepareStatement(insertSql);
        insertStatement.setInt(1, product.getId());
        insertStatement.setString(2, product.getName());
        insertStatement.setDouble(3, product.getPrice());
        insertStatement.setInt(4, product.getStock());
        insertStatement.executeUpdate();
        insertStatement.close();
    }else {
        int count = cntResultSet.getInt(1);
        if (count > 0) {
            updateData(product);
        }
    }
    cntResultSet.close();
    countStatement.close();
    }


    // Method to close the database connection when the program is done
    public void close() throws SQLException {
        connection.close();
    }

    public void connect() {
        /**
         * Establishes a connection to the MySQL database using the provided credentials.
         *
         * @throws SQLException if there is an error connecting to the database
         */
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/practice", "user", "1234Abcd!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
