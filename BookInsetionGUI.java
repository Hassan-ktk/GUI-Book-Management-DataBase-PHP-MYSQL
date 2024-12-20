import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

public class BookInsertionGUI 
{

    private static final String URL = "jdbc:mysql://localhost:3306/book_data"; 
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static Connection con;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Book Insertion");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 2, 10, 10));  

        JLabel idLabel = new JLabel("Book ID:");
        JTextField idField = new JTextField();
        idField.setHorizontalAlignment(JTextField.CENTER);
        idField.setPreferredSize(new Dimension(180, 30));

        JLabel nameLabel = new JLabel("Book Name:");
        JTextField nameField = new JTextField();
        nameField.setHorizontalAlignment(JTextField.CENTER);
        nameField.setPreferredSize(new Dimension(180, 30));

        JLabel authorLabel = new JLabel("Author:");
        JTextField authorField = new JTextField();
        authorField.setHorizontalAlignment(JTextField.CENTER);
        authorField.setPreferredSize(new Dimension(180, 30));

        JLabel isbnLabel = new JLabel("ISBN:");
        JTextField isbnField = new JTextField();
        isbnField.setHorizontalAlignment(JTextField.CENTER);
        isbnField.setPreferredSize(new Dimension(180, 30));

        JLabel availabilityLabel = new JLabel("Availability:");
        JTextField availabilityField = new JTextField();
        availabilityField.setHorizontalAlignment(JTextField.CENTER);
        availabilityField.setPreferredSize(new Dimension(180, 30));

        JButton submitButton = new JButton("Insert Book");

        JButton deleteButton = new JButton("Delete Book");
        JButton updateButton = new JButton("Update Book");
        JButton retrieveButton = new JButton("Retrieve Book");

        JTextField retrieveInfoField = new JTextField();
        retrieveInfoField.setPreferredSize(new Dimension(200, 80));
        retrieveInfoField.setEditable(false);
        retrieveInfoField.setHorizontalAlignment(JTextField.CENTER);

        panel.add(idLabel);
        panel.add(idField);
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(authorLabel);
        panel.add(authorField);
        panel.add(isbnLabel);
        panel.add(isbnField);
        panel.add(availabilityLabel);
        panel.add(availabilityField);

        panel.add(deleteButton);
        panel.add(updateButton);
        panel.add(retrieveButton);
        panel.add(submitButton);

        panel.add(new JLabel("Retrieved Info:"));
        panel.add(retrieveInfoField);

        frame.add(panel);
        frame.setVisible(true);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                try 
                {
                    con = DriverManager.getConnection(URL, USER, PASSWORD);

                    int id = Integer.parseInt(idField.getText());
                    String name = nameField.getText();
                    String author = authorField.getText();
                    int isbn = Integer.parseInt(isbnField.getText());
                    String availability = availabilityField.getText();

                    createBook(id, name, author, isbn, availability);

                    JOptionPane.showMessageDialog(frame, "Book inserted successfully!");

                    idField.setText("");
                    nameField.setText("");
                    authorField.setText("");
                    isbnField.setText("");
                    availabilityField.setText("");
                } 
                catch (SQLException ex) 
                {
                    JOptionPane.showMessageDialog(frame, "Error inserting book: " + ex.getMessage());
                    ex.printStackTrace();
                } 
                catch (NumberFormatException ex) 
                {
                    JOptionPane.showMessageDialog(frame, "Please enter valid numbers for ID and ISBN.");
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    con = DriverManager.getConnection(URL, USER, PASSWORD);

                    int id = Integer.parseInt(idField.getText());
                    deleteBook(id);

                    JOptionPane.showMessageDialog(frame, "Book deleted successfully!");
                    idField.setText("");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Error deleting book: " + ex.getMessage());
                    ex.printStackTrace();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid ID.");
                }
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    con = DriverManager.getConnection(URL, USER, PASSWORD);

                    int id = Integer.parseInt(idField.getText());
                    String name = nameField.getText();
                    String author = authorField.getText();
                    int isbn = Integer.parseInt(isbnField.getText());
                    String availability = availabilityField.getText();

