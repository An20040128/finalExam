ResetAndInsertData為我們的資料庫

連上資料庫的帳號密碼為
String url = "jdbc:mariadb://0.tcp.jp.ngrok.io:12592/411177010";
        String user = "411177010";
        String password = "411177010";

建立連接
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();

先禁用外鍵約束
            statement.execute("SET FOREIGN_KEY_CHECKS = 0");

每次執行前都會
            ResultSet resultSet = statement.executeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema = '411177010'");
            while (resultSet.next()) {
                String tableName = resultSet.getString(1);
                statement.execute("DROP TABLE IF EXISTS " + tableName);
            }
            刪除所有表格後再以其他程式碼載入，以確保資料的更新
創建表格
            String createPlantTable = "CREATE TABLE IF NOT EXISTS Plant (" +
                    "PlantID INT PRIMARY KEY, " +
                    "PlantName VARCHAR(50))";
            
            .....

創建完後再啟用外鍵約束
            statement.execute("SET FOREIGN_KEY_CHECKS = 1");

插入資料的SQL語句
            String insertPlantSQL = "INSERT INTO Plant (PlantID, PlantName) VALUES (?, ?)";
            String insertBrandSQL = "INSERT INTO Brand (BrandID, BrandName) VALUES (?, ?)";
            String insertModelSQL = "INSERT INTO Model (ModelID, ModelName, BrandID, ModelType) VALUES (?, ?, ?, ?)";

            ...

插入各種資料
            preparedStatement = connection.prepareStatement(insertPlantSQL);
            preparedStatement.setInt(1, 1);
            preparedStatement.setString(2, "Plant A");
            preparedStatement.executeUpdate();

            ...

最後關閉資源
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
            }

