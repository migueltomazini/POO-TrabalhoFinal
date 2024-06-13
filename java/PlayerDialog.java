import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerDialog extends JDialog {
    private Player player;
    private JTextField ageField, nameField, nationalityField, clubField;
    private Font defaultFont;
    private Dimension textFieldSize = new Dimension(400, 30);
    private String fileToEdit;
    private Socket client;

    public PlayerDialog(Frame owner, Player player, Font defaultFont, String file, Socket clientReceived) {
        super(owner, "Player Details", true);

        fileToEdit = file;
        client = clientReceived;

        this.player = player;
        this.defaultFont = defaultFont;
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(createLabel("ID:"), gbc);

        gbc.gridx = 1;
        JTextField idField = createTextField(String.valueOf(player.getId()));
        idField.setEditable(false);
        add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(createLabel("Age:"), gbc);

        gbc.gridx = 1;
        ageField = createTextField(String.valueOf(player.getAge()));
        add(ageField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(createLabel("Name:"), gbc);

        gbc.gridx = 1;
        nameField = createTextField(player.getName());
        add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(createLabel("Nationality:"), gbc);

        gbc.gridx = 1;
        nationalityField = createTextField(player.getNationality());
        add(nationalityField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(createLabel("Club:"), gbc);

        gbc.gridx = 1;
        clubField = createTextField(player.getClub());
        add(clubField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        saveButton.setFont(defaultFont);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                savePlayer();
            }
        });
        buttonPanel.add(saveButton);

        JButton deleteButton = new JButton("Delete");
        deleteButton.setFont(defaultFont);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletePlayer();
            }
        });
        buttonPanel.add(deleteButton);

        add(buttonPanel, gbc);

        setSize(800, 600);
        setLocationRelativeTo(owner);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(defaultFont);
        return label;
    }

    private JTextField createTextField(String text) {
        JTextField textField = new JTextField(text);
        textField.setFont(defaultFont);
        textField.setPreferredSize(textFieldSize);
        return textField;
    }

    private void savePlayer() {
        int age = Integer.parseInt(ageField.getText());
        String name = nameField.getText();
        String nationality = nationalityField.getText();
        String club = clubField.getText();

        player.setAge(age);
        player.setName(name);
        player.setNationality(nationality);
        player.setClub(club);

        try {
            PrintStream out = new PrintStream(client.getOutputStream());

            // update
            // remover o jogador com o id
            int id = player.getId();
            String query = "5 " + fileToEdit + "index.bin 1\n" + "1 id " + id + "/";
            // adicionar o jogador novo
            query += "6 " + fileToEdit + "index.bin 1\n" + id + " " + age + " \"" + name + "\" \"" + nationality
                    + "\" \"" + club + "\" ";
            out.print(query);
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(SearchPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        dispose();
    }

    private void deletePlayer() {
        // remover o jogador
        try {
            PrintStream out = new PrintStream(client.getOutputStream());

            // remover o jogador com o id
            int id = player.getId();
            String query = "5 " + fileToEdit + " index.bin 1\n" + "1 id " + id + " ";
            out.print(query);
        } catch (IOException ex) {
            Logger.getLogger(SearchPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        dispose();
    }
}
