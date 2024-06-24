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
    private Player player; // Objeto Player que será editado
    private JTextField ageField, nameField, nationalityField, clubField;// Campos de texto para os detalhes do jogador
    private Font defaultFont;// Fonte padrão para os componentes
    private Dimension textFieldSize = new Dimension(400, 30);// Tamanho padrão para os campos de texto
    private String fileToEdit;// Nome do arquivo que está sendo editado
    private Socket client;// Socket para comunicação com o servidor

    // Construtor da classe PlayerDialog
    public PlayerDialog(Frame owner, Player player, Font defaultFont, String file, Socket clientReceived) {
        super(owner, "Player Details", true);// Chama o construtor da classe pai (JDialog) com título e modal

        fileToEdit = file;
        client = clientReceived;

        this.player = player;
        this.defaultFont = defaultFont;
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();// Configurações de layout
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Adiciona os componentes ao diálogo
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(createLabel("ID:"), gbc);

        gbc.gridx = 1;
        JTextField idField = createTextField(String.valueOf(player.getId()));
        idField.setEditable(false);// Campo de texto do ID não é editável
        add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(createLabel("Age:"), gbc);// Rótulo para a idade

        gbc.gridx = 1;
        ageField = createTextField(String.valueOf(player.getAge()));// Campo de texto para a idade
        add(ageField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(createLabel("Name:"), gbc);// Rótulo para o nome

        gbc.gridx = 1;
        nameField = createTextField(player.getName());// Campo de texto para o nome
        add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(createLabel("Nationality:"), gbc);// Rótulo para a nacionalidade

        gbc.gridx = 1;
        nationalityField = createTextField(player.getNationality());// Campo de texto para a nacionalidade
        add(nationalityField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(createLabel("Club:"), gbc);// Rótulo para o clube

        gbc.gridx = 1;
        clubField = createTextField(player.getClub());
        add(clubField, gbc);

        // Adiciona os botões de salvar e deletar
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
                savePlayer();// Ação para salvar o jogador
            }
        });
        buttonPanel.add(saveButton);

        JButton deleteButton = new JButton("Delete");// Botão de deletar
        deleteButton.setFont(defaultFont);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletePlayer();// Ação para deletar o jogador
            }
        });
        buttonPanel.add(deleteButton);

        add(buttonPanel, gbc);// Adiciona o painel de botões ao diálogo

        setSize(800, 600);
        setLocationRelativeTo(owner);
    }

    // Método para criar um rótulo com a fonte padrão
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(defaultFont);
        return label;
    }

    // Método para criar um campo de texto com a fonte e tamanho padrão
    private JTextField createTextField(String text) {
        JTextField textField = new JTextField(text);
        textField.setFont(defaultFont);
        textField.setPreferredSize(textFieldSize);
        return textField;
    }

     // Método para salvar o jogador
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
            
            // Comando para atualizar o jogador no servidor
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
        dispose();  // Fecha o diálogo
    }

    private void deletePlayer() {
        // remover o jogador
        try {
            PrintStream out = new PrintStream(client.getOutputStream());

           // Comando para deletar o jogador no servidor
            int id = player.getId();
            String query = "5 " + fileToEdit + " index.bin 1\n" + "1 id " + id + " ";
            out.print(query);
        } catch (IOException ex) {
            Logger.getLogger(SearchPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        dispose(); // Fecha o diálogo
    }
}