                    updateBook(id, name, author, isbn, availability);

                    JOptionPane.showMessageDialog(frame, "Book updated successfully!");

                    idField.setText("");
                    nameField.setText("");
                    authorField.setText("");
                    isbnField.setText("");
                    availabilityField.setText("");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Error updating book: " + ex.getMessage());
                    ex.printStackTrace();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter valid numbers for ID and ISBN.");
                }
            }
        });

        retrieveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    con = DriverManager.getConnection(URL, USER, PASSWORD);

//                    int id = Integer.parseInt(idField.getText());
//                    retrieveBook(id, retrieveInfoField);
                    retrieveBook();

                    JOptionPane.showMessageDialog(frame, "Book retrieved successfully!");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Error retrieving book: " + ex.getMessage());
                    ex.printStackTrace();
                } 
//                catch (NumberFormatException ex) {
//                    JOptionPane.showMessageDialog(frame, "Please enter a valid ID.");
//                }
            }
        });
    }

    private static void createBook(int id, String name, String author, int isbn, String availability) {
        String query = "INSERT INTO books(id, name, author, isbn, availability) VALUES(?, ?, ?, ?, ?)";
        try (PreparedStatement preStat = con.prepareStatement(query)) 
        {
            preStat.setInt(1, id);
            preStat.setString(2, name);
            preStat.setString(3, author);
            preStat.setInt(4, isbn);
            preStat.setString(5, availability);
            preStat.executeUpdate();
            
        } 
        catch (SQLException ex) 
        {
            System.err.println("Error inserting book: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static void deleteBook(int id) {
        String query = "DELETE FROM books WHERE id = ?";
        try (PreparedStatement preStat = con.prepareStatement(query)) {
            preStat.setInt(1, id);
            preStat.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error deleting book: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static void updateBook(int id, String name, String author, int isbn, String availability) {
        String query = "UPDATE books SET name = ?, author = ?, isbn = ?, availability = ? WHERE id = ?";
        try (PreparedStatement preStat = con.prepareStatement(query)) {
            preStat.setString(1, name);
            preStat.setString(2, author);
            preStat.setInt(3, isbn);
            preStat.setString(4, availability);
            preStat.setInt(5, id);
            preStat.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error updating book: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

//    private static void retrieveBook(int id, JTextField retrieveInfoField) {
//        String query = "SELECT * FROM books WHERE id = ?";
//        try (PreparedStatement preStat = con.prepareStatement(query)) {
//            preStat.setInt(1, id);
//            ResultSet rs = preStat.executeQuery();
//            if (rs.next()) {
//                String bookDetails = "ID: " + rs.getInt("id") + "\n" +
//                        "Name: " + rs.getString("name") + "\n" +
//                        "Author: " + rs.getString("author") + "\n" +
//                        "ISBN: " + rs.getInt("isbn") + "\n" +
//                        "Availability: " + rs.getString("availability");
//                retrieveInfoField.setText(bookDetails);
//            } else {
//                retrieveInfoField.setText("No book found with ID: " + id);
//            }
//        } catch (SQLException ex) {
//            System.err.println("Error retrieving book: " + ex.getMessage());
//            ex.printStackTrace();
//        }
//    }
    
    private static void retrieveBook() {
    String query = "SELECT * FROM books";
    try (Statement statement = con.createStatement()) 
    {
        ResultSet rs = statement.executeQuery(query);
        System.out.println("Books in the Database:");
        while (rs.next()) {
            System.out.println("--------------------------------");
            System.out.println("ID: " + rs.getInt("id"));
            System.out.println("Name: " + rs.getString("name"));
            System.out.println("Author: " + rs.getString("author"));
            System.out.println("ISBN: " + rs.getInt("isbn"));
            System.out.println("Availability: " + rs.getString("availability"));
        }
    } 
    catch (SQLException ex) 
    {
        System.err.println("Error retrieving books: " + ex.getMessage());
        ex.printStackTrace();
    }
}

}
