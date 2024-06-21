import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class DatabaseQueryGUI {
    private static String dbUrl = "jdbc:mariadb://0.tcp.jp.ngrok.io:12592/";
    private static String user;
    private static String password;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> showLoginScreen());
    }

    private static void showLoginScreen() {
        JFrame loginFrame = new JFrame("Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(300, 150);

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(3, 2));

        JLabel labelUser = new JLabel("USER:");
        JTextField textFieldUser = new JTextField();

        JLabel labelPassword = new JLabel("PASSWORD:");
        JPasswordField passwordField = new JPasswordField();

        JButton buttonLogin = new JButton("登入");

        buttonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                user = textFieldUser.getText();
                password = new String(passwordField.getPassword());

                if (user.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(loginFrame, "用戶名和密碼不能為空", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    loginFrame.dispose();
                    showMainScreen();
                }
            }
        });

        loginPanel.add(labelUser);
        loginPanel.add(textFieldUser);
        loginPanel.add(labelPassword);
        loginPanel.add(passwordField);
        loginPanel.add(buttonLogin);

        loginFrame.getContentPane().add(loginPanel);
        loginFrame.setVisible(true);
    }

    private static void showMainScreen() {
        JFrame frame = new JFrame("Database GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2));

        JLabel labelQuery = new JLabel("SQL輸入:");
        JTextField textFieldQuery = new JTextField();

        JButton buttonExecute = new JButton("查詢");
        JButton buttonShowAll = new JButton("查看所有資料");
        JPanel resultPanel = new JPanel(new BorderLayout());

        buttonExecute.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String query = textFieldQuery.getText();
                if (query.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "SQL輸入不能為空", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String currentDbUrl = dbUrl + user;

                try (Connection connection = DriverManager.getConnection(currentDbUrl, user, password);
                     Statement statement = connection.createStatement();
                     ResultSet resultSet = statement.executeQuery(query)) {

                    JTable table = new JTable(buildTableModel(resultSet));
                    resultPanel.removeAll();
                    resultPanel.add(new JScrollPane(table), BorderLayout.CENTER);
                    resultPanel.revalidate();
                    resultPanel.repaint();

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Error : " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        buttonShowAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentDbUrl = dbUrl + user;
                String query = "SHOW TABLES";

                try (Connection connection = DriverManager.getConnection(currentDbUrl, user, password);
                     Statement statement = connection.createStatement();
                     ResultSet resultSet = statement.executeQuery(query)) {

                    JPanel tablePanel = new JPanel();
                    tablePanel.setLayout(new GridLayout(0, 1));
                    JScrollPane scrollPane = new JScrollPane(tablePanel);

                    while (resultSet.next()) {
                        String tableName = resultSet.getString(1);
                        JButton tableButton = new JButton(tableName);
                        tableButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                displayTableData(tableName, currentDbUrl, user, password);
                            }
                        });
                        tablePanel.add(tableButton);
                    }

                    JFrame tableFrame = new JFrame("Tables");
                    tableFrame.setSize(400, 300);
                    tableFrame.add(scrollPane);
                    tableFrame.setVisible(true);

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Error : " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panel.add(labelQuery);
        panel.add(textFieldQuery);
        panel.add(buttonExecute);
        panel.add(buttonShowAll);
        panel.add(resultPanel);

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }

    private static void displayTableData(String tableName, String dbUrl, String user, String password) {
        JFrame frame = new JFrame("Table Data: " + tableName);
        frame.setSize(600, 400);
        JPanel panel = new JPanel(new BorderLayout());

        try (Connection connection = DriverManager.getConnection(dbUrl, user, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName)) {

            JTable table = new JTable(buildTableModel(resultSet));
            panel.add(new JScrollPane(table), BorderLayout.CENTER);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error : " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        frame.add(panel);
        frame.setVisible(true);
    }

    private static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();

        
        int columnCount = metaData.getColumnCount();
        Vector<String> columnNames = new Vector<>();
        for (int i = 1; i <= columnCount; i++) {
            columnNames.add(metaData.getColumnName(i));
        }

        
        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<>();
            for (int i = 1; i <= columnCount; i++) {
                vector.add(rs.getObject(i));
            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);
    }
}
