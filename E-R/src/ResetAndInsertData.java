import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ResetAndInsertData {
    public static void main(String[] args) {
        String url = "jdbc:mariadb://0.tcp.jp.ngrok.io:12592/411177010";
        String user = "411177010";
        String password = "411177010";

        Connection connection = null;
        Statement statement = null;
        PreparedStatement preparedStatement = null;

        try {
            // 載入MariaDB JDBC
            Class.forName("org.mariadb.jdbc.Driver");

            // 建立連接
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();

            // 禁用外鍵約束
            statement.execute("SET FOREIGN_KEY_CHECKS = 0");

            // 刪除所有表格
            ResultSet resultSet = statement.executeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema = '411177010'");
            while (resultSet.next()) {
                String tableName = resultSet.getString(1);
                statement.execute("DROP TABLE IF EXISTS " + tableName);
            }

            // 創建表格
            String createPlantTable = "CREATE TABLE IF NOT EXISTS Plant (" +
                    "PlantID INT PRIMARY KEY, " +
                    "PlantName VARCHAR(50))";

            String createBrandTable = "CREATE TABLE IF NOT EXISTS Brand (" +
                    "BrandID INT PRIMARY KEY, " +
                    "BrandName VARCHAR(50))";

            String createModelTable = "CREATE TABLE IF NOT EXISTS Model (" +
                    "ModelID INT PRIMARY KEY, " +
                    "ModelName VARCHAR(50), " +
                    "BrandID INT, " +
                    "ModelType VARCHAR(20), " +
                    "FOREIGN KEY (BrandID) REFERENCES Brand(BrandID))";

            String createCustomerTable = "CREATE TABLE IF NOT EXISTS Customer (" +
                    "CustomerID INT PRIMARY KEY, " +
                    "Name VARCHAR(50), " +
                    "Address VARCHAR(100), " +
                    "Phone VARCHAR(20), " +
                    "Gender CHAR(1), " +
                    "Income DOUBLE)";

            String createDealerTable = "CREATE TABLE IF NOT EXISTS Dealer (" +
                    "DealerID INT PRIMARY KEY, " +
                    "DealerName VARCHAR(50), " +
                    "Address VARCHAR(100), " +
                    "DealerPhone VARCHAR(20))";

            String createTransmissionTable = "CREATE TABLE IF NOT EXISTS Transmission (" +
                    "TransmissionID INT PRIMARY KEY, " +
                    "SupplierName VARCHAR(50), " +
                    "ManufactureDate DATE, " +
                    "PlantID INT, " +
                    "FOREIGN KEY (PlantID) REFERENCES Plant(PlantID))";

            String createVehicleTable = "CREATE TABLE IF NOT EXISTS Vehicle (" +
                    "VIN VARCHAR(50) PRIMARY KEY, " +
                    "ModelID INT, " +
                    "TransmissionID INT, " +
                    "CustomerID INT, " +
                    "DealerID INT, " +
                    "SaleDate DATE, " +
                    "Color VARCHAR(20), " +
                    "Transmission VARCHAR(20), " +
                    "FOREIGN KEY (ModelID) REFERENCES Model(ModelID), " +
                    "FOREIGN KEY (TransmissionID) REFERENCES Transmission(TransmissionID), " +
                    "FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID), " +
                    "FOREIGN KEY (DealerID) REFERENCES Dealer(DealerID))";

            String createSalesTable = "CREATE TABLE IF NOT EXISTS Sales (" +
                    "SaleID INT PRIMARY KEY, " +
                    "VIN VARCHAR(50), " +
                    "SaleDate DATE, " +
                    "SalePrice DOUBLE, " +
                    "DealerID INT, " +
                    "FOREIGN KEY (VIN) REFERENCES Vehicle(VIN), " +
                    "FOREIGN KEY (DealerID) REFERENCES Dealer(DealerID))";

            String createInventoryTable = "CREATE TABLE IF NOT EXISTS Inventory (" +
                    "VIN VARCHAR(50), " +
                    "DealerID INT, " +
                    "InventoryDate DATE, " +
                    "FOREIGN KEY (VIN) REFERENCES Vehicle(VIN), " +
                    "FOREIGN KEY (DealerID) REFERENCES Dealer(DealerID))";

            statement.execute(createPlantTable);
            statement.execute(createBrandTable);
            statement.execute(createModelTable);
            statement.execute(createCustomerTable);
            statement.execute(createDealerTable);
            statement.execute(createTransmissionTable);
            statement.execute(createVehicleTable);
            statement.execute(createSalesTable);
            statement.execute(createInventoryTable);

            // 啟用外鍵約束
            statement.execute("SET FOREIGN_KEY_CHECKS = 1");

            // 插入資料的SQL語句
            String insertPlantSQL = "INSERT INTO Plant (PlantID, PlantName) VALUES (?, ?)";
            String insertBrandSQL = "INSERT INTO Brand (BrandID, BrandName) VALUES (?, ?)";
            String insertModelSQL = "INSERT INTO Model (ModelID, ModelName, BrandID, ModelType) VALUES (?, ?, ?, ?)";
            String insertVehicleSQL = "INSERT INTO Vehicle (VIN, ModelID, TransmissionID, CustomerID, DealerID, SaleDate, Color, Transmission) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            String insertCustomerSQL = "INSERT INTO Customer (CustomerID, Name, Address, Phone, Gender, Income) VALUES (?, ?, ?, ?, ?, ?)";
            String insertDealerSQL = "INSERT INTO Dealer (DealerID, DealerName, Address, DealerPhone) VALUES (?, ?, ?, ?)";
            String insertTransmissionSQL = "INSERT INTO Transmission (TransmissionID, SupplierName, ManufactureDate, PlantID) VALUES (?, ?, ?, ?)";
            String insertSalesSQL = "INSERT INTO Sales (SaleID, VIN, SaleDate, SalePrice, DealerID) VALUES (?, ?, ?, ?, ?)";
            String insertInventorySQL = "INSERT INTO Inventory (VIN, DealerID, InventoryDate) VALUES (?, ?, ?)";

            // 插入 Plant 資料
            preparedStatement = connection.prepareStatement(insertPlantSQL);
            preparedStatement.setInt(1, 1);
            preparedStatement.setString(2, "Plant A");
            preparedStatement.executeUpdate();

            preparedStatement.setInt(1, 2);
            preparedStatement.setString(2, "Plant B");
            preparedStatement.executeUpdate();

            // 插入 Brand 資料
            preparedStatement = connection.prepareStatement(insertBrandSQL);
            preparedStatement.setInt(1, 1);
            preparedStatement.setString(2, "Toyota");
            preparedStatement.executeUpdate();

            preparedStatement.setInt(1, 2);
            preparedStatement.setString(2, "Honda");
            preparedStatement.executeUpdate();

            preparedStatement.setInt(1, 3);
            preparedStatement.setString(2, "Tesla");
            preparedStatement.executeUpdate();

            preparedStatement.setInt(1, 4);
            preparedStatement.setString(2, "Lexus");
            preparedStatement.executeUpdate();

            preparedStatement.setInt(1, 5);
            preparedStatement.setString(2, "Nissan");
            preparedStatement.executeUpdate();

            // 插入 Model 資料
            preparedStatement = connection.prepareStatement(insertModelSQL);
            preparedStatement.setInt(1, 1);
            preparedStatement.setString(2, "Camry");
            preparedStatement.setInt(3, 1);
            preparedStatement.setString(4, "Sedan");
            preparedStatement.executeUpdate();

            preparedStatement.setInt(1, 2);
            preparedStatement.setString(2, "Civic");
            preparedStatement.setInt(3, 2);
            preparedStatement.setString(4, "Sedan");
            preparedStatement.executeUpdate();

            preparedStatement.setInt(1, 3);
            preparedStatement.setString(2, "Model S");
            preparedStatement.setInt(3, 3);
            preparedStatement.setString(4, "Sedan");
            preparedStatement.executeUpdate();

            preparedStatement.setInt(1, 4);
            preparedStatement.setString(2, "RX");
            preparedStatement.setInt(3, 4);
            preparedStatement.setString(4, "SUV");
            preparedStatement.executeUpdate();

            preparedStatement.setInt(1, 5);
            preparedStatement.setString(2, "Altima");
            preparedStatement.setInt(3, 5);
            preparedStatement.setString(4, "Sedan");
            preparedStatement.executeUpdate();

            // 插入 Customer 資料
            preparedStatement = connection.prepareStatement(insertCustomerSQL);
            preparedStatement.setInt(1, 1);
            preparedStatement.setString(2, "John Doe");
            preparedStatement.setString(3, "123 Elm St");
            preparedStatement.setString(4, "555-1234");
            preparedStatement.setString(5, "M");
            preparedStatement.setDouble(6, 60000);
            preparedStatement.executeUpdate();

            preparedStatement.setInt(1, 2);
            preparedStatement.setString(2, "Jane Smith");
            preparedStatement.setString(3, "456 Oak St");
            preparedStatement.setString(4, "555-5678");
            preparedStatement.setString(5, "F");
            preparedStatement.setDouble(6, 75000);
            preparedStatement.executeUpdate();

            preparedStatement.setInt(1, 3);
            preparedStatement.setString(2, "Alice Johnson");
            preparedStatement.setString(3, "789 Pine St");
            preparedStatement.setString(4, "555-6789");
            preparedStatement.setString(5, "F");
            preparedStatement.setDouble(6, 80000);
            preparedStatement.executeUpdate();

            preparedStatement.setInt(1, 4);
            preparedStatement.setString(2, "Bob Brown");
            preparedStatement.setString(3, "101 Maple St");
            preparedStatement.setString(4, "555-9876");
            preparedStatement.setString(5, "M");
            preparedStatement.setDouble(6, 55000);
            preparedStatement.executeUpdate();

            preparedStatement.setInt(1, 5);
            preparedStatement.setString(2, "Michael White");
            preparedStatement.setString(3, "102 Cedar St");
            preparedStatement.setString(4, "555-4321");
            preparedStatement.setString(5, "M");
            preparedStatement.setDouble(6, 70000);
            preparedStatement.executeUpdate();

            // 插入 Dealer 資料
            preparedStatement = connection.prepareStatement(insertDealerSQL);
            preparedStatement.setInt(1, 1);
            preparedStatement.setString(2, "Best Motors");
            preparedStatement.setString(3, "123 Main St");
            preparedStatement.setString(4, "555-7890");
            preparedStatement.executeUpdate();

            preparedStatement.setInt(1, 2);
            preparedStatement.setString(2, "Super Cars");
            preparedStatement.setString(3, "456 Elm St");
            preparedStatement.setString(4, "555-9876");
            preparedStatement.executeUpdate();

            preparedStatement.setInt(1, 3);
            preparedStatement.setString(2, "Top Vehicles");
            preparedStatement.setString(3, "789 Maple St");
            preparedStatement.setString(4, "555-6543");
            preparedStatement.executeUpdate();

            preparedStatement.setInt(1, 4);
            preparedStatement.setString(2, "Auto World");
            preparedStatement.setString(3, "321 Oak St");
            preparedStatement.setString(4, "555-1234");
            preparedStatement.executeUpdate();

            // 插入 Transmission 資料
            preparedStatement = connection.prepareStatement(insertTransmissionSQL);
            preparedStatement.setInt(1, 1);
            preparedStatement.setString(2, "Getrag");
            preparedStatement.setDate(3, java.sql.Date.valueOf("2023-01-01"));
            preparedStatement.setInt(4, 1);
            preparedStatement.executeUpdate();

            preparedStatement.setInt(1, 2);
            preparedStatement.setString(2, "Aisin");
            preparedStatement.setDate(3, java.sql.Date.valueOf("2023-02-01"));
            preparedStatement.setInt(4, 2);
            preparedStatement.executeUpdate();

            preparedStatement.setInt(1, 3);
            preparedStatement.setString(2, "BorgWarner");
            preparedStatement.setDate(3, java.sql.Date.valueOf("2023-03-01"));
            preparedStatement.setInt(4, 1);
            preparedStatement.executeUpdate();

            // 插入 Vehicle 資料
            preparedStatement = connection.prepareStatement(insertVehicleSQL);
            preparedStatement.setString(1, "VIN001");
            preparedStatement.setInt(2, 1);
            preparedStatement.setInt(3, 1);
            preparedStatement.setInt(4, 1);
            preparedStatement.setInt(5, 1);
            preparedStatement.setDate(6, java.sql.Date.valueOf("2023-05-01"));
            preparedStatement.setString(7, "Red");
            preparedStatement.setString(8, "Automatic");
            preparedStatement.executeUpdate();

            preparedStatement.setString(1, "VIN002");
            preparedStatement.setInt(2, 2);
            preparedStatement.setInt(3, 2);
            preparedStatement.setInt(4, 2);
            preparedStatement.setInt(5, 2);
            preparedStatement.setDate(6, java.sql.Date.valueOf("2023-06-01"));
            preparedStatement.setString(7, "Blue");
            preparedStatement.setString(8, "Manual");
            preparedStatement.executeUpdate();

            preparedStatement.setString(1, "VIN003");
            preparedStatement.setInt(2, 3);
            preparedStatement.setInt(3, 1);
            preparedStatement.setInt(4, 3);
            preparedStatement.setInt(5, 3);
            preparedStatement.setDate(6, java.sql.Date.valueOf("2023-07-01"));
            preparedStatement.setString(7, "White");
            preparedStatement.setString(8, "Automatic");
            preparedStatement.executeUpdate();

            preparedStatement.setString(1, "VIN004");
            preparedStatement.setInt(2, 4);
            preparedStatement.setInt(3, 2);
            preparedStatement.setInt(4, 4);
            preparedStatement.setInt(5, 1);
            preparedStatement.setDate(6, java.sql.Date.valueOf("2023-08-01"));
            preparedStatement.setString(7, "Black");
            preparedStatement.setString(8, "Automatic");
            preparedStatement.executeUpdate();

            preparedStatement.setString(1, "VIN005");
            preparedStatement.setInt(2, 3);
            preparedStatement.setInt(3, 1);
            preparedStatement.setInt(4, 1);
            preparedStatement.setInt(5, 2);
            preparedStatement.setDate(6, java.sql.Date.valueOf("2023-09-01"));
            preparedStatement.setString(7, "Gray");
            preparedStatement.setString(8, "Manual");
            preparedStatement.executeUpdate();

            preparedStatement.setString(1, "VIN006");
            preparedStatement.setInt(2, 4);
            preparedStatement.setInt(3, 2);
            preparedStatement.setInt(4, 2);
            preparedStatement.setInt(5, 3);
            preparedStatement.setDate(6, java.sql.Date.valueOf("2023-10-01"));
            preparedStatement.setString(7, "Silver");
            preparedStatement.setString(8, "Automatic");
            preparedStatement.executeUpdate();

            preparedStatement.setString(1, "VIN007");
            preparedStatement.setInt(2, 5);
            preparedStatement.setInt(3, 3);
            preparedStatement.setInt(4, 5);
            preparedStatement.setInt(5, 4);
            preparedStatement.setDate(6, java.sql.Date.valueOf("2023-11-01"));
            preparedStatement.setString(7, "Blue");
            preparedStatement.setString(8, "Automatic");
            preparedStatement.executeUpdate();

            // 插入 Sales 資料
            preparedStatement = connection.prepareStatement(insertSalesSQL);
            preparedStatement.setInt(1, 1);
            preparedStatement.setString(2, "VIN001");
            preparedStatement.setDate(3, java.sql.Date.valueOf("2023-05-01"));
            preparedStatement.setDouble(4, 30000);
            preparedStatement.setInt(5, 1);
            preparedStatement.executeUpdate();

            preparedStatement.setInt(1, 2);
            preparedStatement.setString(2, "VIN002");
            preparedStatement.setDate(3, java.sql.Date.valueOf("2023-06-01"));
            preparedStatement.setDouble(4, 28000);
            preparedStatement.setInt(5, 2);
            preparedStatement.executeUpdate();

            preparedStatement.setInt(1, 3);
            preparedStatement.setString(2, "VIN003");
            preparedStatement.setDate(3, java.sql.Date.valueOf("2023-07-01"));
            preparedStatement.setDouble(4, 32000);
            preparedStatement.setInt(5, 3);
            preparedStatement.executeUpdate();

            preparedStatement.setInt(1, 4);
            preparedStatement.setString(2, "VIN004");
            preparedStatement.setDate(3, java.sql.Date.valueOf("2023-08-01"));
            preparedStatement.setDouble(4, 31000);
            preparedStatement.setInt(5, 1);
            preparedStatement.executeUpdate();

            preparedStatement.setInt(1, 5);
            preparedStatement.setString(2, "VIN005");
            preparedStatement.setDate(3, java.sql.Date.valueOf("2023-09-01"));
            preparedStatement.setDouble(4, 29000);
            preparedStatement.setInt(5, 2);
            preparedStatement.executeUpdate();

            preparedStatement.setInt(1, 6);
            preparedStatement.setString(2, "VIN006");
            preparedStatement.setDate(3, java.sql.Date.valueOf("2023-10-01"));
            preparedStatement.setDouble(4, 33000);
            preparedStatement.setInt(5, 3);
            preparedStatement.executeUpdate();

            preparedStatement.setInt(1, 7);
            preparedStatement.setString(2, "VIN007");
            preparedStatement.setDate(3, java.sql.Date.valueOf("2023-11-01"));
            preparedStatement.setDouble(4, 35000);
            preparedStatement.setInt(5, 4);
            preparedStatement.executeUpdate();

            // 插入 Inventory 資料
            preparedStatement = connection.prepareStatement(insertInventorySQL);
            preparedStatement.setString(1, "VIN001");
            preparedStatement.setInt(2, 1);
            preparedStatement.setDate(3, java.sql.Date.valueOf("2023-04-01"));
            preparedStatement.executeUpdate();

            preparedStatement.setString(1, "VIN002");
            preparedStatement.setInt(2, 2);
            preparedStatement.setDate(3, java.sql.Date.valueOf("2023-05-01"));
            preparedStatement.executeUpdate();

            preparedStatement.setString(1, "VIN003");
            preparedStatement.setInt(2, 3);
            preparedStatement.setDate(3, java.sql.Date.valueOf("2023-06-01"));
            preparedStatement.executeUpdate();

            preparedStatement.setString(1, "VIN004");
            preparedStatement.setInt(2, 1);
            preparedStatement.setDate(3, java.sql.Date.valueOf("2023-07-01"));
            preparedStatement.executeUpdate();

            preparedStatement.setString(1, "VIN005");
            preparedStatement.setInt(2, 2);
            preparedStatement.setDate(3, java.sql.Date.valueOf("2023-08-01"));
            preparedStatement.executeUpdate();

            preparedStatement.setString(1, "VIN006");
            preparedStatement.setInt(2, 3);
            preparedStatement.setDate(3, java.sql.Date.valueOf("2023-09-01"));
            preparedStatement.executeUpdate();

            preparedStatement.setString(1, "VIN007");
            preparedStatement.setInt(2, 4);
            preparedStatement.setDate(3, java.sql.Date.valueOf("2023-10-01"));
            preparedStatement.executeUpdate();

            System.out.println("資料重置並插入成功！");

        } catch (ClassNotFoundException e) {
            System.err.println("MariaDB JDBC 驅動未找到！");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("連接失敗！");
            e.printStackTrace();
        } finally {
            // 關閉資源
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
